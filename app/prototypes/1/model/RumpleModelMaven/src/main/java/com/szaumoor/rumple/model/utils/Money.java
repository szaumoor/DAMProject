package com.szaumoor.rumple.model.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Currency;

public enum Money {
    ;

    private static final NumberFormat FORMATTER = NumberFormat.getCurrencyInstance();

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
}
