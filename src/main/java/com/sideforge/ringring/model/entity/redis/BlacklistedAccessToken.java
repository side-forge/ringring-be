package com.sideforge.ringring.model.entity.redis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Builder
@AllArgsConstructor
@RedisHash(value = "refresh-token", timeToLive = 60*60*24)
public class BlacklistedAccessToken {
    @Id
    private String token;
}