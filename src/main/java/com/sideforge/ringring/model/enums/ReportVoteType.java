package com.sideforge.ringring.model.enums;

import lombok.Getter;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// 투표 유형
@Getter
public enum ReportVoteType {
    UP("up"),
    DOWN("down");

    private static final Map<String, ReportVoteType> REPORT_VOTE_TYPE_MAP =
            Collections.unmodifiableMap(Stream.of(values())
                    .collect(Collectors.toMap(ReportVoteType::getValue, Function.identity())));

    private final String value;

    ReportVoteType(String value) {
        this.value = value;
    }

    public static ReportVoteType fromValue(String value) {
        return REPORT_VOTE_TYPE_MAP.get(value);
    }
}
