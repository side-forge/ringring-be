package com.sideforge.ringring.repository;

import com.sideforge.ringring.model.entity.Account;
import com.sideforge.ringring.model.entity.AccountBanHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountBanHistoryRepository extends JpaRepository<AccountBanHistory, Long> {
    Optional<AccountBanHistory> findTopByBannedAccountOrderByCreatedAtDesc(Account account);
}
