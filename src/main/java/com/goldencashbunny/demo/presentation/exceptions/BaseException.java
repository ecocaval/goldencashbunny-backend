package com.goldencashbunny.demo.presentation.exceptions;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
public class BaseException extends RuntimeException {

    private final String message;
    private final LocalDateTime timestamp = LocalDateTime.now();
    private Map<Object, Object> details;

    public BaseException(String message) {
        this.message = message;
    }
}
