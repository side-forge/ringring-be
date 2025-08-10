package com.sideforge.ringring.domain.account.repository;

import com.sideforge.ringring.domain.account.model.entity.AccountRoleMapping;
import com.sideforge.ringring.domain.account.model.entity.id.AccountRoleMappingId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRoleMappingRepository extends JpaRepository<AccountRoleMapping, AccountRoleMappingId> {
}
