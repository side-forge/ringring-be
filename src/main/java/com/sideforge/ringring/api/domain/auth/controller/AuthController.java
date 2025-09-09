package com.sideforge.ringring.api.domain.auth.controller;

import com.sideforge.ringring.api.common.model.dto.ApiCommonResDto;
import com.sideforge.ringring.api.common.service.ResponseService;
import com.sideforge.ringring.api.domain.auth.model.dto.request.LoginReqDto;
import com.sideforge.ringring.api.domain.auth.model.dto.response.LoginResDto;
import com.sideforge.ringring.api.domain.auth.model.dto.response.TokenReissueResDto;
import com.sideforge.ringring.api.domain.auth.security.jwt.JwtTokenProvider;
import com.sideforge.ringring.api.domain.auth.service.AuthService;
import com.sideforge.ringring.config.properties.JwtProperties;
import com.sideforge.ringring.exception.dto.AccountNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final ResponseService responseService;
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtProperties jwtProperties;

    @PostMapping("/login")
    public ResponseEntity<ApiCommonResDto<LoginResDto>> login(
            @Valid @RequestBody LoginReqDto reqDto
    ) {
        LoginResDto result;
        try {
            result = authService.login(reqDto.getEmail(), reqDto.getPassword());
        } catch (BadCredentialsException | AccountNotFoundException e) {
            // 실패 카운트 증가 및 잠금 처리
            authService.handleLoginFailure(reqDto.getEmail());
            throw new AccountNotFoundException("Invalid email or password");
        }

        ResponseCookie cookie = ResponseCookie.from(jwtProperties.getCookieConfig().getName(), result.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path(jwtProperties.getCookieConfig().getPath())
                .maxAge(Duration.ofMillis(jwtProperties.getRefreshTokenExpire()))
                .build();

        result.setRefreshToken(null);

        return responseService.resSuccessWithCookies(result, cookie);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiCommonResDto<Void>> logout(
            HttpServletRequest request
    ) {
        String token = jwtTokenProvider.resolveToken(request);
        String accountId = jwtTokenProvider.getAccountId(token);
        authService.logout(token, accountId);
        ResponseCookie cookie = ResponseCookie.from(jwtProperties.getCookieConfig().getName(), "")
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path(jwtProperties.getCookieConfig().getPath())
                .maxAge(Duration.ofMillis(0))
                .build();

        return responseService.resSuccessWithCookies(cookie);
    }

    @PostMapping("/token/reissue")
    public ResponseEntity<ApiCommonResDto<TokenReissueResDto>> reissue(
            @CookieValue(name = "#{@jwtProperties.cookieConfig.name}", required = false) String refreshToken
    ) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new AccountNotFoundException("Refresh token missing");
        }
        TokenReissueResDto result = authService.reissue(refreshToken);
        ResponseCookie cookie = ResponseCookie.from(jwtProperties.getCookieConfig().getName(), result.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path(jwtProperties.getCookieConfig().getPath())
                .maxAge(Duration.ofMillis(jwtProperties.getRefreshTokenExpire()))
                .build();

        result.setRefreshToken(null);
        return responseService.resSuccessWithCookies(result, cookie);
    }

}
