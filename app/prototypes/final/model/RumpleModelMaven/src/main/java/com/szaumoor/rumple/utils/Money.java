package com.szaumoor.rumple.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Comparator;
import java.util.Currency;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Utility class for money.
 */
public final class Money {
    private static final NumberFormat FORMATTER = NumberFormat.getCurrencyInstance();

    /**
     * Private constructor to prevent instantiation
     */
    private Money() {
        throw new AssertionError("Utility class cannot be instantiated");
    }

    /**
     * Checks if the given quantity is in range, which in money terms means greater than or equal to zero.
     *
     * @param quantity the quantity to check
     * @return true if the quantity is not null and greater than or equal to zero, otherwise false
     */
    public static boolean inRange(final BigDecimal quantity) {
        return quantity != null && quantity.compareTo(BigDecimal.ZERO) >= 0;
    }

    /**
     * Returns a formatted String from a BigDecimal quantity for the passed currency.
     *
     * @param quantity The BigDecimal value to be formatted
     * @return String containing the formatted amount from the passed value according to the passed
     * currency
     */
    public static String formatCurrency(final BigDecimal quantity, final Currency currency) {
        FORMATTER.setCurrency(currency);
        FORMATTER.setMaximumFractionDigits(currency.getDefaultFractionDigits());
        return FORMATTER.format(quantity);
    }

    /**
     * Parses a formatted currency string and returns the corresponding BigDecimal value.
     *
     * @param formatted the formatted currency string to be parsed
     * @return an Optional containing the parsed BigDecimal value, or an empty Optional if the parsing fails
     */
    public static Optional<BigDecimal> unformatCurrency(final String formatted) {
        try {
            return Optional.of(new BigDecimal(FORMATTER.parse(formatted).toString()));
        } catch (final ParseException e) {
            return Optional.empty();
        }
    }

    /**
     * Retrieves a list of currencies sorted by their code.
     *
     * @return a list of currencies sorted by their code
     */
    public static List<Currency> currenciesSortedByCode() {
        return Currency.getAvailableCurrencies()
                .stream()
                .sorted(Comparator.comparing(Currency::toString))
                .collect(Collectors.toList());
    }

    /**
     * Checks if the given currency is a valid currency.
     *
     * @param currency the currency to be checked
     * @return true if the currency is valid, false otherwise
     */
    public static boolean isCurrency(final String currency) {
        try {
            Currency.getInstance(currency);
        } catch (final IllegalArgumentException e) {
            return false;
        }
        return true;
    }
}
