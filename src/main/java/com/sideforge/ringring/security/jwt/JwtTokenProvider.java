package com.sideforge.ringring.security.jwt;

import com.sideforge.ringring.common.config.properties.JwtProperties;
import com.sideforge.ringring.common.exception.dto.InvalidTokenException;
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
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String TOKEN_ISSUER = "sideforge-ringring-be";

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes());
    }

    public String generateAccessToken(Authentication authentication) {
        String roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        // UTC
        Instant now = Instant.now();
        Instant expiry = now.plusMillis(jwtProperties.getAccessTokenExpire());

        // ToDo. sub는 "authentication.getName()" 가 아닌 uuid 와 같은 키로 변경 필요
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(JwtTokenClaim.ROLE.getName(), roles)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiry))
                .setIssuer(TOKEN_ISSUER)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String generateRefreshToken(Authentication authentication) {
        // UTC
        Instant now = Instant.now();
        Instant expiry = now.plusMillis(jwtProperties.getAccessTokenExpire());

        // ToDo. sub는 "authentication.getName()" 가 아닌 uuid 와 같은 키로 변경 필요
        return Jwts.builder()
                .setSubject(authentication.getName())
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

    public String getUserId(String token) {
        return parseClaims(token).getSubject();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);
        String roles = claims.get(JwtTokenClaim.ROLE.getName(), String.class);

        List<SimpleGrantedAuthority> authorities = Optional.ofNullable(roles)
                .map(r -> Arrays.stream(r.split(","))
                        .filter(s -> !s.isEmpty())
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());

        // ToDo. 커스텀 User 클래스 정의 필요
        // password는 null 또는 빈 문자열로 처리
        User principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }
}
