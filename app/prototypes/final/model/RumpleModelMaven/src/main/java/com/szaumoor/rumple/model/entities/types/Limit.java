package com.szaumoor.rumple.model.entities.types;

import com.szaumoor.rumple.model.interfaces.Formattable;
import com.szaumoor.rumple.utils.Money;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.szaumoor.utils.Nulls.nonNull;

/**
 * This class encapsulates the budget selected limits for the purposes of notifications
 * and reports.
 */
public record Limit(BigDecimal softLimit, BigDecimal hardLimit, Currency currency) implements Formattable<Map<String, String>> {

    public static final String SOFT_LIMIT = "softLimit";
    public static final String HARD_LIMIT = "hardLimit";
    public static final String AVG_LIMIT = "avgLimit";

    /**
     * Constructor for the budget limit. Does no validation of any sort and should only be used to construct objects
     * when it's certain that they are valid, such as when pulling them from a database where insertions were previously
     * validated.
     *
     * @param softLimit This is the limit where a warning should be issued.
     * @param hardLimit This is the limit that should not be crossed for the associated Budget.
     */
    public Limit {
    }

    /**
     * Static factory method to create a BudgetLimit. Returns null if the parameters are invalid.
     *
     * @param softLimit This is the limit where a warning should be issued. It cannot be negative
     *                  or higher than the hard limit.
     * @param hardLimit This is the limit that should not be crossed for the associated Budget. It
     *                  cannot be negative or lower than the soft limit.
     * @param curr      Currency associated with this budget
     * @return A BudgetLimit object from the passed args, or null if it's invalid.
     */
    public static Optional<Limit> of(final BigDecimal softLimit, final BigDecimal hardLimit, final Currency curr) {
        if (!validateLimit(softLimit, hardLimit) || Objects.isNull(curr)) return Optional.empty();
        return Optional.of(new Limit(softLimit, hardLimit, curr));
    }


    /**
     * Calculates the threshold for the second warning, which is the arithmetic average
     * of both limits.
     *
     * @return The amount representing the average of both limits for notification purposes.
     */
    public BigDecimal getSecondWarning() {
        return hardLimit.add(softLimit).divide(new BigDecimal("2.0"), RoundingMode.UNNECESSARY);
    }

    private static boolean validateLimit(final BigDecimal lowerLimit, final BigDecimal higherLimit) {
        return nonNull(lowerLimit, higherLimit) &&
                lowerLimit.compareTo(BigDecimal.ZERO) >= 0 &&
                higherLimit.compareTo(lowerLimit) > 0;
    }

    /**
     * Returns a formatted String of this limit.
     *
     * @return The resulting String.
     */
    @Override
    public String toString() {
        return Money.formatCurrency(softLimit, currency) + ", " + Money.formatCurrency(hardLimit, currency);
    }

    @Override
    public Map<String, String> format() {
        return Map.of(SOFT_LIMIT, Money.formatCurrency(softLimit, currency),
                HARD_LIMIT, Money.formatCurrency(hardLimit, currency),
                AVG_LIMIT, Money.formatCurrency(getSecondWarning(), currency));
    }
}
