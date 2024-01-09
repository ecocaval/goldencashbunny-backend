package com.goldencashbunny.demo.presentation.exceptions;

import com.goldencashbunny.demo.core.messages.ErrorMessages;
import com.goldencashbunny.demo.presentation.exceptions.base.BadRequestException;
import lombok.Getter;

@Getter
public class InvalidLoginException extends BadRequestException {

    public InvalidLoginException() {
        super(ErrorMessages.ERROR_ACCOUNT_INVALID_LOGIN.getMessage());
    }
}
