package com.goldencashbunny.demo.presentation.exceptions.handler;


import lombok.Builder;

@Builder
public class ApiExceptionResponse {
    private String errorMessage;
    private String timestamp;
    private String instance;
}
