package com.sideforge.ringring.api.domain.account.service;

import com.sideforge.ringring.util.CodeGenerator;
import com.sideforge.ringring.api.domain.account.model.dto.request.EmailVerificationReqDto;
import com.sideforge.ringring.api.domain.account.model.entity.EmailVerificationCode;
import com.sideforge.ringring.api.domain.account.model.enums.EmailTemplateType;
import com.sideforge.ringring.api.domain.account.repository.AccountRepository;
import com.sideforge.ringring.api.domain.account.repository.EmailVerificationCodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {
    private final MailService mailService;
    private final AccountRepository accountRepository;
    private final EmailVerificationCodeRepository emailVerificationCodeRepository;

    public boolean isEmailDuplicated(String email) {
        return accountRepository.existsByEmail(email);
    }

    public void sendSignupVerificationEmail(EmailVerificationReqDto reqDto) {
        String verificationCode = CodeGenerator.generateNumericCode(6);
        String recipientEmail = reqDto.getEmail();

        // 인증 코드 저장
        emailVerificationCodeRepository.save(EmailVerificationCode.builder()
                .email(recipientEmail)
                .code(verificationCode)
                .build());

        // 이메일 전송
        mailService.sendEmailAsync(EmailTemplateType.SIGNUP_EMAIL_VERIFICATION, recipientEmail, verificationCode);
    }
}
