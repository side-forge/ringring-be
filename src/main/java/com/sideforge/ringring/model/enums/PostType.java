package com.sideforge.ringring.model.enums;

import lombok.Getter;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// 게시글 타입
@Getter
public enum PostType {
    REPORT("report");

    private static final Map<String, PostType> POST_TYPE_MAP =
            Collections.unmodifiableMap(Stream.of(values())
                    .collect(Collectors.toMap(PostType::getValue, Function.identity())));

    private final String value;

    PostType(String value) {
        this.value = value;
    }

    public static PostType fromValue(String value) {
        return POST_TYPE_MAP.get(value);
    }
}
