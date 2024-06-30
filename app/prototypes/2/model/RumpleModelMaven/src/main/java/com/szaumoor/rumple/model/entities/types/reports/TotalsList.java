package com.szaumoor.rumple.model.entities.types.reports;

import com.szaumoor.rumple.model.interfaces.Entity;

import java.math.BigDecimal;
import java.util.List;

public abstract class TotalsList<I extends Entity> extends StatList<TotalsList.Total, I> {
    public record Total(String name, BigDecimal total) implements Stat {
        double asDouble() {
            return total.doubleValue();
        }
    }

    protected TotalsList(final List<I> items) {
        super(items);
    }
}
