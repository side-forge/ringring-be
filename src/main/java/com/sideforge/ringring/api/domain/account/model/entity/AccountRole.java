package com.sideforge.ringring.api.domain.account.model.entity;

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
    private String id;  // DB에 코드값 그대로 사용중이므로 컨버터 생략

    @Column(nullable = false, length = 20)
    private String name;

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<AccountRoleMapping> accountRoleMappings;
}
