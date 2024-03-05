package edu.ucsd.cse110.successorator.lib.util;

import androidx.room.TypeConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateConvereter {
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @TypeConverter
    public static LocalDate StringToLocalDate(String value) {
        if (value == null) {
            return null;
        }
        return LocalDate.parse(value, formatter);
    }

    @TypeConverter
    public static String LocalDateToString(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.format(formatter);
    }
}
