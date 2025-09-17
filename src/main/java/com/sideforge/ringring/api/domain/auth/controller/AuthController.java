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
import com.sideforge.ringring.exception.dto.InvalidTokenException;
import io.swagger.v3.oas.annotations.Operation;
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

    /**
     * 사용자 로그인
     * - 계정 상태 검증 및 토큰 발급
     */
    @PostMapping("/login")
    @Operation(summary = "로그인")
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

    /**
     * 사용자 로그아웃
     * - 토큰 블랙리스트 등록 및 쿠키에서 제거
     */
    @PostMapping("/logout")
    @Operation(summary = "로그아웃")
    public ResponseEntity<ApiCommonResDto<Void>> logout(
            HttpServletRequest request
    ) {
        String accessToken = jwtTokenProvider.resolveToken(request);
        String accountId = jwtTokenProvider.getAccountId(accessToken);
        authService.logout(accessToken, accountId);
        ResponseCookie cookie = ResponseCookie.from(jwtProperties.getCookieConfig().getName(), "")
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path(jwtProperties.getCookieConfig().getPath())
                .maxAge(Duration.ofMillis(0))
                .build();

        return responseService.resSuccessWithCookies(cookie);
    }

    /**
     * 토큰 재발급
     * - 토큰 RT 및 신규 발급
     */
    @PostMapping("/token/reissue")
    @Operation(summary = "토큰 재발급")
    public ResponseEntity<ApiCommonResDto<TokenReissueResDto>> reissue(
            @CookieValue(name = "#{@jwtProperties.cookieConfig.name}", required = false) String refreshToken
    ) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new InvalidTokenException("Refresh token missing");
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
