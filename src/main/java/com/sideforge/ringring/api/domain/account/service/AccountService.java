package com.sideforge.ringring.api.domain.account.service;

import com.sideforge.ringring.api.domain.account.model.dto.request.UserAvailabilityReqDto;
import com.sideforge.ringring.api.domain.account.model.dto.response.UserAvailabilityResDto;
import com.sideforge.ringring.api.domain.account.model.enums.UserIdentifierType;
import com.sideforge.ringring.util.CodeGenerator;
import com.sideforge.ringring.api.domain.account.model.dto.request.EmailVerificationReqDto;
import com.sideforge.ringring.api.domain.account.model.entity.EmailVerificationCode;
import com.sideforge.ringring.api.domain.account.model.enums.EmailTemplateType;
import com.sideforge.ringring.api.domain.account.repository.AccountRepository;
import com.sideforge.ringring.api.domain.account.repository.EmailVerificationCodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {
    private final MailService mailService;
    private final AccountRepository accountRepository;
    private final EmailVerificationCodeRepository emailVerificationCodeRepository;

    @Transactional
    public void sendEmailVerification(EmailVerificationReqDto reqDto) {
        String verificationCode = CodeGenerator.generateNumericCode(6);
        String recipientEmail = reqDto.getEmail();

        // 인증 코드 저장
        emailVerificationCodeRepository.save(EmailVerificationCode.builder()
                .email(recipientEmail)
                .code(verificationCode)
                .build());

        // 이메일 전송
        mailService.sendEmail(EmailTemplateType.SIGNUP_EMAIL_VERIFICATION, recipientEmail, verificationCode);
    }

    public UserAvailabilityResDto checkIdentifier(UserAvailabilityReqDto reqDto) {
        UserIdentifierType type = reqDto.getType();
        String raw = reqDto.getValue();
        
        // 유효성 검증
        if (!type.isValid(raw)) {
            return new UserAvailabilityResDto(false);
        }

        String normalized = type.normalize(raw);

        // 중복 검증
        boolean exists = switch (type) {
            case EMAIL -> accountRepository.existsByEmail(normalized);
            case PHONE -> accountRepository.existsByPhoneNumber(normalized);
        };
        
        return new UserAvailabilityResDto(!exists);
    }
}
