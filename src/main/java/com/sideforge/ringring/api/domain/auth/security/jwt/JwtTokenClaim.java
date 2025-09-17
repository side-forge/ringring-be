package com.sideforge.ringring.api.domain.auth.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JwtTokenClaim {
    ACCOUNT_ID("sub"),
    EMAIL("email"),
    ISSUER("iss"),
    ROLE("roles"),
    ISSUED_AT("iat"),
    EXPIRATION("exp"),
    JTI("jti");

    private final String name;
}
