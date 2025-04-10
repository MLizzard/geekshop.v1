package com.study.geekshop.exceptions;

import java.time.LocalDateTime;

public record ErrorResponse(
        int code,
        String message,
        LocalDateTime timeStamp
) {
    public ErrorResponse(int code, String message) {
        this(code, message, LocalDateTime.now());
    }
}
