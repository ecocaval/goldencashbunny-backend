package com.goldencashbunny.demo.presentation.exceptions;

import com.goldencashbunny.demo.core.messages.ErrorMessages;
import com.goldencashbunny.demo.presentation.exceptions.base.NotFoundException;

import java.util.Map;

public class CepNotFoundException extends NotFoundException {

    public CepNotFoundException(String zipCode) {
        super(ErrorMessages.ERROR_ADDRESS_NOT_FOUND_FOR_ZIP_CODE.getMessage(), Map.of("zipCode", zipCode));
    }
}
