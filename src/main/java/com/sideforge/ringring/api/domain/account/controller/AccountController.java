package com.sideforge.ringring.api.domain.account.controller;

import com.sideforge.ringring.api.common.model.dto.ApiCommonResDto;
import com.sideforge.ringring.api.common.service.ResponseService;
import com.sideforge.ringring.api.domain.account.model.dto.request.EmailVerificationReqDto;
import com.sideforge.ringring.api.domain.account.model.dto.response.EmailDupCheckResDto;
import com.sideforge.ringring.api.domain.account.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class AccountController {
    private final ResponseService responseService;
    private final AccountService accountService;

    @GetMapping("/check-email")
    public ResponseEntity<ApiCommonResDto<EmailDupCheckResDto>> checkDuplicateEmail(
            @RequestParam("email") String email
    ) {
        boolean isDuplicate = accountService.isEmailDuplicated(email);
        EmailDupCheckResDto result = EmailDupCheckResDto.builder()
                .isDuplicate(isDuplicate)
                .build();
        return responseService.resSuccess(result);
    }

    @PostMapping("/verify-email/request")
    public ResponseEntity<ApiCommonResDto<Void>> sendSignupVerificationEmail(
            @Valid @RequestBody EmailVerificationReqDto reqDto
    ) {
        accountService.sendSignupVerificationEmail(reqDto);
        return responseService.resSuccess();
    }
}
