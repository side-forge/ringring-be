package com.sideforge.ringring.domain.account.model.entity;

import com.sideforge.ringring.domain.account.model.entity.id.AccountRoleMappingId;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_account_role_mapping")
public class AccountRoleMapping {

    @Id
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

    public static AccountRoleMapping of(Account account, AccountRole role) {
        return new AccountRoleMapping(new AccountRoleMappingId(), account, role);
    }
}
