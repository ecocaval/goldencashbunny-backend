package com.goldencashbunny.demo.core.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessages {

    ERROR_ACCOUNT_EMAIL_ALREADY_IN_USE("This email is already on use."),
    ERROR_ACCOUNT_NOT_FOUND_BY_ID("Account with this id was not found."),
    ERROR_ACCOUNT_NOT_FOUND_BY_EMAIL("Account with this email was not found."),
    ERROR_ACCOUNT_DUPLICATED_BY_EMAIL("There is already an account registered with this email."),
    ERROR_ACCOUNT_DUPLICATED_BY_CPF("There is already an account registered with this cpf."),
    ERROR_ACCOUNT_DUPLICATED_BY_CNPJ("There is already an account registered with this cnpj."),
    ;

    private final String message;
}
