package com.sideforge.ringring.api.domain.account.repository;

import com.sideforge.ringring.api.domain.account.model.entity.Account;
import com.sideforge.ringring.api.domain.account.model.entity.AccountBanHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountBanHistoryRepository extends JpaRepository<AccountBanHistory, Long> {
    Optional<AccountBanHistory> findTopByBannedAccountOrderByCreatedAtDesc(Account account);
}
