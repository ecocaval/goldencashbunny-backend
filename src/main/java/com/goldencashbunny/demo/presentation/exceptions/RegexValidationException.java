package com.goldencashbunny.demo.presentation.exceptions;

import com.goldencashbunny.demo.presentation.exceptions.base.BadRequestException;
import lombok.Getter;

@Getter
public class RegexValidationException extends BadRequestException {

    public RegexValidationException(String message) {
        super(message);
    }
}
