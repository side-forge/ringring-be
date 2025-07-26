package com.sideforge.ringring.common.exception;

import com.sideforge.ringring.model.dto.res.ApiCommonResDto;
import com.sideforge.ringring.model.enums.ApiResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    /** Java 객체로 바인딩한 후, @Valid나 @Validated 유효성 검사에 실패했을 때 발생. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiCommonResDto<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ApiResponseCode apiResponseCode = ApiResponseCode.REQ_INVALID_PARAMETER;

        // 메시지 간략화
        String simplifiedMessage = e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> String.format("'%s' %s", fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.joining(", "));
        log.error("Invalid request data. Simplified Message: {}", simplifiedMessage);

        return ResponseEntity
                .status(apiResponseCode.getHttpStatus())
                .body(ApiCommonResDto.<Void>builder()
                        .code(apiResponseCode.getCode())
                        .message(apiResponseCode.formatMessage(simplifiedMessage))
                        .build()
                );
    }

    /** JSON 자체가 깨졌거나, JSON 구조와 DTO 매핑이 불가능할 때 발생. */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiCommonResDto<Void>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        ApiResponseCode apiResponseCode = ApiResponseCode.REQ_INVALID_BODY;

        // 메시지 간략화
        String simplifiedMessage = e.getMostSpecificCause().getMessage();
        log.error("Unreadable HTTP message. Simplified Message: {}", simplifiedMessage);

        return ResponseEntity
                .status(apiResponseCode.getHttpStatus())
                .body(ApiCommonResDto.<Void>builder()
                        .code(apiResponseCode.getCode())
                        .message(apiResponseCode.formatMessage(simplifiedMessage))
                        .build()
                );
    }
}
