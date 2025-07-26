package com.sideforge.ringring.model.enums;

import com.sideforge.ringring.common.exception.dto.ResourceLoadException;
import lombok.Getter;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

// 이메일 템플릿 타입
@Getter
public enum EmailTemplateType {
    SIGNUP_EMAIL_VERIFICATION("\uD83D\uDCEC RingRing 이메일 인증","templates/signup-email-verification.html"),
    RESET_PASSWORD_VERIFICAION("\uD83D\uDD10 RingRing 비밀번호 재설정", "templates/reset-password-verification.html");

    private final String subject;
    private final String path;

    EmailTemplateType(String subject, String path) {
        this.subject = subject;
        this.path = path;
    }

    public String loadTemplate() {
        try (InputStream inputStream = new ClassPathResource(this.path).getInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new ResourceLoadException(this.path);
        }
    }
}
