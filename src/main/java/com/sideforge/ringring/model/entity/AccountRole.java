package com.sideforge.ringring.model.entity;

import com.sideforge.ringring.model.enums.AccountRoleType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_account_role")
public class AccountRole {

    @Id
    @Column(columnDefinition = "CHAR(5)", nullable = false)
    private String id;

    @Column(nullable = false, length = 20)
    private String name;

    public static AccountRole from(AccountRoleType type) {
        return new AccountRole(type.getRoleId(), type.getRoleName());
    }
}
