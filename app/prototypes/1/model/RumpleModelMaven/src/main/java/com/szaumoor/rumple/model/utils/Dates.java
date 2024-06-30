package com.szaumoor.rumple.model.utils;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public enum Dates {
    ;

    private static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static boolean validatePeriod(final ZonedDateTime startDate, final ZonedDateTime endDate) {
        return endDate.isAfter(startDate);
    }

    public static boolean notFuture(final ZonedDateTime date) {
        return date.isBefore(ZonedDateTime.now().plusDays(1L));
    }

    public static boolean notPast(final ZonedDateTime date) {
        return date.isAfter(ZonedDateTime.now());
    }

    public static String format(final ZonedDateTime date) {
        return FORMATTER.format(date);
    }

    public static String format(final LocalDate date) {
        return FORMATTER.format(date);
    }

    public static void setFormatter(final DateTimeFormatter formatter) {
        Dates.FORMATTER = formatter;
    }
}
