package com.sideforge.ringring.api.domain.account.repository;

import com.sideforge.ringring.api.domain.account.model.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    Optional<Account> findByEmail(String username);

    boolean existsByEmail(String email);
}
