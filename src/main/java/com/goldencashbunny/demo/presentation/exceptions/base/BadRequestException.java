package com.goldencashbunny.demo.presentation.exceptions.base;

public class BadRequestException extends BaseException {
    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Object details) {
        super(message, details);
    }
}
