package com.rumpel.rumpelandroid.utils;

import static com.rumpel.rumpelandroid.utils.AndroidUtils.crash;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Currency;
import java.util.Locale;
import java.util.function.Function;
import java.util.function.Predicate;

public enum NumUtils {
    ;
    private static NumberFormat formatter;

    /**
     * Checks if two numeric expense limits (soft-hard) are compatible. They must be
     * positive, and the soft limit must be lower than the hard limit.
     *
     * @param softLimit Soft limit to check.
     * @param hardLimit Hard limit to check.
     * @return True if the parameters are valid, false otherwise
     */
    public static boolean limitsAreValid(double softLimit, double hardLimit) {
        return softLimit >= 0 && softLimit < hardLimit;
    }

    /**
     * Returns a formatted String from a double quantity for the currency
     * associated with the locale.
     *
     * @param quantity The decimal value to be formatted
     * @return String containing the formatted, two-decimal amount from the passed value
     */
    public static String formatCurrency(final Double quantity) {
        if (quantity == null) throw new NullPointerException("Cannot format a null quantity");
        if (formatter == null) {
            formatter = NumberFormat.getCurrencyInstance();
            formatter.setMaximumFractionDigits(2);
        }
        formatter.setCurrency(Currency.getInstance(Locale.getDefault()));
        return formatter.format(quantity);
    }

    /**
     * Returns a formatted String from a BigDecimal quantity for the currency
     * associated with the locale.
     *
     * @param quantity The BigDecimal value to be formatted
     * @return String containing the formatted, two-decimal amount from the passed value
     */
    public static String formatCurrency(final BigDecimal quantity) {
        if (quantity == null) throw new NullPointerException("Cannot format a null quantity");
        if (formatter == null) {
            formatter = NumberFormat.getCurrencyInstance();
            formatter.setMaximumFractionDigits(2);
        }
        formatter.setCurrency(Currency.getInstance(Locale.getDefault()));
        return formatter.format(quantity);
    }

    /**
     * Returns a formatted String from a BigDecimal quantity for the passed currency.
     *
     * @param quantity The BigDecimal value to be formatted
     * @return String containing the formatted amount from the passed value according to the passed
     * currency
     */
    public static String formatCurrency(final BigDecimal quantity, final Currency currency) {
        if (quantity == null) throw new NullPointerException("Cannot format a null quantity");
        if (currency == null) return formatCurrency(quantity);
        if (formatter == null) formatter = NumberFormat.getCurrencyInstance();
        formatter.setCurrency(currency);
        formatter.setMaximumFractionDigits(currency.getDefaultFractionDigits());
        return formatter.format(quantity);
    }

    public static boolean expenseIsValid(BigDecimal b1) {
        return b1.compareTo(BigDecimal.ZERO) >= 0;
    }

    public static <T> BigDecimal decimalSum(final Collection<T> collection,
                                            final Predicate<T> predicate,
                                            final Function<T, BigDecimal> func,
                                            final Predicate<BigDecimal> predicateNum) {
        if (collection == null) throw new NullPointerException("Passed collection is null");
        if (func == null) throw new NullPointerException("Passed function is null");
        return collection.stream()
                .filter(predicate == null ? t -> true : predicate)
                .map(func)
                .filter(predicateNum == null ? t -> true : predicateNum)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static <T> Double doubleSum(final Collection<T> collection, final Function<T, Double> func) {
        if (collection == null) throw new NullPointerException("Passed collection is null");
        if (func == null) throw new NullPointerException("Passed function is null");
        return collection.stream()
                .map(func)
                .reduce(0.0, Double::sum);
    }

    public static <T> Integer intSum(final Collection<T> collection, final Function<T, Integer> func) {
        if (collection == null) throw new NullPointerException("Passed collection is null");
        if (func == null) throw new NullPointerException("Passed function is null");
        return collection.stream()
                .map(func)
                .reduce(0, Integer::sum);
    }
}
