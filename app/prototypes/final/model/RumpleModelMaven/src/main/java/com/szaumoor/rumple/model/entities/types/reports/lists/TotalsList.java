package com.szaumoor.rumple.model.entities.types.reports.lists;

import com.szaumoor.rumple.model.entities.types.reports.Stat;
import com.szaumoor.rumple.model.interfaces.Entity;

import java.math.BigDecimal;
import java.util.List;

/**
 * Abstract class for lists of totals.
 *
 * @param <I> The type of the items this total list uses
 */
public abstract class TotalsList<I extends Entity> extends StatList<TotalsList.Total, I> {
    /**
     * Convenience record to hold a name and a total for various elements.
     *
     * @param name  The name of the element
     * @param total The total
     */
    public record Total(String name, BigDecimal total) implements Stat {
        /**
         * Returns the total as a double
         * @return the total
         */
        double asDouble() {
            return total.doubleValue();
        }
    }

    /**
     * Adds the provided items to the list
     *
     * @param items The items to add
     */
    protected TotalsList(final List<I> items) {
        super(items);
    }
}
