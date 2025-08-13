package com.sideforge.ringring.domain.post.model.enums;

import lombok.Getter;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// 게시판 타입
@Getter
public enum BoardType {
    NOTICE("notice");

    private static final Map<String, BoardType> BOARD_TYPE_MAP =
            Collections.unmodifiableMap(Stream.of(values())
                    .collect(Collectors.toMap(BoardType::getValue, Function.identity())));

    private final String value;

    BoardType(String value) {
        this.value = value;
    }

    public static BoardType fromValue(String value) {
        return BOARD_TYPE_MAP.get(value);
    }
}
