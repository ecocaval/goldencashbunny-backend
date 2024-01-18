package com.goldencashbunny.demo.presentation.exceptions;

import com.goldencashbunny.demo.presentation.exceptions.base.BadRequestException;
import lombok.Getter;

@Getter
public class InvalidRowReferenceException extends BadRequestException {

    public InvalidRowReferenceException(String message) {
        super(message);
    }
}
