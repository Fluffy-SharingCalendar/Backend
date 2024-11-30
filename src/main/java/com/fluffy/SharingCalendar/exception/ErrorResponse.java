package com.fluffy.SharingCalendar.exception;

public record ErrorResponse(Integer errorCode, String message) {
    public static ErrorResponse from(ErrorCode errorCode) {
        return new ErrorResponse(errorCode.getHttpStatus().value(), errorCode.getMessage());
    }
}
