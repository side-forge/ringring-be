package com.sideforge.ringring.api.domain.auth.security.jwt;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface JwtTokenPayload {
    JwtTokenClaim claim() default JwtTokenClaim.ACCOUNT_ID;
    boolean required() default true;
}
