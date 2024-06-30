package com.szaumoor.rumple.model.interfaces;

public interface Calculable<R extends Number> extends Formattable<String> {
    R calcTotal();
}
