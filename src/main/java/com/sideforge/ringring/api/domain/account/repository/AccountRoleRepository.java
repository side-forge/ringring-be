package com.sideforge.ringring.api.domain.account.repository;

import com.sideforge.ringring.api.domain.account.model.entity.AccountRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRoleRepository extends JpaRepository<AccountRole, String> {
}
