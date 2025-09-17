package com.sideforge.ringring.api.domain.auth.repository;

import com.sideforge.ringring.api.common.model.enums.RedisKey;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class TokenStore {
    private final StringRedisTemplate redisTemplate;

    public void saveRefresh(String accountId, String refresh, Duration ttl) {
        String key = RedisKey.REFRESH_TOKEN.format(accountId);
        redisTemplate.opsForValue().set(key, refresh, ttl);
    }

    public String getRefresh(String accountId) {
        String key = RedisKey.REFRESH_TOKEN.format(accountId);
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteRefresh(String accountId) {
        redisTemplate.delete(RedisKey.REFRESH_TOKEN.format(accountId));
    }

    public void saveBlacklist(String jti, Duration ttl) {
        redisTemplate.opsForValue().set(RedisKey.BLACKLIST_TOKEN.format(jti), "1", ttl);
    }

    public boolean isBlacklisted(String jti) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(RedisKey.BLACKLIST_TOKEN.format(jti)));
    }
}
