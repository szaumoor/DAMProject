package com.rumpel.rumpelandroid.utils;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;

/**
 * Helper class with utilities relating to managing dates.
 */
public enum DateUtils {
    ;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE d LLL yyyy");
    private static final DateTimeFormatter formatterShort = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static String formatDate(final ZonedDateTime date) {
        return formatter.format(date);
    }

    /**
     * Checks if the passed dates are not null and they make sense: that is, the ending date
     * is chronologically after the start date.
     *
     * @param startDate Starting date to check against
     * @param endDate   Ending date to check against
     * @return True if the parameters are valid, false otherwise
     * @throws NullPointerException if any of the passed dates are null
     */
    public static boolean periodIsValid(final ZonedDateTime startDate, final ZonedDateTime endDate) {
        if (startDate == null) throw new NullPointerException("Start date is null");
        if (endDate == null) throw new NullPointerException("End date is null");
        return endDate.isAfter(startDate);
    }

    /**
     * Checks whether a date is within a certain period.
     *
     * @param startDate Start date of the period.
     * @param endDate   End date of the period.
     * @param toCheck   Date to check.
     * @return True if the date is contained in the period, false otherwise.
     * @throws NullPointerException if any of the passed dates are null
     */
    public static boolean dateWithinPeriod(final ZonedDateTime startDate, final ZonedDateTime endDate,
                                           final ZonedDateTime toCheck) {
        if (startDate == null) throw new NullPointerException("Start date is null");
        if (endDate == null) throw new NullPointerException("End date is null");
        if (toCheck == null) throw new NullPointerException("Date to check is null");
        return toCheck.isAfter(startDate.minusDays(1)) && toCheck.isBefore(endDate.plusDays(1));
    }

    /**
     * Checks if the passed third and fourth dates are contained within
     * the first and second. That is, this checks if those are a subset of
     * the first or not. Note that this method considers an identical interval
     * as a subset.
     *
     * @param startToCompareTo The start date of the interval.
     * @param endToCompareTo   The end date of the interval.
     * @param startDate        The start date of the interval to check.
     * @param endDate          The end date of the interval to check.
     * @return True if the second computer interval is contained within the first,
     * false if the intervals are invalid or are not contained in the first.
     */
    public static boolean periodContainsOther(final ZonedDateTime startToCompareTo, final ZonedDateTime endToCompareTo,
                                              final ZonedDateTime startDate, final ZonedDateTime endDate) {
        return periodIsValid(startToCompareTo, endToCompareTo) &&
                periodIsValid(startDate, endDate) &&
                startToCompareTo.isAfter(startDate.minusDays(1)) &&
                endToCompareTo.isBefore(endDate.plusDays(1));

    }

    /**
     * Extract a Date object from a LocalDate. It's here to turn the more up-to-date
     * and useful LocalDate objects to avoid incompatibilities with the current database
     * APIs.
     *
     * @param date LocalDate to be converted.
     * @return Resulting Date object from the provided LocalDate or null if the argument passed is null.
     */
    public static Optional<Date> getDateObject(final LocalDate date) {
        return Optional.ofNullable(date == null ? null : new Date
                (
                        date.getYear(),
                        date.getMonthValue() - 1,
                        date.getDayOfMonth()
                )
        );
    }
}