package com.sideforge.ringring.api.domain.account.service;

import com.sideforge.ringring.api.common.model.enums.RedisKey;
import com.sideforge.ringring.api.domain.account.model.dto.request.EmailVerificationReqDto;
import com.sideforge.ringring.api.domain.account.model.dto.request.UserAvailabilityReqDto;
import com.sideforge.ringring.api.domain.account.model.dto.response.UserAvailabilityResDto;
import com.sideforge.ringring.api.domain.account.model.enums.EmailTemplateType;
import com.sideforge.ringring.api.domain.account.model.enums.UserIdentifierType;
import com.sideforge.ringring.api.domain.account.repository.AccountRepository;
import com.sideforge.ringring.api.domain.account.repository.AuthCodeStore;
import com.sideforge.ringring.config.properties.CryptoProperties;
import com.sideforge.ringring.exception.dto.TooManyRequestException;
import com.sideforge.ringring.util.CodeGenerator;
import com.sideforge.ringring.util.CryptoUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public void sendAuthCodeEmail(EmailVerificationReqDto reqDto) {
        String email = UserIdentifierType.EMAIL.normalize(reqDto.getEmail());

        // 이메일 락 여부 체크
        if (authCodeStore.isLocked(RedisKey.AUTH_CODE_EMAIL_LOCK.format(email))) {
            throw new TooManyRequestException("Too many failed attempts. Try again later.");
        }

        // 요청 쿨다운 체크
        if (!authCodeStore.acquireCooldown(RedisKey.AUTH_CODE_EMAIL_CD.format(email),
                RedisKey.AUTH_CODE_EMAIL_CD.getTtl())) {
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
}
