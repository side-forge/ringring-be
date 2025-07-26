package com.sideforge.ringring.service;

import com.sideforge.ringring.common.util.CodeGenerator;
import com.sideforge.ringring.model.dto.req.account.EmailVerificationReqDto;
import com.sideforge.ringring.model.entity.redis.EmailVerificationCode;
import com.sideforge.ringring.model.enums.EmailTemplateType;
import com.sideforge.ringring.repository.AccountRepository;
import com.sideforge.ringring.repository.EmailVerificationCodeRepository;
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
