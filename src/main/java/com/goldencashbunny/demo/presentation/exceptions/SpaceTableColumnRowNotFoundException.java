package com.goldencashbunny.demo.presentation.exceptions;

import com.goldencashbunny.demo.presentation.exceptions.base.NotFoundException;

public class SpaceTableColumnRowNotFoundException extends NotFoundException {
    public SpaceTableColumnRowNotFoundException(String message) {
        super(message);
    }
}
