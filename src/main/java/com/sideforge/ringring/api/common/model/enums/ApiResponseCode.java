package com.sideforge.ringring.api.common.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ApiResponseCode {

    // 200 - SUCCESS
    /** 200 - 요청 성공 */
    SUCCESS(HttpStatus.OK, "2000", "Success"),

    // 400 - BAD REQUEST
    /** 400 - 잘못된 URL */
    INVALID_URL(HttpStatus.BAD_REQUEST, "4000", "Invalid request URL"),
    /** 400 - 잘못된 파라미터 */
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "4001", "Invalid request parameter: %s"),
    /** 400 - 잘못된 본문 형식 */
    INVALID_BODY_TYPE(HttpStatus.BAD_REQUEST, "4002", "Invalid request body type: %s"),
    /** 400 - 잘못된 요청 컨텐츠 */
    INVALID_CONTENTS(HttpStatus.BAD_REQUEST, "4003", "Invalid content: %s"),
    /** 400 - 잘못된 값 사용 */
    INVALID_VALUE(HttpStatus.BAD_REQUEST, "4004", "Invalid value: %s"),

    // 401 - UNAUTHORIZED
    /** 401 - 토큰 누락 또는 만료 */
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "4010", "Invalid or expired token"),
    /** 401 - 인증 정보 누락 */
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "4011", "Missing authentication credentials"),
    /** 401 - 잘못된 email, 존재하지 않는 사용자 */
    ACCOUNT_NOT_FOUND(HttpStatus.UNAUTHORIZED, "4012", "Invalid email or password"),

    // 403 - FORBIDDEN
    /** 403 - 권한 없음 */
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "4030", "Access denied: %s"),

    // 404 - NOT FOUND
    /** 404 - 리소스를 찾을 수 없음 */
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "4040", "Resource not found: %s"),

    // 409 - CONFLICT
    /** 409 - 중복된 리소스 존재 */
    DUPLICATE_RESOURCE(HttpStatus.CONFLICT, "4090", "Resource already exists: %s"),

    // 429 - TOO MANY REQUESTS
    /** 429 - 요청 빈도 초과 */
    TOO_MANY_REQUESTS(HttpStatus.TOO_MANY_REQUESTS, "4290", "Too many requests. Try again later."),

    // 500 - SERVER ERROR
    /** 500 - 내부 서버 오류 */
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "5000", "Internal server error"),
    /** 500 - 데이터 타입 변환 오류 */
    DATA_TYPE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "5001", "Invalid data type"),
    /** 500 - JSON 파싱 실패 */
    JSON_PARSE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "5002", "Failed to parse JSON"),
    /** 500 - 리소스 로딩 실패 (파일, 템플릿 등) */
    RESOURCE_LOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "5003", "Failed to load resource: %s"),

    // 502 - BAD GATEWAY
    /** 502 - 외부 시스템 연동 실패 */
    EXTERNAL_SERVICE_FAILED(HttpStatus.BAD_GATEWAY, "5020", "External service error: %s"),
    /** 502 - 외부 시스템 응답 오류 (4xx/5xx 등) */
    EXTERNAL_SERVICE_INVALID_RESPONSE(HttpStatus.BAD_GATEWAY, "5021", "Unacceptable response from external service"),

    // 503 - SERVICE UNAVAILABLE
    /** 503 - 서버 과부하 또는 점검 중 */
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "5030", "Service temporarily unavailable"),

    // 504 - GATEWAY TIMEOUT
    /** 504 - 외부 시스템 타임아웃 */
    EXTERNAL_TIMEOUT(HttpStatus.GATEWAY_TIMEOUT, "5040", "Timeout from external system"),

    // 510 - DB 관련
    /** 510 - DB 연결 실패 */
    DB_CONNECTION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "5100", "Failed to connect to database"),
    /** 510 - DB 제약 조건 위반 */
    DB_CONSTRAINT_VIOLATION(HttpStatus.INTERNAL_SERVER_ERROR, "5101", "Database constraint violation"),
    /** 510 - DB 시스템 오류 */
    DB_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "5102", "Database error: %s");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    public String formatMessage(Object... args) {
        return message != null && args.length > 0 ? message.formatted(args) : message;
    }
}
