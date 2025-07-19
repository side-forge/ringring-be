package com.sideforge.ringring.model.entity;

import com.sideforge.ringring.model.entity.id.AccountRoleMappingId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
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
}
