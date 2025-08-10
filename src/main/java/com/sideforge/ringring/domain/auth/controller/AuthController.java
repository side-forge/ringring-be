package com.sideforge.ringring.domain.auth.controller;

import com.sideforge.ringring.common.exception.dto.AccountNotFoundException;
import com.sideforge.ringring.domain.auth.model.dto.request.LoginReqDto;
import com.sideforge.ringring.common.model.dto.ApiCommonResDto;
import com.sideforge.ringring.domain.auth.model.dto.response.LoginResDto;
import com.sideforge.ringring.common.model.enums.ApiResponseCode;
import com.sideforge.ringring.domain.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

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

        return ResponseEntity
                .status(ApiResponseCode.SUCCESS.getHttpStatus())
                .body(ApiCommonResDto.<LoginResDto>builder()
                        .data(result)
                        .code(ApiResponseCode.SUCCESS.getCode())
                        .message(ApiResponseCode.SUCCESS.formatMessage())
                        .build()
                );
    }
}
