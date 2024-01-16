package com.goldencashbunny.demo.presentation.exceptions;

import com.goldencashbunny.demo.presentation.exceptions.base.NotFoundException;

public class WorkSpaceNotFoundException extends NotFoundException {
    public WorkSpaceNotFoundException(String message) {
        super(message);
    }
}
