package com.sideforge.ringring.domain.account.model.enums;

import lombok.Getter;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// 계정 벤 종류
@Getter
public enum AccountBanType {
    CAUTION("caution"),
    TEMPORARY("temporary"),
    PERMANENT("permanent");

    private static final Map<String, AccountBanType> ACCOUNT_BAN_TYPE_MAP =
            Collections.unmodifiableMap(Stream.of(values())
                    .collect(Collectors.toMap(AccountBanType::getValue, Function.identity())));

    private final String value;

    AccountBanType(String value) {
        this.value = value;
    }

    public static AccountBanType fromValue(String value) {
        return ACCOUNT_BAN_TYPE_MAP.get(value);
    }
}
