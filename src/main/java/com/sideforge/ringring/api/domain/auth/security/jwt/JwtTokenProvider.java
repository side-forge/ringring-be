package com.sideforge.ringring.api.domain.auth.security.jwt;

import com.sideforge.ringring.config.properties.JwtProperties;
import com.sideforge.ringring.exception.dto.InvalidTokenException;
import com.sideforge.ringring.api.domain.auth.repository.BlacklistedAccessTokenRepository;
import com.sideforge.ringring.api.domain.auth.model.principal.CustomUserPrincipal;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final JwtProperties jwtProperties;
    private final BlacklistedAccessTokenRepository blacklistedAccessTokenRepository;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String TOKEN_ISSUER = "sideforge-ringring-be";

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes());
    }

    public String generateAccessToken(Authentication authentication) {
        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
        String userId = principal.getAccountId();
        String email = principal.getUsername();

        String roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        // UTC
        Instant now = Instant.now();
        Instant expiry = now.plusMillis(jwtProperties.getAccessTokenExpire());

        return Jwts.builder()
                .setSubject(userId)
                .claim(JwtTokenClaim.ROLE.getName(), roles)
                .claim(JwtTokenClaim.EMAIL.getName(), email)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiry))
                .setIssuer(TOKEN_ISSUER)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String generateRefreshToken(Authentication authentication) {
        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
        String userId = principal.getAccountId();

        // UTC
        Instant now = Instant.now();
        Instant expiry = now.plusMillis(jwtProperties.getRefreshTokenExpire());

        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiry))
                .setIssuer(TOKEN_ISSUER)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            // 블랙리스트에 등재된 토큰 여부 체크
            if (blacklistedAccessTokenRepository.existsById(token)) {
                log.warn("Token is blacklisted: {}", token);
                return false;
            }

            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("Token expired during validation: {}", e.getMessage());
        } catch (JwtException | IllegalArgumentException e) {
            log.debug("Token validation failed: {}", e.getClass().getSimpleName(), e);
        }
        return false;
    }

    public Claims parseClaims(String token) {
        if (token == null || token.trim().isEmpty()) {
            throw new InvalidTokenException("JWT token is missing or empty");
        }

        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.warn("Token is expired but returning claims. Reason: {}", e.getMessage());
            return e.getClaims();
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Token parsing failed: {} - {}", e.getClass().getSimpleName(), e.getMessage());
            throw new InvalidTokenException("Failed to parse JWT token", e);
        }
    }

    public String getAccountId(String token) {
        return parseClaims(token).getSubject();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);
        String userId = claims.get(JwtTokenClaim.ACCOUNT_ID.getName(), String.class);
        String email = claims.get(JwtTokenClaim.EMAIL.getName(), String.class);
        String roles = claims.get(JwtTokenClaim.ROLE.getName(), String.class);

        List<SimpleGrantedAuthority> authorities = Optional.ofNullable(roles)
                .map(roleStr -> Arrays.stream(roleStr.split(","))
                        .map(String::trim)
                        .filter(role -> !role.isEmpty())
                        .distinct() // 중복 제거
                        .map(SimpleGrantedAuthority::new)
                        .toList()
                )
                .orElseGet(List::of);

        CustomUserPrincipal principal = CustomUserPrincipal.from(userId, email, authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, principal.getAuthorities());
    }
}
