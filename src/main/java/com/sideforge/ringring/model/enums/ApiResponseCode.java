package com.sideforge.ringring.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ApiResponseCode {

    /** 성공 */
    SUCCESS(HttpStatus.OK, "0000", "Success"),

    /** 잘못된 요청 URL */
    REQ_INVALID_URL(HttpStatus.BAD_REQUEST, "1000", "Invalid request URL"),

    /** 요청 파라미터가 잘못된 경우 */
    REQ_INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "1001", "Invalid request parameter (header/query/path): %s"),

    /** 요청 본문이 잘못되었거나 유효성 검증에 실패한 경우 */
    REQ_INVALID_BODY(HttpStatus.BAD_REQUEST, "1002", "Invalid request body: %s");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    public String formatMessage(Object... args) {
        return message != null && args.length > 0 ? message.formatted(args) : message;
    }
}
