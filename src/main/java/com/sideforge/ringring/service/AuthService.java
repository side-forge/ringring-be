package com.sideforge.ringring.service;

import com.sideforge.ringring.common.exception.dto.AccountNotFoundException;
import com.sideforge.ringring.common.exception.dto.AccountStatusException;
import com.sideforge.ringring.model.dto.res.auth.LoginResDto;
import com.sideforge.ringring.model.entity.Account;
import com.sideforge.ringring.model.entity.AccountBanHistory;
import com.sideforge.ringring.model.entity.redis.RefreshToken;
import com.sideforge.ringring.model.enums.ApiResponseCode;
import com.sideforge.ringring.repository.AccountBanHistoryRepository;
import com.sideforge.ringring.repository.AccountRepository;
import com.sideforge.ringring.repository.RefreshTokenRepository;
import com.sideforge.ringring.security.jwt.JwtTokenProvider;
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

    private static final int LOCK_TIME = 30;
    private static final int MAX_FAILED_COUNT = 5;

    private void handleLockedAccount(Account account) {
        LocalDateTime lockedAt = account.getIsLockedAt();

        // 데이터 정합성 오류: LOCKED인데 락 시간이 없음
        if (lockedAt == null) {
            throw new AccountStatusException("Your account is currently locked. Please contact support.");
        }
        // 계정 언락 시간 체크
        if (LocalDateTime.now().isBefore(lockedAt.plusMinutes(LOCK_TIME))) {
            throw new AccountStatusException("Your account is temporarily locked. Please try again later.");
        }

        // 잠금 기간 만료 → 계정 활성화
        account.unlock();
        accountRepository.save(account);
    }

    private void handleBannedAccount(Account account) {
        // 데이터 정합성 오류: BANNED인데 ban 이력 없음
        AccountBanHistory latestBan = accountBanHistoryRepository.findTopByBannedAccountOrderByCreatedAtDesc(account)
                .orElseThrow(() -> new AccountStatusException("Your account is currently banned. Please contact support."));

        // 영구 정지 or 아직 정지 기간 중
        if (latestBan.getEndsAt() == null || latestBan.getEndsAt().isAfter(LocalDateTime.now())) {
            throw new AccountStatusException("Your account is currently banned.");
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
            case WITHDRAWN -> throw new AccountStatusException("This account has been withdrawn.");
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
}
