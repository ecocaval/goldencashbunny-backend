package com.goldencashbunny.demo.presentation.exceptions;

import com.goldencashbunny.demo.presentation.exceptions.base.BadRequestException;
import lombok.Getter;

import java.util.Map;

@Getter
public class InvalidValueForColumnTypeException extends BadRequestException {

    public InvalidValueForColumnTypeException(String message, String value, String columnType) {
        super(message, Map.of("value", value, "columnType", columnType));
    }
}
