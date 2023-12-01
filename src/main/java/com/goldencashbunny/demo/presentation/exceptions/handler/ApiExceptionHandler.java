package com.goldencashbunny.demo.presentation.exceptions.handler;

import com.goldencashbunny.demo.presentation.exceptions.DuplicationOnRegistrationException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({DuplicationOnRegistrationException.class})
    protected ResponseEntity<Object> handleDuplicationOnRegistrationException(
            DuplicationOnRegistrationException e,
            WebRequest request,
            HttpServletRequest httpServletRequest
    ) {
        ApiExceptionResponse apiExceptionResponse = ApiExceptionResponse.builder()
                .errorMessage(e.getMessage())
                .timestamp(e.getTimestamp().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .instance(httpServletRequest.getRequestURI())
                .details(e.getDetails())
                .build();

        return handleExceptionInternal(
                e,
                apiExceptionResponse,
                new HttpHeaders(),
                HttpStatus.CONFLICT,
                request
        );
    }

    @ExceptionHandler({IllegalArgumentException.class})
    protected ResponseEntity<Object> handleIllegalArgumentException(
            IllegalArgumentException e,
            WebRequest request,
            HttpServletRequest httpServletRequest
    ) {
        ApiExceptionResponse apiExceptionResponse = ApiExceptionResponse.builder()
                .errorMessage(e.getMessage())
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .instance(httpServletRequest.getRequestURI())
                .build();

        return handleExceptionInternal(
                e,
                apiExceptionResponse,
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST,
                request
        );
    }
}
