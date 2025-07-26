package com.sideforge.ringring.controller;

import com.sideforge.ringring.model.dto.req.auth.LoginReqDto;
import com.sideforge.ringring.model.dto.res.ApiCommonResDto;
import com.sideforge.ringring.model.dto.res.auth.LoginResDto;
import com.sideforge.ringring.model.enums.ApiResponseCode;
import com.sideforge.ringring.security.jwt.JwtTokenProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<ApiCommonResDto<LoginResDto>> login(
            @Valid @RequestBody LoginReqDto reqDto
    ) {
        Authentication authInputToken = new UsernamePasswordAuthenticationToken(reqDto.getEmail(), reqDto.getPassword());

        Authentication authenticated = authenticationManager.authenticate(authInputToken);

        // 인증 성공 → 토큰 생성
        String accessToken = jwtTokenProvider.generateAccessToken(authenticated);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authenticated);

        return ResponseEntity
                .status(ApiResponseCode.SUCCESS.getHttpStatus())
                .body(ApiCommonResDto.<LoginResDto>builder()
                        .data(LoginResDto.builder()
                                .accessToken(accessToken)
                                .refreshToken(refreshToken)
                                .build())
                        .code(ApiResponseCode.SUCCESS.getCode())
                        .message(ApiResponseCode.SUCCESS.formatMessage())
                        .build()
                );
    }
}
