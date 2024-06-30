package com.rumpel.rumpelandroid.utils;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Helper class with generic operations with collections of elements.
 */
public enum CollectionsUtils {
    ;
    /**
     * Passes elements from an array into a set of the same type according
     * to a predicate. Null predicates do nothing.
     *
     * @param condition Condition to filter the array contents for.
     * @param array     The array of elements to inject into the next set.
     * @param <T>       Type of elements that will be dealt with.
     * @return Synchronized Set of elements of the array after being filtered. This set
     * does not accept nulls.
     */
    public static <T> Set<T> fromArrayToSet(final Predicate<T> condition, final T... array) {
        return Stream.of(array)
                .filter(condition == null ? t -> true : condition)
                .collect(Collectors.toCollection(() -> Collections.synchronizedSet(new LinkedHashSet<>())));
    }

    /**
     * Passes elements from an one collection into another collection of the same type according
     * to a predicate. Null predicates do nothing.
     *
     * @param target    The target collection.
     * @param source    The source collection to inject into the target.
     * @param condition Condition to filter the source collection elements with.
     * @param <T>       Type of elements that will be dealt with.
     * @return True if the elements were added correctly and the collections are not null, false otherwise.
     * @throws NullPointerException if the passed collections are null
     */
    public static <T> boolean intoSetFiltered(final Collection<T> target, final Collection<T> source, final Predicate<T> condition) {
        if (target == null || source == null) throw new NullPointerException("Some of the collections used were null");
        final List<T> filteredList = source.stream()
                .filter(condition != null ? condition : t -> true)
                .collect(Collectors.toUnmodifiableList());
        return target.addAll(filteredList);
    }
}
