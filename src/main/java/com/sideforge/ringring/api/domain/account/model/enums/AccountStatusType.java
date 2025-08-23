package com.sideforge.ringring.api.domain.account.model.enums;

import lombok.Getter;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// 계정 상태
@Getter
public enum AccountStatusType {
    ACTIVE("active"),
    INACTIVE("inactive"),
    LOCKED("locked"),
    BANNED("banned"),
    WITHDRAWN("withdrawn");

    private static final Map<String, AccountStatusType> ACCOUNT_STATUS_TYPE_MAP =
            Collections.unmodifiableMap(Stream.of(values())
                    .collect(Collectors.toMap(AccountStatusType::getValue, Function.identity())));

    private final String value;

    AccountStatusType(String value) {
        this.value = value;
    }

    public static AccountStatusType fromValue(String value) {
        return ACCOUNT_STATUS_TYPE_MAP.get(value);
    }
}
