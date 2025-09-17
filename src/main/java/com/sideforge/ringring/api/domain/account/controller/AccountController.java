package com.sideforge.ringring.api.domain.account.controller;

import com.sideforge.ringring.api.common.model.dto.ApiCommonResDto;
import com.sideforge.ringring.api.common.service.ResponseService;
import com.sideforge.ringring.api.domain.account.model.dto.request.EmailVerificationCheckReqDto;
import com.sideforge.ringring.api.domain.account.model.dto.request.EmailVerificationReqDto;
import com.sideforge.ringring.api.domain.account.model.dto.request.UserAvailabilityReqDto;
import com.sideforge.ringring.api.domain.account.model.dto.response.UserAvailabilityResDto;
import com.sideforge.ringring.api.domain.account.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
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

    /**
     * 사용자 식별자(EMAIL/PHONE) 가용성 확인
     * - PII가 URL/로그에 남지 않도록 POST 사용
     */
    @PostMapping("/identifier/availability")
    @Operation(summary = "사용자 식별자 가용성 체크")
    public ResponseEntity<ApiCommonResDto<UserAvailabilityResDto>> getIdentifierAvailability(
            @Valid @RequestBody UserAvailabilityReqDto reqDto
    ) {
        UserAvailabilityResDto result = accountService.checkIdentifier(reqDto);
        return responseService.resSuccess(result);
    }

    /**
     * 사용자 이메일 인증메일 발송
     *  - 인증코드 레디스 저장 및 인증메일 비동기 전송
     */
    @PostMapping("/email/verifications")
    @Operation(summary = "이메일 인증메일 발송")
    public ResponseEntity<ApiCommonResDto<Void>> emailVerification(
            @Valid @RequestBody EmailVerificationReqDto reqDto
    ) {
        accountService.sendAuthCodeEmail(reqDto);
        return responseService.resSuccess();
    }

    /**
     * 사용자 이메일 인증코드 확인
     */
    @PostMapping("/email/verifications/confirm")
    @Operation(summary = "이메일 인증코드 검증")
    public ResponseEntity<ApiCommonResDto<Void>> confirmEmailVerification(
            @Valid @RequestBody EmailVerificationCheckReqDto reqDto
    ) {
        accountService.verifyAuthCodeEmail(reqDto);
        return responseService.resSuccess();
    }
}
