package com.sideforge.ringring.api.common.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Duration;

@Getter
@AllArgsConstructor
public enum RedisKey {
    /**
     * ── Email Verification Code ──────────────────────────────────────────
     * - key: ringring:auth:code:email:{email}
     * - TTL: 10분
     * - 용도: 이메일 본인 인증 시 발급된 코드(HMAC 해시 등)를 저장
     * - args: email
     */
    AUTH_CODE_EMAIL("ringring:auth:code:email:%s", Duration.ofMinutes(10)),

    /**
     * ── Email Verification Cooldown ──────────────────────────────────────
     * - key: ringring:auth:code:email:{email}:cd
     * - TTL: 60초
     * - 용도: 동일 이메일로 짧은 시간 내 중복 발송을 막기 위한 쿨다운 플래그
     * - args: email
     */
    AUTH_CODE_EMAIL_CD("ringring:auth:code:email:%s:cd", Duration.ofSeconds(60)),

    /**
     * ── Email Verification Attempts ─────────────────────────────────────
     * - key: ringring:auth:code:email:{email}:attempts
     * - TTL: 10분 (코드 TTL과 동일)
     * - 용도: 인증 코드 검증 시도 횟수를 기록하여 brute-force 방지
     * - args: email
     */
    AUTH_CODE_EMAIL_ATTEMPTS("ringring:auth:code:email:%s:attempts", Duration.ofMinutes(10)),

    /**
     * ── Email Verification Lock ─────────────────────────────────────────
     * - key: ringring:auth:code:email:{email}:lock
     * - TTL: 10분
     * - 용도: 시도 횟수 초과 시 해당 이메일 계정에 대한 인증 요청 잠금
     * - args: email
     */
    AUTH_CODE_EMAIL_LOCK("ringring:auth:code:email:%s:lock", Duration.ofMinutes(10)),

    /**
     * ── Email Verification Verified Ticket ────────────────────────────────
     * - key: ringring:auth:code:email:{email}:verified
     * - TTL: 10분
     * - 용도: 사용자가 이메일 인증코드를 성공적으로 검증했음을 나타내는 단발성 티켓
     * - args: email
     */
    AUTH_CODE_EMAIL_VERIFIED("ringring:auth:code:email:%s:verified", Duration.ofMinutes(10)),

    /**
     * ── JWT Blacklist ──────────────────────────────────────────────────
     * - key: ringring:auth:jwt:blacklist:{tokenId}
     * - TTL: 24시간 (토큰 만료 시간과 맞추어 설정 권장)
     * - 용도: 강제 로그아웃/토큰 무효화 시 블랙리스트 등록
     * - args: tokenId(JWT JTI 또는 고유 식별자)
     */
    BLACKLIST_TOKEN("ringring:auth:jwt:blacklist:%s", Duration.ofHours(24)),

    /**
     * ── Refresh Token Storage ──────────────────────────────────────────
     * - key: ringring:auth:refresh:{userId}
     * - TTL: 14일
     * - 용도: 사용자별 Refresh Token 저장  ==>  사용자 단위가 아닌 사용자/디바이스 단위로 저장되어야 하지만 생략
     * - args: userId
     */
    REFRESH_TOKEN("ringring:auth:jwt:refresh:%s", Duration.ofDays(14));

    private final String pattern;
    private final Duration ttl;

    public String format(Object... args) { return String.format(pattern, args); }
}


