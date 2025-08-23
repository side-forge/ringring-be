package com.sideforge.ringring.api.domain.account.service;

import com.sideforge.ringring.config.properties.MailProperties;
import com.sideforge.ringring.api.domain.account.model.enums.EmailTemplateType;
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
public class MailService {
    private final JavaMailSender mailSender;
    private final MailProperties mailProperties;

    @Async
    public void sendEmailAsync(EmailTemplateType emailTemplate, String to, String code) {
        String from = mailProperties.getUsername();
        String subject = emailTemplate.getSubject();
        String template = emailTemplate.loadTemplate();
        String body = String.format(template, code);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            log.error("Failed to send verification email to {}: {}", to, e.getMessage(), e);
        }
    }
}
