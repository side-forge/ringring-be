package com.sideforge.ringring.api.domain.account.service;

import com.sideforge.ringring.api.common.model.enums.RedisKey;
import com.sideforge.ringring.api.domain.account.model.dto.request.EmailVerificationCheckReqDto;
import com.sideforge.ringring.api.domain.account.model.dto.request.EmailVerificationReqDto;
import com.sideforge.ringring.api.domain.account.model.dto.request.UserAvailabilityReqDto;
import com.sideforge.ringring.api.domain.account.model.dto.response.UserAvailabilityResDto;
import com.sideforge.ringring.api.domain.account.model.enums.EmailTemplateType;
import com.sideforge.ringring.api.domain.account.model.enums.UserIdentifierType;
import com.sideforge.ringring.api.domain.account.repository.AccountRepository;
import com.sideforge.ringring.api.domain.account.repository.AuthCodeStore;
import com.sideforge.ringring.config.properties.CryptoProperties;
import com.sideforge.ringring.exception.dto.InvalidRequestContentsException;
import com.sideforge.ringring.exception.dto.InvalidValueException;
import com.sideforge.ringring.exception.dto.TooManyRequestException;
import com.sideforge.ringring.util.CodeGenerator;
import com.sideforge.ringring.util.CryptoUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {
    private final MailService mailService;
    private final CryptoProperties cryptoProperties;
    private final AccountRepository accountRepository;
    private final AuthCodeStore authCodeStore;

    public UserAvailabilityResDto checkIdentifier(UserAvailabilityReqDto reqDto) {
        UserIdentifierType type = reqDto.getType();
        String raw = reqDto.getValue();

        // 유효성 검증
        if (!type.isValid(raw)) {
            return new UserAvailabilityResDto(false);
        }

        // 중복 검증
        String normalized = type.normalize(raw);
        boolean exists = switch (type) {
            case EMAIL -> accountRepository.existsByEmail(normalized);
            case PHONE -> accountRepository.existsByPhoneNumber(normalized);
        };

        return new UserAvailabilityResDto(!exists);
    }

    public void sendAuthCodeEmail(EmailVerificationReqDto reqDto) {
        String email = UserIdentifierType.EMAIL.normalize(reqDto.getEmail());

        // 이메일 락 여부 체크
        if (authCodeStore.isLocked(RedisKey.AUTH_CODE_EMAIL_LOCK.format(email))) {
            throw new TooManyRequestException("Too many failed attempts. Try again later.");
        }

        // 요청 쿨다운 체크
        if (!authCodeStore.acquireCooldown(RedisKey.AUTH_CODE_EMAIL_CD.format(email), RedisKey.AUTH_CODE_EMAIL_CD.getTtl())) {
            throw new TooManyRequestException("Please wait before requesting a new code.");
        }

        // 코드 생성 및 암호화
        String rawCode = CodeGenerator.generateNumericCode(6);
        String hashCode = CryptoUtils.hmacSha256(rawCode, cryptoProperties.getSecretKey());

        authCodeStore.saveCode(
                RedisKey.AUTH_CODE_EMAIL.format(email),
                RedisKey.AUTH_CODE_EMAIL_ATTEMPTS.format(email),
                hashCode,
                RedisKey.AUTH_CODE_EMAIL.getTtl()
        );

        // 이메일 전송 (비동기)
        mailService.sendEmail(EmailTemplateType.AUTH_EMAIL, email, rawCode);
    }

    public void verifyAuthCodeEmail(EmailVerificationCheckReqDto reqDto) {
        String email = UserIdentifierType.EMAIL.normalize(reqDto.getEmail());
        String rawCode = reqDto.getCode();

        String codeKey = RedisKey.AUTH_CODE_EMAIL.format(email);
        String attemptsKey = RedisKey.AUTH_CODE_EMAIL_ATTEMPTS.format(email);
        String lockKey = RedisKey.AUTH_CODE_EMAIL_LOCK.format(email);
        String verifiedKey = RedisKey.AUTH_CODE_EMAIL_VERIFIED.format(email);

        // Lock 체크
        if (authCodeStore.isLocked(lockKey)) {
            throw new TooManyRequestException("Too many failed attempts. Try again later.");
        }

        // 저장된 해시 조회 및 입력 코드 해시화
        String storedHash = authCodeStore.getCode(codeKey);
        if (storedHash == null) {
            throw new InvalidValueException("No verification code found or code expired.");
        }
        String inputHash = CryptoUtils.hmacSha256(rawCode, cryptoProperties.getSecretKey());

        // 입력한 코드와 저장된 코드 비교
        boolean match = java.security.MessageDigest.isEqual(
                storedHash.getBytes(java.nio.charset.StandardCharsets.UTF_8),
                inputHash.getBytes(java.nio.charset.StandardCharsets.UTF_8)
        );

        if (match) {
            // 인증 코드 및 인증 시도 횟수 정리
            authCodeStore.delete(codeKey);
            authCodeStore.delete(attemptsKey);

            // 인증 확인 티켓 발급
            authCodeStore.saveVerifiedTicket(verifiedKey, "1", RedisKey.AUTH_CODE_EMAIL_VERIFIED.getTtl());
        } else {
            // 인증 시도 횟수 차감
            int remain = authCodeStore.decrementAttempts(attemptsKey);

            // attemptsKey TTL 만료/누락으로 null→-1 즉, 만료로 간주
            if (remain == -1) {
                throw new InvalidValueException("No verification code found or code expired.");
            }

            if (remain <= 0) {
                authCodeStore.lock(lockKey, RedisKey.AUTH_CODE_EMAIL_LOCK.getTtl());
                throw new TooManyRequestException("Too many failed attempts. Locked temporarily.");
            }
            throw new InvalidRequestContentsException("Invalid verification code. Attempts left: " + remain);
        }
    }
}
