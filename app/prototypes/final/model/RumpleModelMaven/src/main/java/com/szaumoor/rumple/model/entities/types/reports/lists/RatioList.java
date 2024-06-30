package com.szaumoor.rumple.model.entities.types.reports.lists;

import com.szaumoor.rumple.db.utils.Pair;
import com.szaumoor.rumple.model.entities.types.reports.Stat;
import com.szaumoor.rumple.model.interfaces.Entity;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

/**
 * Abstract class for lists of ratios.
 *
 * @param <I> The type of the items this ratio list uses
 */
public abstract class RatioList<I extends Entity> extends StatList<RatioList.Ratio, I> {
    /**
     * Adds the provided items to the list
     *
     * @param items The items to add
     */
    protected RatioList(final List<I> items) {
        super(items);
    }

    /**
     * Convenience record to hold a name and a ratio for various elements.
     *
     * @param name  The name of the element
     * @param ratio The ratio
     */
    public record Ratio(String name, double ratio) implements Stat {
        public double asPercentage() {
            return ratio * 100.0;
        }

        /**
         * Converts the ratio to a percentage string representation.
         *
         * @return a string representation of the number as a percentage
         */
        public String percentageAsString() {
            return asPercentage() + "%";
        }
        public String ratioAsString() {
            return ratio + "%"; // this makes no sense, actually
        }
    }

    /**
     * Unpacks a list of pairs into a single BigDecimal to get the total
     *
     * @param values the list of pairs
     * @return the total
     * @param <T> the type of the pairs
     */
    protected static <T> BigDecimal getTotalFromPairs(final List<Pair<T, BigDecimal>> values) {
        return values.stream().map(Pair::second).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Handles the division of two big decimals as it's implemented in the app
     *
     * @param dividend the dividend
     * @param divisor  the divisor
     * @return the result as a Double
     */
    protected static Double divide(final BigDecimal dividend, final BigDecimal divisor) {
        return dividend.divide(divisor, MathContext.DECIMAL32).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
    }
}
