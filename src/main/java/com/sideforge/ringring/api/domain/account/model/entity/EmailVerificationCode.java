package com.sideforge.ringring.api.domain.account.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Builder
@AllArgsConstructor
@RedisHash(value = "email-verification-code", timeToLive = 60*10)
public class EmailVerificationCode {
    @Id
    private String email;
    private String code;
}
