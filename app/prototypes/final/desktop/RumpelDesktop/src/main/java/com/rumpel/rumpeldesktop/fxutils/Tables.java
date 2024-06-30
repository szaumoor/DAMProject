package com.rumpel.rumpeldesktop.fxutils;


import com.szaumoor.rumple.utils.Money;
import javafx.scene.control.TableColumn;

import java.util.OptionalInt;
import java.util.concurrent.atomic.AtomicReference;

/**
 *  Utility class for setting up TableViews in FX
 */
public final class Tables {
    /**
     * Private constructor to prevent instantiation
     */
    private Tables() {
        throw new AssertionError("Utility classes cannot be instantiated");
    }

    /**
     * Sorts the given TableColumn by money amount. It expects the column to contain money values.
     * Not working very well.
     *
     * @param  column    the TableColumn to be sorted
     */
    public static void sortByMoneyAmount(final TableColumn<?, String> column) {
        column.setComparator((o1, o2) -> {
            final AtomicReference<OptionalInt> comparison = new AtomicReference<>(OptionalInt.empty());
            Money.unformatCurrency(o1).ifPresent(money1 -> Money.unformatCurrency(o2).ifPresent(money2 -> {
                comparison.set(OptionalInt.of(money1.compareTo(money2)));
            }));
            var optional = comparison.get();
            return optional.isPresent() ? optional.getAsInt() : o1.compareTo(o2);
        });
    }
}
