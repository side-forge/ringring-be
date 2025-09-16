package com.sideforge.ringring.exception.dto;

public class TooManyRequestException extends RuntimeException {
    public TooManyRequestException(String message) {
        super(message);
    }
}
