package com.sideforge.ringring.domain.account.model.enums;

import lombok.Getter;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// 소셜 로그인 제공자
@Getter
public enum SocialProviderType {
    GOOGLE("google"),
    NAVER("naver");

    private static final Map<String, SocialProviderType> SOCIAL_PROVIDER_TYPE_MAP =
            Collections.unmodifiableMap(Stream.of(values())
                    .collect(Collectors.toMap(SocialProviderType::getValue, Function.identity())));

    private final String value;

    SocialProviderType(String value) {
        this.value = value;
    }

    public static SocialProviderType fromValue(String value) {
        return SOCIAL_PROVIDER_TYPE_MAP.get(value);
    }
}
