package com.sideforge.ringring.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JwtTokenClaim {
    USER_ID("sub"),
    ISSUER("iss"),
    ROLE("roles"),
    ISSUED_AT("iat"),
    EXPIRATION("exp");

    private final String name;
}
