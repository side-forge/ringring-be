package com.sideforge.ringring.api.domain.account.model.enums;

import lombok.Getter;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// 계정 권한
@Getter
public enum AccountRoleType {
    ROLE_ADMIN("R0000", "관리자"),
    ROLE_MANAGER("R0100", "매니저"),
    ROLE_USER("R0200", "사용자");

    private static final Map<String, AccountRoleType> ACCOUNT_ROLE_TYPE_MAP =
            Collections.unmodifiableMap(Stream.of(values())
                    .collect(Collectors.toMap(AccountRoleType::getRoleId, Function.identity())));

    private final String roleId;
    private final String roleName;

    AccountRoleType(String roleId, String roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
    }

    public static AccountRoleType fromRoleId(String roleId) {
        return ACCOUNT_ROLE_TYPE_MAP.get(roleId);
    }
}
