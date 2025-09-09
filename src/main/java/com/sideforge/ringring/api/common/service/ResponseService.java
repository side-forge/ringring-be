package com.sideforge.ringring.api.common.service;

import com.sideforge.ringring.api.common.model.dto.ApiCommonResDto;
import com.sideforge.ringring.api.common.model.dto.PagingResDto;
import com.sideforge.ringring.api.common.model.enums.ApiResponseCode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ResponseService {
    public ResponseEntity<ApiCommonResDto<Void>> resSuccess() {
        ApiResponseCode apiResponseCode = ApiResponseCode.SUCCESS;
        ApiCommonResDto<Void> response = ApiCommonResDto.<Void>builder()
                .code(apiResponseCode.getCode())
                .message(apiResponseCode.getMessage())
                .build();
        return ResponseEntity.status(apiResponseCode.getHttpStatus())
                .body(response);
    }

    public <T> ResponseEntity<ApiCommonResDto<T>> resSuccess(T data) {
        ApiResponseCode apiResponseCode = ApiResponseCode.SUCCESS;
        ApiCommonResDto<T> response = ApiCommonResDto.<T>builder()
                .data(data)
                .code(apiResponseCode.getCode())
                .message(apiResponseCode.getMessage())
                .build();
        return ResponseEntity.status(apiResponseCode.getHttpStatus())
                .body(response);
    }

    public <T> ResponseEntity<ApiCommonResDto<T>> resSuccess(T data, PagingResDto pagingResDto) {
        ApiResponseCode apiResponseCode = ApiResponseCode.SUCCESS;
        ApiCommonResDto<T> response = ApiCommonResDto.<T>builder()
                .data(data)
                .code(apiResponseCode.getCode())
                .message(apiResponseCode.getMessage())
                .pagingData(pagingResDto)
                .build();
        return ResponseEntity.status(apiResponseCode.getHttpStatus())
                .body(response);
    }

    public <T> ResponseEntity<ApiCommonResDto<T>> resSuccess(T data, HttpHeaders extraHeaders) {
        ApiResponseCode apiResponseCode = ApiResponseCode.SUCCESS;
        ApiCommonResDto<T> response = ApiCommonResDto.<T>builder()
                .data(data)
                .code(apiResponseCode.getCode())
                .message(apiResponseCode.getMessage())
                .build();

        return ResponseEntity.status(apiResponseCode.getHttpStatus())
                .headers(extraHeaders != null ? extraHeaders : HttpHeaders.EMPTY)
                .body(response);
    }

    public ResponseEntity<ApiCommonResDto<Void>> resSuccess(HttpHeaders extraHeaders) {
        ApiResponseCode apiResponseCode = ApiResponseCode.SUCCESS;
        ApiCommonResDto<Void> response = ApiCommonResDto.<Void>builder()
                .code(apiResponseCode.getCode())
                .message(apiResponseCode.getMessage())
                .build();

        return ResponseEntity.status(apiResponseCode.getHttpStatus())
                .headers(extraHeaders != null ? extraHeaders : HttpHeaders.EMPTY)
                .body(response);
    }

    public ResponseEntity<ApiCommonResDto<Void>> resFailure(ApiResponseCode apiResponseCode) {
        ApiCommonResDto<Void> response = ApiCommonResDto.<Void>builder()
                .code(apiResponseCode.getCode())
                .message(apiResponseCode.getMessage())
                .build();
        return ResponseEntity.status(apiResponseCode.getHttpStatus())
                .body(response);
    }

    public ResponseEntity<ApiCommonResDto<Void>> resFailure(ApiResponseCode apiResponseCode, String message) {
        ApiCommonResDto<Void> response = ApiCommonResDto.<Void>builder()
                .code(apiResponseCode.getCode())
                .message(message)
                .build();
        return ResponseEntity
                .status(apiResponseCode.getHttpStatus())
                .body(response);
    }

    public <T> ResponseEntity<ApiCommonResDto<T>> resSuccessWithCookies(T data, ResponseCookie... cookies) {
        HttpHeaders headers = new HttpHeaders();
        if (cookies != null) {
            for (ResponseCookie c : cookies) {
                headers.add(HttpHeaders.SET_COOKIE, c.toString());
            }
        }
        return resSuccess(data, headers);
    }

    public ResponseEntity<ApiCommonResDto<Void>> resSuccessWithCookies(ResponseCookie... cookies) {
        HttpHeaders headers = new HttpHeaders();
        if (cookies != null) {
            for (ResponseCookie c : cookies) {
                headers.add(HttpHeaders.SET_COOKIE, c.toString());
            }
        }
        return resSuccess(headers);
    }
}
