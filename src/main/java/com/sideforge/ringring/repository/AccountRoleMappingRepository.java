package com.sideforge.ringring.repository;

import com.sideforge.ringring.model.entity.AccountRoleMapping;
import com.sideforge.ringring.model.entity.id.AccountRoleMappingId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRoleMappingRepository extends JpaRepository<AccountRoleMapping, AccountRoleMappingId> {
}
