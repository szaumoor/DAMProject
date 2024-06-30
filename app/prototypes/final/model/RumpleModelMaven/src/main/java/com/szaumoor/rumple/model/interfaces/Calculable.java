package com.szaumoor.rumple.model.interfaces;

/**
 * Interface for entities that can be calculated in some way
 * @param <R>
 */
public interface Calculable<R extends Number> extends Formattable<String> {
    R calcTotal();
    R getTotal();
}
