package com.sideforge.ringring.api.domain.account.model.entity;

import com.sideforge.ringring.api.domain.account.model.entity.id.AccountRoleMappingId;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_account_role_mapping")
public class AccountRoleMapping {
    @EmbeddedId
    private AccountRoleMappingId id;

    @MapsId("accountId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    @ToString.Exclude
    private Account account;

    @MapsId("roleId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    @ToString.Exclude
    private AccountRole role;

    private AccountRoleMapping(Account account, AccountRole role) {
        this.account = Objects.requireNonNull(account);
        this.role = Objects.requireNonNull(role);
        this.id = new AccountRoleMappingId(account.getId(), role.getId());
    }

    public static AccountRoleMapping of(Account account, AccountRole role) {
        return new AccountRoleMapping(account, role);
    }
}
