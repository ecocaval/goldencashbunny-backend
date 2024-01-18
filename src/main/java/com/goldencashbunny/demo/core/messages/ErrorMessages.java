package com.goldencashbunny.demo.core.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessages {

    ERROR_ACCOUNT_EMAIL_OUT_OF_PATTERN("Por favor, insira um e-mail válido."),
    ERROR_ACCOUNT_CPF_OUT_OF_PATTERN("Por favor, insira um cpf válido."),
    ERROR_ACCOUNT_CNPJ_OUT_OF_PATTERN("Por favor, insira um cnpj válido."),
    ERROR_ACCOUNT_EMAIL_ALREADY_IN_USE("Este e-mail já está em uso."),
    ERROR_ACCOUNT_NOT_FOUND_BY_USERNAME("A conta com este username não foi encontrada."),
    ERROR_ACCOUNT_NOT_FOUND_BY_ID("A conta com este id não foi encontrada."),
    ERROR_ACCOUNT_NOT_FOUND_BY_EMAIL("A conta com este e-mail não foi encontrada."),
    ERROR_ACCOUNT_NOT_FOUND_BY_CPF("A conta com este cpf não foi encontrada."),
    ERROR_ACCOUNT_NOT_FOUND_BY_CNPJ("A conta com este cnpj não foi encontrada."),
    ERROR_ACCOUNT_ROLE_NOT_FOUND("A account_role buscada é inválida."),
    ERROR_ACCOUNT_DUPLICATED_BY_EMAIL("Já existe uma conta registrada com este e-mail."),
    ERROR_ACCOUNT_DUPLICATED_BY_CPF("Já existe uma conta registrada com este cpf."),
    ERROR_ACCOUNT_DUPLICATED_BY_CNPJ("Já existe uma conta registrada com este cnpj."),
    ERROR_ACCOUNT_INVALID_LOGIN("Não encontramos uma conta para o login fornecido."),
    ERROR_INVALID_COLUMN_REFERENCE("A referencia da coluna informada não está entre os valores permitidos."),
    ERROR_INVALID_ROW_REFERENCE("A referencia da linha da coluna informada não está entre os valores permitidos."),
    ERROR_INVALID_WORKSPACE_ID("O id do workspace é inválido."),
    ERROR_INVALID_SPACE_ID("O id do space é inválido."),
    ERROR_INVALID_SPACE_TABLE_ID("O id do space table é inválido."),
    ERROR_INVALID_SPACE_TABLE_COLUMN_ID("O id da coluna da tabela é inválido."),
    ERROR_INVALID_ACCOUNT_ID("O id da conta é inválido."),
    ERROR_DURING_TOKEN_GENERATION("Um erro ocorreu ao tentar gerar token."),
    ERROR_WORKSPACE_NOT_FOUND_BY_ID("O workspace com este id não foi encontrada."),
    ERROR_SPACE_NOT_FOUND_BY_ID("O space com este id não foi encontrada."),
    ERROR_SPACE_TABLE_NOT_FOUND_BY_ID("O tabela com este id não foi encontrada."),
    ERROR_SPACE_TABLE_COLUMN_NOT_FOUND_BY_ID("A coluna da tabela com este id não foi encontrada."),
    ERROR_INVALID_VALUE_FOR_COLUMN_TYPE("Este valor não pode ser atribuido a tabela, pois difere-se do tipo da coluna."),
    ERROR_INVALID_TOKEN("Por favor, envie um token válido para acessar este recurso."),
    ;

    private final String message;
}
