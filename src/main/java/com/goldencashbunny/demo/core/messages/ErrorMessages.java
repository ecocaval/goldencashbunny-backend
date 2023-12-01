package com.goldencashbunny.demo.core.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessages {

    ERROR_ACCOUNT_EMAIL_OUT_OF_PATTERN("Por favor, insira um e-mail válido."),
    ERROR_ACCOUNT_CPF_OUT_OF_PATTERN("Por favor, insira um CPF válido."),
    ERROR_ACCOUNT_CNPJ_OUT_OF_PATTERN("Por favor, insira um CNPJ válido."),
    ERROR_ACCOUNT_EMAIL_ALREADY_IN_USE("Este e-mail já está em uso."),
    ERROR_ACCOUNT_NOT_FOUND_BY_ID("A Conta com este ID não foi encontrada."),
    ERROR_ACCOUNT_NOT_FOUND_BY_EMAIL("A Conta com este e-mail não foi encontrada."),
    ERROR_ACCOUNT_DUPLICATED_BY_EMAIL("Já existe uma conta registrada com este e-mail."),
    ERROR_ACCOUNT_DUPLICATED_BY_CPF("Já existe uma conta registrada com este CPF."),
    ERROR_ACCOUNT_DUPLICATED_BY_CNPJ("Já existe uma conta registrada com este CNPJ."),
    ;

    private final String message;
}
