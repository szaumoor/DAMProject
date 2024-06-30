package com.szaumoor.rumple.model.entities.types.reports;

import com.szaumoor.rumple.model.interfaces.Entity;
import com.szaumoor.rumple.model.interfaces.Streamable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public abstract class StatList<T extends Stat, I extends Entity> implements Streamable<I> {
    protected StatList(final List<I> items) {
        stats = new ArrayList<>(20);
        this.items = items;
    }

    protected final List<T> stats;
    protected final List<I> items;

    public final List<T> getStats() {
        return stats;
    }

    protected abstract void calculate();

    @Override
    public final Stream<I> stream() {
        return items.stream();
    }

    @Override
    public final boolean add(I item) {
        return items.add(item);
    }

    @Override
    public final boolean addAll(Collection<I> elements) {
        return items.addAll(elements);
    }

    @Override
    public final I getItem(int index) {
        return items.get(index);
    }

    @Override
    public final int size() {
        return items.size();
    }

    @Override
    public final boolean contains(I object) {
        return items.contains(object);
    }

    @Override
    public final void forEach(Consumer<I> consumer) {
        items.forEach(consumer);
    }

    @Override
    public final void clear() {
        items.clear();
    }
}
