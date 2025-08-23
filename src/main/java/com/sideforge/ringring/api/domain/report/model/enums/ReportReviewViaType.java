package com.sideforge.ringring.api.domain.report.model.enums;

import lombok.Getter;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// 신고 판정 방식
@Getter
public enum ReportReviewViaType {
    PENDING("pending"),
    AUTO("auto"),
    MANUAL("menual");

    private static final Map<String, ReportReviewViaType> REPORT_REVIEW_VIA_TYPE_MAP =
            Collections.unmodifiableMap(Stream.of(values())
                    .collect(Collectors.toMap(ReportReviewViaType::getValue, Function.identity())));

    private final String value;

    ReportReviewViaType(String value) {
        this.value = value;
    }

    public static ReportReviewViaType fromValue(String value) {
        return REPORT_REVIEW_VIA_TYPE_MAP.get(value);
    }
}
