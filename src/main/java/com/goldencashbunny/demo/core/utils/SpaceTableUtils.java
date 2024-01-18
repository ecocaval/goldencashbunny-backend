package com.goldencashbunny.demo.core.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SpaceTableUtils {

    private static final String[] DATE_ACCEPTED_PATTERNS = {"yyyy/MM/dd", "dd-MM-yyyy", "MM-dd-yyyy", "yyyy-MM-dd", "dd/MM/yyyy"};

    private static final String DEFAULT_DATE_PATTERN = "dd/MM/yyyy";

    public static boolean checkIfValueIsBoolean(String value) {

        if (value == null || !(value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false"))) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public static boolean checkIfValueIsDate(String value) {

        for (String pattern : DATE_ACCEPTED_PATTERNS) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);

                dateFormat.setLenient(false);

                dateFormat.parse(value);

                return Boolean.TRUE;

            } catch (ParseException ignored) {}
        }
        return Boolean.FALSE;
    }

    public static String convertStringToDefaultDatePattern(String value) {

        for (String pattern : DATE_ACCEPTED_PATTERNS) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);

                dateFormat.setLenient(false);

                Date date = dateFormat.parse(value);

                SimpleDateFormat targetFormat = new SimpleDateFormat(DEFAULT_DATE_PATTERN);

                return targetFormat.format(date);

            } catch (ParseException ignored) {}
        }
        return value;
    }

    public static boolean checkIfValueIsNumber(String str) {

        if (str == null) {
            return Boolean.FALSE;
        }

        try {
            Long.parseLong(str);
            return Boolean.TRUE;
        } catch (NumberFormatException ignored) {}

        try {
            Double.parseDouble(str);
            return Boolean.TRUE;
        } catch (NumberFormatException ignored) {}

        return Boolean.FALSE;
    }
}
