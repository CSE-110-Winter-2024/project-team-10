package edu.ucsd.cse110.successorator.lib.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateConverter {
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static LocalDate StringToLocalDate(String value) {
        if (value == null) {
            return null;
        }
        return LocalDate.parse(value, formatter);
    }

    public static String LocalDateToString(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.format(formatter);
    }
}
