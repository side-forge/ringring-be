package com.sideforge.ringring.domain.report.model.enums;

import lombok.Getter;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// 신고 상태
@Getter
public enum ReportStatusType {
    PENDING("pending"),
    SPAM("spam"),
    RELEASED("released");

    private static final Map<String, ReportStatusType> REPORT_STATUS_TYPE_MAP =
            Collections.unmodifiableMap(Stream.of(values())
                    .collect(Collectors.toMap(ReportStatusType::getValue, Function.identity())));

    private final String value;

    ReportStatusType(String value) {
        this.value = value;
    }

    public static ReportStatusType fromValue(String value) {
        return REPORT_STATUS_TYPE_MAP.get(value);
    }
}
