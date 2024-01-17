package com.goldencashbunny.demo.presentation.exceptions;

import com.goldencashbunny.demo.presentation.exceptions.base.NotFoundException;

public class SpaceTableNotFoundException extends NotFoundException {
    public SpaceTableNotFoundException(String message) {
        super(message);
    }
}
