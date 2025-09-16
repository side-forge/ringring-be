package com.sideforge.ringring.api.domain.account.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class EmailVerificationCodeStore {
    private final StringRedisTemplate redisTemplate;

    public void saveCode(String dataKey, String attemptsKey, String hash, Duration ttl) {
        redisTemplate.executePipelined((RedisCallback<Object>) conn -> {
            var sc = conn.stringCommands();
            sc.setEx(dataKey.getBytes(), ttl.toSeconds(), hash.getBytes());
            sc.setEx(attemptsKey.getBytes(), ttl.toSeconds(), "0".getBytes());
            return null;
        });
    }

    public boolean isLocked(String lockKey) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(lockKey));
    }

    public boolean acquireCooldown(String cdKey, Duration ttl) {
        return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(cdKey, "1", ttl));
    }
}
