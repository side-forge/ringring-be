package com.sideforge.ringring.domain.account.controller;

import com.sideforge.ringring.domain.account.model.dto.request.EmailVerificationReqDto;
import com.sideforge.ringring.common.model.dto.ApiCommonResDto;
import com.sideforge.ringring.common.model.enums.ApiResponseCode;
import com.sideforge.ringring.domain.account.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/verify-email/request")
    public ResponseEntity<ApiCommonResDto<?>> sendSignupVerificationEmail(
            @Valid @RequestBody EmailVerificationReqDto reqDto
    ) {
        accountService.sendSignupVerificationEmail(reqDto);
        return ResponseEntity
                .status(ApiResponseCode.SUCCESS.getHttpStatus())
                .body(ApiCommonResDto.<Void>builder()
                        .code(ApiResponseCode.SUCCESS.getCode())
                        .message(ApiResponseCode.SUCCESS.formatMessage())
                        .build()
                );
    }
}
