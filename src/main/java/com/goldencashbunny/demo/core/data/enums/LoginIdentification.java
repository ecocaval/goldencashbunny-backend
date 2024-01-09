package com.goldencashbunny.demo.core.data.enums;

public enum LoginIdentification {
    USERNAME, EMAIL, CPF, CNPJ;

    public static LoginIdentification getFromLogin(String login) {

        if (RegexValidator.applyRegexValidation(RegexValidator.EMAIL_REGEX , login)) {
            return LoginIdentification.EMAIL;
        }

        if (RegexValidator.applyRegexValidation(RegexValidator.CPF_REGEX , login)) {
            return LoginIdentification.CPF;
        }

        if (RegexValidator.applyRegexValidation(RegexValidator.CNPJ_REGEX , login)) {
            return LoginIdentification.CNPJ;
        }

        return LoginIdentification.USERNAME;
    }
}
