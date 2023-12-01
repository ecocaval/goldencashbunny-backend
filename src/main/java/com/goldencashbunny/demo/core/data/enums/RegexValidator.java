package com.goldencashbunny.demo.core.data.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.regex.Pattern;

@AllArgsConstructor
@Getter
public enum RegexValidator {

    EMAIL_REGEX("^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-zA-Z]{2,})$"),
    CPF_REGEX("^[0-9]{3}\\.?[0-9]{3}\\.?[0-9]{3}\\-?[0-9]{2}$"),
    CNPJ_REGEX("^(\\d{2})\\.?(\\d{3})\\.?(\\d{3})/?\\d{4}-?(\\d{2})$")
    ;

    private final String regex;

    public static void applyRegexValidation(RegexValidator regex, String input, String errorMessage) {
        if(!Pattern.matches(regex.getRegex(), input)) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
