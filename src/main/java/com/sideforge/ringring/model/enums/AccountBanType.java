package com.sideforge.ringring.model.enums;

import lombok.Getter;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// 계정 벤 종류
@Getter
public enum AccountBanType {
    CAUTION_BAN("caution_ban"),
    TEMPORARY_BAN("temporary_ban"),
    PERMANENT_BAN("permanent_ban");

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
