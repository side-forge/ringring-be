package com.sideforge.ringring.service;

import com.sideforge.ringring.common.exception.dto.AccountStatusException;
import com.sideforge.ringring.model.entity.Account;
import com.sideforge.ringring.model.entity.AccountBanHistory;
import com.sideforge.ringring.repository.AccountBanHistoryRepository;
import com.sideforge.ringring.repository.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final AccountRepository accountRepository;
    private final AccountBanHistoryRepository accountBanHistoryRepository;

    private void handleLockedAccount(Account account) {
        LocalDateTime lockedAt = account.getIsLockedAt();

        // 데이터 정합성 오류: LOCKED인데 락 시간이 없음
        if (lockedAt == null) {
            throw new AccountStatusException("Your account is currently locked. Please contact support.");
        }
        // 계정 언락 시간 체크
        if (LocalDateTime.now().isBefore(lockedAt.plusMinutes(30))) {
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
    public void validateAccountStatus(Account account) {
        switch (account.getStatus()) {
            case WITHDRAWN -> throw new AccountStatusException("This account has been withdrawn.");
            case LOCKED -> handleLockedAccount(account);
            case BANNED -> handleBannedAccount(account);
        }
    }
}
