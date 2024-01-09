package com.goldencashbunny.demo.presentation.exceptions;

import com.goldencashbunny.demo.core.messages.ErrorMessages;
import com.goldencashbunny.demo.presentation.exceptions.base.NotFoundException;

public class RoleNotFoundException extends NotFoundException {
    public RoleNotFoundException() {
        super(ErrorMessages.ERROR_ACCOUNT_ROLE_NOT_FOUND.getMessage());
    }
}
