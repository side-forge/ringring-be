package com.sideforge.ringring.api.domain.account.controller;

import com.sideforge.ringring.api.common.service.ResponseService;
import com.sideforge.ringring.api.domain.account.model.dto.request.EmailVerificationReqDto;
import com.sideforge.ringring.api.common.model.dto.ApiCommonResDto;
import com.sideforge.ringring.api.common.model.enums.ApiResponseCode;
import com.sideforge.ringring.api.domain.account.service.AccountService;
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
    private final ResponseService responseService;
    private final AccountService accountService;

    @PostMapping("/verify-email/request")
    public ResponseEntity<ApiCommonResDto<Void>> sendSignupVerificationEmail(
            @Valid @RequestBody EmailVerificationReqDto reqDto
    ) {
        accountService.sendSignupVerificationEmail(reqDto);
        return responseService.resSuccess();
    }
}
