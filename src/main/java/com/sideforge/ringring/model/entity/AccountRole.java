package com.sideforge.ringring.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Builder
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

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<AccountRoleMapping> accountRoleMappings;
}
