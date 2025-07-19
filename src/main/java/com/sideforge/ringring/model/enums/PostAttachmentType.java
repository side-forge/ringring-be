package com.sideforge.ringring.model.enums;

import lombok.Getter;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// 게시글 첨부파일 타입
@Getter
public enum PostAttachmentType {
    IMAGE("image"),
    AUDIO("audio"),
    VIDEO("video");

    private static final Map<String, PostAttachmentType> POST_ATTACHMENT_TYPE_MAP =
            Collections.unmodifiableMap(Stream.of(values())
                    .collect(Collectors.toMap(PostAttachmentType::getValue, Function.identity())));

    private final String value;

    PostAttachmentType(String value) {
        this.value = value;
    }

    public static PostAttachmentType fromValue(String value) {
        return POST_ATTACHMENT_TYPE_MAP.get(value);
    }
}
