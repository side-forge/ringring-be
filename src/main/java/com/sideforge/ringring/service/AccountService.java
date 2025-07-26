package com.sideforge.ringring.service;

import com.sideforge.ringring.common.util.CodeGenerator;
import com.sideforge.ringring.model.dto.req.account.EmailVerificationReqBodyDto;
import com.sideforge.ringring.model.entity.redis.EmailVerificationCode;
import com.sideforge.ringring.repository.AccountRepository;
import com.sideforge.ringring.repository.EmailVerificationCodeRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final EmailVerificationCodeRepository emailVerificationCodeRepository;
    private final JavaMailSender mailSender;

    // ToDo. 하드코딩 제거
    @Async
    public void sendSignupVerificationEmail(EmailVerificationReqBodyDto reqBodyDto) {
        String verificationCode = CodeGenerator.generateNumericCode(6);
        String recipientEmail = reqBodyDto.getEmail();
        String senderEmail = "sideforge.manager@gmail.com";
        String subject = "[RingRing] Email Verification Code";

        String body = """
            <p>Welcome to <strong>RingRing</strong>!</p>
            <p>Your verification code is:</p>
            <h2>%s</h2>
            <p>Please enter this code in the app to verify your email address.</p>
            """.formatted(verificationCode);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(senderEmail);
            helper.setTo(recipientEmail);
            helper.setSubject(subject);
            helper.setText(body, true);
            mailSender.send(message);

            // 인증 코드 저장
            emailVerificationCodeRepository.save(EmailVerificationCode.builder()
                    .email(recipientEmail)
                    .code(verificationCode)
                    .build());

        } catch (MessagingException e) {
            log.error("Failed to send verification email to {}: {}", recipientEmail, e.getMessage(), e);
        }
    }
}
