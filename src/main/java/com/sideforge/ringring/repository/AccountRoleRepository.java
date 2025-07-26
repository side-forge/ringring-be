package com.sideforge.ringring.repository;

import com.sideforge.ringring.model.entity.AccountRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRoleRepository extends JpaRepository<AccountRole, String> {
}
