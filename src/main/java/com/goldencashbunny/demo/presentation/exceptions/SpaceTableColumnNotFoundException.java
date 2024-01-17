package com.goldencashbunny.demo.presentation.exceptions;

import com.goldencashbunny.demo.presentation.exceptions.base.NotFoundException;

public class SpaceTableColumnNotFoundException extends NotFoundException {
    public SpaceTableColumnNotFoundException(String message) {
        super(message);
    }
}
