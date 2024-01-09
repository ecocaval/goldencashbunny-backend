package com.goldencashbunny.demo.presentation.exceptions;

import com.goldencashbunny.demo.presentation.exceptions.base.ConflictException;

import java.util.Map;

public class DuplicationOnRegistrationException extends ConflictException {

    public DuplicationOnRegistrationException(String message, String duplicatedField, String duplicatedValue) {
        super(message, Map.of(duplicatedField, duplicatedValue));
    }
}
