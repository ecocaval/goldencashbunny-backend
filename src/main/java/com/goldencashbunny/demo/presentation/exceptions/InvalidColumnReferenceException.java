package com.goldencashbunny.demo.presentation.exceptions;

import com.goldencashbunny.demo.core.messages.ErrorMessages;
import com.goldencashbunny.demo.presentation.exceptions.base.BadRequestException;
import lombok.Getter;

@Getter
public class InvalidColumnReferenceException extends BadRequestException {

    private final Integer informedColumnReference;
    private final Integer maxColumnReference;

    public InvalidColumnReferenceException(Integer informedColumnReference, Integer maxColumnReference) {
        super(ErrorMessages.ERROR_INVALID_COLUMN_REFERENCE.getMessage());
        this.informedColumnReference = informedColumnReference;
        this.maxColumnReference = maxColumnReference;
    }
}
