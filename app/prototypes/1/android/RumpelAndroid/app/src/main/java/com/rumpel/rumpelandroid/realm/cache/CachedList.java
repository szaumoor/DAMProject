package com.rumpel.rumpelandroid.realm.cache;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.szaumoor.rumple.model.interfaces.AbstractEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class CachedList<T extends AbstractEntity<Object>> {
    private final List<T> list;

    public CachedList() {
        list = new ArrayList<>();
    }

    public CachedList(final List<T> list) {
        this.list = list;
    }

    public int size() {
        return list.size();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public boolean add(T t) {
        return list.add(t);
    }

    public boolean remove(@Nullable Object o) {
        return list.remove(o);
    }

    public boolean addAll(@NonNull Collection<? extends T> c) {
        return list.addAll(c);
    }


    public Stream<T> stream() {
        return list.stream();
    }
}
