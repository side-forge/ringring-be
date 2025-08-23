package com.sideforge.ringring.api.domain.auth.controller;

import com.sideforge.ringring.api.common.model.dto.ApiCommonResDto;
import com.sideforge.ringring.api.common.service.ResponseService;
import com.sideforge.ringring.api.domain.auth.model.dto.request.LoginReqDto;
import com.sideforge.ringring.api.domain.auth.model.dto.request.TokenReissueReqDto;
import com.sideforge.ringring.api.domain.auth.model.dto.response.LoginResDto;
import com.sideforge.ringring.api.domain.auth.model.dto.response.TokenReissueResDto;
import com.sideforge.ringring.api.domain.auth.security.jwt.JwtTokenProvider;
import com.sideforge.ringring.api.domain.auth.service.AuthService;
import com.sideforge.ringring.exception.dto.AccountNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final ResponseService responseService;
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

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

        return responseService.resSuccess(result);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiCommonResDto<Void>> logout(
            HttpServletRequest request
    ) {
        String token = jwtTokenProvider.resolveToken(request);
        String accountId = jwtTokenProvider.getAccountId(token);
        authService.logout(token, accountId);
        return responseService.resSuccess();
    }

    @PostMapping("/token/reissue")
    public ResponseEntity<ApiCommonResDto<TokenReissueResDto>> reissue(
            @Valid @RequestBody TokenReissueReqDto reqDto
    ) {
        TokenReissueResDto result = authService.reissue(reqDto.getRefreshToken());
        return responseService.resSuccess(result);
    }

}
