package com.szaumoor.rumple.model.utils.types;

import com.szaumoor.rumple.model.interfaces.Formattable;
import com.szaumoor.rumple.model.utils.Money;
import com.szaumoor.rumple.model.utils.Numbers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * This class encapsulates the budget selected limits for the purposes of notifications
 * and reports.
 */
public final class Limit implements Formattable<Map<String, String>> {
    private final BigDecimal softLimit, hardLimit;
    private final Currency curr;

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
    public Limit(final BigDecimal softLimit, final BigDecimal hardLimit, final Currency curr) {
        this.softLimit = softLimit;
        this.hardLimit = hardLimit;
        this.curr = curr;
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
        if (!Numbers.limitsValid(softLimit, hardLimit) || Objects.isNull(curr)) return Optional.empty();
        return Optional.of(new Limit(softLimit, hardLimit, curr));
    }

    /**
     * Getter for the hard limit of this budget limit.
     *
     * @return the hard limit of this budget, that is, the limit that should not be surpassed.
     */
    public BigDecimal getHardLimit() {
        return hardLimit;
    }

    /**
     * Getter for the currency of this budget limit.
     *
     * @return the currency of this budget limit.
     */
    public Currency getCurrency() {
        return curr;
    }

    /**
     * Getter for the soft limit of this budget limit.
     *
     * @return the soft limit of this budget, that is, the limit that should not be surpassed.
     */
    public BigDecimal getSoftLimit() {
        return softLimit;
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

    /**
     * Returns a formatted String of this limit.
     *
     * @return The resulting String.
     */
    @Override
    public String toString() {
        return Money.formatCurrency(softLimit, curr) + ", " + Money.formatCurrency(hardLimit,curr);
    }


    @Override
    public Map<String, String> format() {
        return Map.of(SOFT_LIMIT, Money.formatCurrency(softLimit, curr),
                HARD_LIMIT, Money.formatCurrency(hardLimit, curr),
                AVG_LIMIT, Money.formatCurrency(getSecondWarning(), curr));
    }
}
