package com.sideforge.ringring.api.domain.auth.service;

import com.sideforge.ringring.api.common.model.enums.ApiResponseCode;
import com.sideforge.ringring.api.domain.account.model.entity.Account;
import com.sideforge.ringring.api.domain.account.model.entity.AccountBanHistory;
import com.sideforge.ringring.api.domain.account.repository.AccountBanHistoryRepository;
import com.sideforge.ringring.api.domain.account.repository.AccountRepository;
import com.sideforge.ringring.api.domain.auth.model.dto.response.LoginResDto;
import com.sideforge.ringring.api.domain.auth.model.dto.response.TokenReissueResDto;
import com.sideforge.ringring.api.domain.auth.model.entity.BlacklistedAccessToken;
import com.sideforge.ringring.api.domain.auth.model.entity.RefreshToken;
import com.sideforge.ringring.api.domain.auth.model.principal.CustomUserPrincipal;
import com.sideforge.ringring.api.domain.auth.repository.BlacklistedAccessTokenRepository;
import com.sideforge.ringring.api.domain.auth.repository.RefreshTokenRepository;
import com.sideforge.ringring.api.domain.auth.security.jwt.JwtTokenProvider;
import com.sideforge.ringring.exception.dto.AccountNotFoundException;
import com.sideforge.ringring.exception.dto.AccountStatusException;
import com.sideforge.ringring.exception.dto.InvalidTokenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final AccountRepository accountRepository;
    private final AccountBanHistoryRepository accountBanHistoryRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BlacklistedAccessTokenRepository blacklistedAccessTokenRepository;

    private static final int LOCK_TIME = 30;
    private static final int MAX_FAILED_COUNT = 5;

    private void handleLockedAccount(Account account) {
        LocalDateTime lockedAt = account.getIsLockedAt();

        // 데이터 정합성 오류: LOCKED인데 락 시간이 없음
        if (lockedAt == null) {
            throw new AccountStatusException("Account is currently locked. Please contact support.");
        }
        // 계정 언락 시간 체크
        if (LocalDateTime.now().isBefore(lockedAt.plusMinutes(LOCK_TIME))) {
            throw new AccountStatusException("Account is temporarily locked. Please try again later.");
        }

        // 잠금 기간 만료 → 계정 활성화
        account.unlock();
        accountRepository.save(account);
    }

    private void handleBannedAccount(Account account) {
        // 데이터 정합성 오류: BANNED인데 ban 이력 없음
        AccountBanHistory latestBan = accountBanHistoryRepository.findTopByBannedAccountOrderByCreatedAtDesc(account)
                .orElseThrow(() -> new AccountStatusException("Account is currently banned. Please contact support."));

        // 영구 정지 or 아직 정지 기간 중
        if (latestBan.getEndsAt() == null || latestBan.getEndsAt().isAfter(LocalDateTime.now())) {
            throw new AccountStatusException("Account is currently banned.");
        }

        // 제재 기간 만료 → 계정 활성화
        account.unban();
        accountRepository.save(account);
    }

    @Transactional
    public void handleLoginFailure(String email) {
        accountRepository.findByEmail(email).ifPresent(account -> {
            account.increaseFailedLoginCount(MAX_FAILED_COUNT);
            accountRepository.save(account);
        });
    }

    @Transactional
    public LoginResDto login(String email, String password) throws BadCredentialsException, AccountNotFoundException {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new AccountNotFoundException(ApiResponseCode.ACCOUNT_NOT_FOUND.getMessage()));

        // account 상태 검증
        switch (account.getStatus()) {
            case WITHDRAWN -> throw new AccountStatusException("Account has been withdrawn.");
            case LOCKED -> handleLockedAccount(account);
            case BANNED -> handleBannedAccount(account);
        }

        // account 인증 요청
        Authentication authInputToken = new UsernamePasswordAuthenticationToken(email, password);
        Authentication authenticated = authenticationManager.authenticate(authInputToken);

        // 토큰 발급 및 저장
        String accessToken = jwtTokenProvider.generateAccessToken(authenticated);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authenticated);
        refreshTokenRepository.save(RefreshToken.builder()
                .accountId(account.getId())
                .token(refreshToken)
                .build());

        // 로그인 성공 → 실패 카운트 초기화
        account.resetFailedLoginCount();
        account.updateLastLoginAt();
        accountRepository.save(account);

        return LoginResDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public void logout(String token, String accountId) {
        refreshTokenRepository.deleteById(accountId);
        blacklistedAccessTokenRepository.save(BlacklistedAccessToken.builder()
                .token(token)
                .build());
    }

    @Transactional
    public TokenReissueResDto reissue(String refreshToken) {
        String accountId = jwtTokenProvider.getAccountId(refreshToken);
        RefreshToken saved = refreshTokenRepository.findById(accountId)
                .orElseThrow(() -> new InvalidTokenException("Refresh token not found."));

        // 저장된 토큰과 일치하지 않을 경우 폐기 (토큰 유출 방지)
        if (!saved.getToken().equals(refreshToken)) {
            refreshTokenRepository.deleteById(accountId);
            throw new InvalidTokenException("Refresh token reuse detected.");
        }

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found."));

        CustomUserPrincipal principal = CustomUserPrincipal.from(account);
        Authentication authenticated = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());

        // 토큰 발급 및 저장
        String newAccessToken = jwtTokenProvider.generateAccessToken(authenticated);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(authenticated);
        refreshTokenRepository.save(RefreshToken.builder()
                .accountId(account.getId())
                .token(refreshToken)
                .build());

        saved.reissue(newRefreshToken);
        refreshTokenRepository.save(saved);

        return TokenReissueResDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }
}
