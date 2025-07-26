package com.sideforge.ringring.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ApiResponseCode {

    // === [00] Success ===
    SUCCESS(HttpStatus.OK, "0000", "Success"),

    // === [10] Request Error ===
    REQ_INVALID_URL(HttpStatus.BAD_REQUEST, "1000", "Invalid URL"),
    REQ_INVALID_DATA(HttpStatus.BAD_REQUEST, "1001", "Invalid request data: {msg}");

    // === [20] Auth Error ===
    // === [30] DB Error ===
    // === [40] Server Error ===

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    public String formatMessage(Object... args) {
        return message != null && args.length > 0 ? message.formatted(args) : message;
    }
}
