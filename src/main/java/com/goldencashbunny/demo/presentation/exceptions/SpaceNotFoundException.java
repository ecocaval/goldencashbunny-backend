package com.goldencashbunny.demo.presentation.exceptions;

import com.goldencashbunny.demo.presentation.exceptions.base.NotFoundException;

public class SpaceNotFoundException extends NotFoundException {
    public SpaceNotFoundException(String message) {
        super(message);
    }
}
