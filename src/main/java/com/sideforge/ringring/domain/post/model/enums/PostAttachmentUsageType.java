package com.sideforge.ringring.domain.post.model.enums;

import lombok.Getter;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// 첨부파일 용도 타입
@Getter
public enum PostAttachmentUsageType {
    FILE("file"),
    CONTENT("content"),
    THUMBNAIL("thumbnail");

    private static final Map<String, PostAttachmentUsageType> POST_ATTACHMENT_USAGE_TYPE_MAP =
            Collections.unmodifiableMap(Stream.of(values())
                    .collect(Collectors.toMap(PostAttachmentUsageType::getValue, Function.identity())));

    private final String value;

    PostAttachmentUsageType(String value) {
        this.value = value;
    }

    public static PostAttachmentUsageType fromValue(String value) {
        return POST_ATTACHMENT_USAGE_TYPE_MAP.get(value);
    }
}
