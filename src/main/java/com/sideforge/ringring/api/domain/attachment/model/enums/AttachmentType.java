package com.sideforge.ringring.api.domain.attachment.model.enums;

import lombok.Getter;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// 첨부파일 타입
@Getter
public enum AttachmentType {
    IMAGE("image"),
    VIDEO("video"),
    AUDIO("audio"),
    DOC("doc"),
    ARCHIVE("archive"),
    OTHER("other");

    private static final Map<String, AttachmentType> ATTACHMENT_TYPE_MAP =
            Collections.unmodifiableMap(Stream.of(values())
                    .collect(Collectors.toMap(AttachmentType::getValue, Function.identity())));

    private final String value;

    AttachmentType(String value) {
        this.value = value;
    }

    public static AttachmentType fromValue(String value) {
        return ATTACHMENT_TYPE_MAP.get(value);
    }
}
