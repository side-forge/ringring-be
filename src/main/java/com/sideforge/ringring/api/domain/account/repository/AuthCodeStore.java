package com.sideforge.ringring.api.domain.account.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class AuthCodeStore {
    private final StringRedisTemplate redisTemplate;

    public static final int MAX_ATTEMPTS = 5;

    // 인증 코드, 시도 횟수 저장
    public void saveCode(String dataKey, String attemptsKey, String hash, Duration ttl) {
        redisTemplate.executePipelined((RedisCallback<Object>) conn -> {
            var sc = conn.stringCommands();
            sc.setEx(dataKey.getBytes(), ttl.toSeconds(), hash.getBytes());
            sc.setEx(attemptsKey.getBytes(), ttl.toSeconds(), String.valueOf(MAX_ATTEMPTS).getBytes());
            return null;
        });
    }

    public boolean isLocked(String lockKey) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(lockKey));
    }

    public boolean acquireCooldown(String cdKey, Duration ttl) {
        return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(cdKey, "1", ttl));
    }

    public String getCode(String codeKey) {
        return redisTemplate.opsForValue().get(codeKey);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public boolean exists(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public void lock(String lockKey, Duration ttl) {
        redisTemplate.opsForValue().set(lockKey, "1", ttl);
    }

    // 남은 시도 횟수 차감 (만료 시 -1 반환)
    public int decrementAttempts(String attemptsKey) {
        Long left = redisTemplate.execute((RedisCallback<Long>) conn ->
                conn.stringCommands().decr(attemptsKey.getBytes()));
        return left == null ? -1 : left.intValue();
    }
    
    // 인증 코드 확인 후 완료 티켓 발급
    public void saveVerifiedTicket(String key, String value, Duration ttl) {
        redisTemplate.opsForValue().set(key, value, ttl);
    }
}
