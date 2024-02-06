package com.goldencashbunny.demo.presentation.exceptions.base;

public class NotFoundException extends BaseException {
    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Object details) {
        super(message, details);
    }
}
