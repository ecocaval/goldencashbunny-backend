package com.goldencashbunny.demo.presentation.exceptions;

import java.util.Map;

public class DuplicationOnRegistrationException extends BaseException{
    public DuplicationOnRegistrationException(String message) {
        super(message);
    }
    public DuplicationOnRegistrationException(String message, String duplicatedField, String duplicatedValue) {
        super(message, Map.of(duplicatedField, duplicatedValue));
    }
}
