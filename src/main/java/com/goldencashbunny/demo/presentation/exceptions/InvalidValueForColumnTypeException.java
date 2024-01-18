package com.goldencashbunny.demo.presentation.exceptions;

import com.goldencashbunny.demo.presentation.exceptions.base.BadRequestException;
import lombok.Getter;

@Getter
public class InvalidValueForColumnTypeException extends BadRequestException {

    private final String value;
    private final String columnType;

    public InvalidValueForColumnTypeException(String message, String value, String columnType) {
        super(message);
        this.value = value;
        this.columnType = columnType;
    }
}
