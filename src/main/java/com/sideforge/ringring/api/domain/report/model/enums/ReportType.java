package com.sideforge.ringring.api.domain.report.model.enums;

import lombok.Getter;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// 신고 유형
@Getter
public enum ReportType {
    LOAN("loan", "대출", "불법 대출 권유, 고금리 유도"),
    TELEMARKETING("telemarketing", "텔레마케팅", "상품/서비스를 광고하려는 전화 마케팅"),
    GAMBLING("gambling", "불법 도박", "스포츠토토, 슬롯머신 등 불법 도박 사이트 유도"),
    ADULT("adult", "성인/유흥", "유흥업소, 성인 채팅, 성매매 알선 등"),
    INSURANCE("insurance", "보험 광고", "보험 상담, 가입 권유 등 과도한 영업"),
    SURVEY("survey", "여론조사", "설문조사 명목으로 개인 정보 수집"),
    INVESTMENT("investment", "투자 사기", "고수익 보장형 투자 권유 (코인, 펀드 등)"),
    IMPERSONATION("impersonation", "기관 사칭", "경찰, 금융기관, 정부 등으로 사칭해 협박 및 개인정보 요구"),
    ETC("etc", "기타", "기타 사유");

    private static final Map<String, ReportType> REPORT_TYPE_MAP =
            Collections.unmodifiableMap(Stream.of(values())
                    .collect(Collectors.toMap(ReportType::getValue, Function.identity())));

    private final String value;
    private final String label;
    private final String description;

    ReportType(String value, String label, String description) {
        this.value = value;
        this.label = label;
        this.description = description;
    }

    public static ReportType fromValue(String value) {
        return REPORT_TYPE_MAP.get(value);
    }
}
