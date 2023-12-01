package com.goldencashbunny.demo.presentation.exceptions.handler;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class ApiExceptionResponse {
    private String errorMessage;
    private String timestamp;
    private String instance;
    private Object details;
}
