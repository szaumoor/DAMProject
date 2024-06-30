package com.szaumoor.rumple.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public enum Dates {
    ;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("dd/MM/yyyy -- HH:mm:ss");

    public static boolean validatePeriod(final ZonedDateTime startDate, final ZonedDateTime endDate) {
        return endDate.isAfter(
                startDate.plusDays(1)
                        .toLocalDateTime()
                        .minusNanos(1)
                        .atZone(ZoneId.systemDefault()));
    }

    public static boolean pastOrPresent(final ZonedDateTime date) {
        return date.isBefore(LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()));
    }

    public static boolean presentOrFuture(final ZonedDateTime date) {
        return date.isAfter(
                ZonedDateTime.now().minusDays(1)
        );
    }

    public static boolean past(final ZonedDateTime date) {
        return date.isBefore(LocalDate.now().atStartOfDay(ZoneId.systemDefault()));
    }

    public static boolean future(final ZonedDateTime date) {
        return date.isAfter(
                LocalDate.now()
                        .plusDays(1)
                        .atStartOfDay(ZoneId.systemDefault())
                        .minusNanos(1L)
        );
    }

    public static String format(final ZonedDateTime date) {
        return formatter.format(date);
    }

    public static String formatWithTime(final ZonedDateTime date) {
        return formatterTime.format(date);
    }

    public static String format(final LocalDate date) {
        return formatter.format(date);
    }

    public static String formatWithTime(final LocalDate date) {
        return formatterTime.format(date);
    }

    public static ZonedDateTime zonedFromDate(final Date date) {
        return ZonedDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    public static Date fromZonedDate(final ZonedDateTime dateTime) {
        return Date.from(dateTime.toInstant());
    }
}
