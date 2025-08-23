package com.sideforge.ringring.exception;

import com.sideforge.ringring.exception.dto.AccountNotFoundException;
import com.sideforge.ringring.exception.dto.AccountStatusException;
import com.sideforge.ringring.exception.dto.InvalidRequestContentsException;
import com.sideforge.ringring.exception.dto.ResourceLoadException;
import com.sideforge.ringring.api.common.model.dto.ApiCommonResDto;
import com.sideforge.ringring.api.common.model.enums.ApiResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    //    +------------------------------------------------------------------+
    //    |                        4xx Client Errors                         |
    //    +------------------------------------------------------------------+
    /** Java 객체로 바인딩한 후, @Valid나 @Validated 유효성 검사에 실패했을 때 발생. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiCommonResDto<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ApiResponseCode apiResponseCode = ApiResponseCode.INVALID_PARAMETER;
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
        ApiResponseCode apiResponseCode = ApiResponseCode.INVALID_BODY_TYPE;
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

    /** 잘못된 요청 데이터 발생 */
    @ExceptionHandler(InvalidRequestContentsException.class)
    public ResponseEntity<ApiCommonResDto<Void>> handleInvalidRequestContentsException(InvalidRequestContentsException e) {
        ApiResponseCode apiResponseCode = ApiResponseCode.INVALID_CONTENTS;
        log.error("Invalid Request Data. Exception Message: {}",e.getMessage());
        return ResponseEntity
                .status(apiResponseCode.getHttpStatus())
                .body(ApiCommonResDto.<Void>builder()
                        .code(apiResponseCode.getCode())
                        .message(apiResponseCode.formatMessage(e.getMessage()))
                        .build()
                );
    }

    /** 계정 상태가 활성화/비활성화 상태가 아닌데 로그인 시도 시 발생 */
    @ExceptionHandler(AccountStatusException.class)
    public ResponseEntity<ApiCommonResDto<Void>> handleAccountStatusException(AccountStatusException e) {
        ApiResponseCode apiResponseCode = ApiResponseCode.ACCESS_DENIED;
        log.error("Account status invalid for login. Message: {}", e.getMessage());
        return ResponseEntity
                .status(apiResponseCode.getHttpStatus())
                .body(ApiCommonResDto.<Void>builder()
                        .code(apiResponseCode.getCode())
                        .message(apiResponseCode.formatMessage(e.getMessage()))
                        .build()
                );
    }

    /** 계정을 찾을 수 없을 때 발생 */
    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ApiCommonResDto<Void>> handleAccountNotFoundException(AccountNotFoundException e) {
        ApiResponseCode apiResponseCode = ApiResponseCode.ACCOUNT_NOT_FOUND;
        log.error("Account not found or invalid credentials. Message: {}", e.getMessage());
        return ResponseEntity
                .status(apiResponseCode.getHttpStatus())
                .body(ApiCommonResDto.<Void>builder()
                        .code(apiResponseCode.getCode())
                        .message(apiResponseCode.getMessage())
                        .build()
                );
    }


    //    +------------------------------------------------------------------+
    //    |                        5xx Server Errors                         |
    //    +------------------------------------------------------------------+
    /** 인증 과정에서 일반적인 오류가 발생했을 때 (ex: UserDetailsService 내부 예외) */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiCommonResDto<Void>> handleAuthenticationException(AuthenticationException e) {
        return ResponseEntity
                .status(ApiResponseCode.UNAUTHORIZED.getHttpStatus())
                .body(ApiCommonResDto.<Void>builder()
                        .code(ApiResponseCode.UNAUTHORIZED.getCode())
                        .message("Authentication failed: " + e.getMessage())
                        .build()
                );
    }

    /** 리소스 로딩 실패 */
    @ExceptionHandler(ResourceLoadException.class)
    public ResponseEntity<ApiCommonResDto<Void>> handleResourceLoadException(ResourceLoadException e) {
        ApiResponseCode apiResponseCode = ApiResponseCode.RESOURCE_LOAD_FAILED;
        log.error("Resource Load Error. Exception Message: {}",e.getMessage());
        return ResponseEntity
                .status(apiResponseCode.getHttpStatus())
                .body(ApiCommonResDto.<Void>builder()
                        .code(apiResponseCode.getCode())
                        .message(apiResponseCode.formatMessage(e.getMessage()))
                        .build()
                );
    }
}
