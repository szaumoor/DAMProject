package com.szaumoor.rumple.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Utility class for dates.
 */
public final class Dates {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("dd/MM/yyyy -- HH:mm:ss");

    /**
     * Private constructor to prevent instantiation
     */
    private Dates() {
        throw new AssertionError("Utility class cannot be instantiated");
    }

    /**
     * Somewhat hacky implementation of a function that checks if a date is valid, which in this case
     * it means that the end day comes after the start day.
     *
     * @param startDate The start date
     * @param endDate   The end date
     * @return true if the end date is after the start date
     */
    public static boolean validatePeriod(final ZonedDateTime startDate, final ZonedDateTime endDate) {
        return endDate.isAfter(
                startDate.plusDays(1)
                        .toLocalDateTime()
                        .minusNanos(1)
                        .atZone(ZoneId.systemDefault()));
    }

    /**
     * Determines if the given date is in the past or present.
     *
     * @param date the date to be checked
     * @return true if the date is before the current date plus one day, false otherwise
     */
    public static boolean pastOrPresent(final ZonedDateTime date) {
        return date.isBefore(LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()));
    }

    /**
     * Checks if the given date is present or in the future.
     *
     * @param date the date to be checked
     * @return true if the date is after the current date minus one day, false otherwise
     */
    public static boolean presentOrFuture(final ZonedDateTime date) {
        return date.isAfter(
                ZonedDateTime.now().minusDays(1)
        );
    }

    /**
     * Determines if the given date is in the past.
     *
     * @param date the date to check
     * @return true if the date is in the past, false otherwise
     */
    public static boolean past(final ZonedDateTime date) {
        return date.isBefore(LocalDate.now().atStartOfDay(ZoneId.systemDefault()));
    }

    /**
     * Checks if the given date is in the future.
     *
     * @param date the date to be checked
     * @return true if the given date is in the future, false otherwise
     */
    public static boolean future(final ZonedDateTime date) {
        return date.isAfter(
                LocalDate.now()
                        .plusDays(1)
                        .atStartOfDay(ZoneId.systemDefault())
                        .minusNanos(1L)
        );
    }

    /**
     * Formats the given date according to the basic formatter in this class.
     *
     * @param date the date to be formatted
     * @return the formatted date as a string
     */
    public static String format(final ZonedDateTime date) {
        return formatter.format(date);
    }

    /**
     * Formats the given date according to the time formatter in this class.
     *
     * @param date the date to be formatted
     * @return the formatted date as a string
     */
    public static String formatWithTime(final ZonedDateTime date) {
        return formatterTime.format(date);
    }

    /**
     * Formats the given date according to the basic formatter in this class.
     *
     * @param date the date to be formatted
     * @return the formatted date as a string
     */
    public static String format(final LocalDate date) {
        return formatter.format(date);
    }

    /**
     * Formats the given date according to the time formatter in this class.
     *
     * @param date the date to be formatted
     * @return the formatted date as a string
     */
    public static String formatWithTime(final LocalDate date) {
        return formatterTime.format(date);
    }

    /**
     * Converts a Date object to a ZonedDateTime object in the system default time zone.
     *
     * @param date the Date object to be converted
     * @return the corresponding ZonedDateTime object
     */
    public static ZonedDateTime zonedFromDate(final Date date) {
        return ZonedDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * Converts a LocalDate object to a ZonedDateTime object in the system default time zone.
     *
     * @param date the LocalDate object to be converted
     * @return the corresponding ZonedDateTime object
     */
    public static ZonedDateTime zonedFromDate(final LocalDate date) {
        return ZonedDateTime.of(date, LocalTime.now(), ZoneId.systemDefault());
    }

    /**
     * Converts a ZonedDateTime object to a Date object.
     *
     * @param dateTime the ZonedDateTime object to be converted
     * @return the converted Date object
     */
    public static Date fromZonedDate(final ZonedDateTime dateTime) {
        return Date.from(dateTime.toInstant());
    }

    /**
     * Converts a LocalDate object to a Date object.
     *
     * @param date the LocalDate object to be converted
     * @return the converted Date object
     */
    public static Date fromLocalDate(final LocalDate date) {
        return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
