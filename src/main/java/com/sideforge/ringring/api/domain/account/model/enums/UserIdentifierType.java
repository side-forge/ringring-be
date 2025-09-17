package com.sideforge.ringring.api.domain.account.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.sideforge.ringring.exception.dto.InvalidValueException;
import lombok.Getter;

@Getter
public enum UserIdentifierType {
    EMAIL("email") {
        @Override
        public boolean isValid(String raw) {
            if (raw == null) return false;
            String trimmed = raw.trim();
            int at = trimmed.indexOf('@');
            if (at <= 0) return false;
            int dot = trimmed.lastIndexOf('.');
            if (dot < at + 2) return false;
            return dot < trimmed.length() - 2;
        }

        @Override
        public String normalize(String raw) {
            return raw == null ? null : raw.trim();
        }

        @Override
        public String mask(String normalized) {
            int at = normalized.indexOf('@');
            if (at <= 1) return "***";
            String local = normalized.substring(0, at);
            String domain = normalized.substring(at);
            return local.charAt(0) + "***" + domain;
        }
    },

    PHONE("phone") {
        @Override
        public boolean isValid(String raw) {
            if (raw == null) return false;
            String digits = raw.replaceAll("\\D", "");
            return digits.matches("^010\\d{8}$");
        }

        @Override
        public String normalize(String raw) {
            if (!isValid(raw)) return null;
            String digits = raw.replaceAll("\\D", "");
            return "+82" + digits.substring(1); // 010 → +8210...
        }

        @Override
        public String mask(String normalized) {
            String digits = normalized.replaceAll("\\D", "");
            if (digits.length() != 11 && !digits.startsWith("+82")) {
                return "***********";
            }
            // 원본이 010xxxxxxxx면 뒤 4자리만 노출
            if (digits.length() == 11) {
                return "*******" + digits.substring(7);
            }
            // 정규화된 +8210xxxxxxx면 뒤 4자리만 노출
            return "********" + digits.substring(digits.length() - 4);
        }
    };

    private final String code;

    UserIdentifierType(String code) {
        this.code = code;
    }

    @JsonValue
    public String getCode() { return code; }

    @JsonCreator
    public static UserIdentifierType from(String value) {
        if (value == null) return null;
        for (UserIdentifierType type : values()) {
            if (type.code.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new InvalidValueException("Invalid UserIdentifierType: " + value);
    }

    // 입력값이 형식적으로 올바른지 검증
    public abstract boolean isValid(String raw);

    // DB/조회에 사용될 정규화된 값 생성
    public abstract String normalize(String raw);

    // 응답/로그에 민감정보 노출 방지 마스킹 값
    public abstract String mask(String normalized);
}
