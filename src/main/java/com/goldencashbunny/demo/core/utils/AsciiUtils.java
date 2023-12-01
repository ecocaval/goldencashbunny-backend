package com.goldencashbunny.demo.core.utils;

import java.text.Normalizer;

public class AsciiUtils {

    private String removeAccents(String input) {
        return Normalizer.normalize(input, Normalizer.Form.NFD).replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
    }

    private String toLowerCaseAndTrim(String input) {
        return input.trim().toLowerCase();
    }

    private String removeSpecialCharactersForDocuments(String input) {
        return input.replaceAll("-", "").replaceAll("\\.", "");
    }

    private String processString(String input, boolean forDocument) {

        if (input == null || input.isBlank()) {
            return input;
        }

        input = removeAccents(input);
        input = toLowerCaseAndTrim(input);

        if (forDocument) {
            input = removeSpecialCharactersForDocuments(input);
        }

        return input;
    }

    public String cleanString(String input) {
        return processString(input, Boolean.FALSE);
    }

    public String cleanDocumentString(String input) {
        return processString(input, Boolean.TRUE);
    }
}

