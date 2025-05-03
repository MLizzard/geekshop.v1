package com.study.geekshop.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.ACCEPTED) // HTTP 202 - лог пока не готов, но запрос принят
public class LogNotReadyException extends RuntimeException {
    public LogNotReadyException(String message) {
        super(message);
    }
}
