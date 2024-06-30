package com.szaumoor.rumple.model.entities.types;

import com.szaumoor.rumple.model.interfaces.Formattable;
import com.szaumoor.rumple.utils.Dates;

import java.time.Period;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Optional;

/**
 * This immutable class encapsulates the time span a budget applies to and the functionality thereof.
 */
public record Interval(ZonedDateTime startDate, ZonedDateTime endDate) implements Formattable<Map<String, String>> {

    public static final String START_DATE = "startDate";
    public static final String END_DATE = "endDate";

    /**
     * Constructor for a time interval to use in the context of budgets. Does no validation of any sort and should only be used to construct objects
     * when it's certain that they are valid, such as when pulling them from a database where insertions were previously
     * validated.
     *
     * @param startDate Starting date for a budget.
     * @param endDate   Ending date for a budget.
     */
    public Interval {
    }

    /**
     * Static factory method to construct a BudgetInterval object.
     * It returns null if any of the follow situations happen: <br><br>
     * <p>
     * 1. Start date or end date are null <br>
     * 2. End date is before the start date. <br><br>
     * <p>
     * Otherwise, it returns the specific BudgetInterval object.
     *
     * @param startDate Starting date for this interval
     * @param endDate   Ending date for this interval
     * @return The resulting BudgetInterval or null if the parameters were invalid.
     */
    public static Optional<Interval> of(final ZonedDateTime startDate, final ZonedDateTime endDate) {
        if (Dates.past(startDate) || !Dates.validatePeriod(startDate, endDate)) return Optional.empty();
        return Optional.of(new Interval(startDate, endDate));
    }

    /**
     * Retrieves the time span between the two dates of this interval.
     *
     * @return The number of isolated time units between the two dates in this interval.
     */
    public Period getPeriod() {
        return Period.between(startDate.toLocalDate(), endDate.toLocalDate());
    }


    /**
     * Getter for the start date of this time interval.
     *
     * @return Start date of this interval
     */
    @Override
    public ZonedDateTime startDate() {
        return startDate;
    }

    /**
     * Getter for the end date of this time interval.
     *
     * @return End date of this interval
     */
    @Override
    public ZonedDateTime endDate() {
        return endDate;
    }

    @Override
    public Map<String, String> format() {
        return Map.of(START_DATE, Dates.format(startDate),
                END_DATE, Dates.format(endDate));
    }

    @Override
    public String toString() {
        return Dates.format(startDate) + "-" + Dates.format(endDate);
    }
}