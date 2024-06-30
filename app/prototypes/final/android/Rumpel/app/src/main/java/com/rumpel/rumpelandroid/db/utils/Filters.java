package com.rumpel.rumpelandroid.db.utils;

import com.szaumoor.rumple.model.interfaces.Entity;

import org.bson.Document;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Basic implementation of the Filters utility class found in the Java SDK but not in this Realms version to create
 * filters to for the queries.
 */
public final class Filters {
    private Filters() {
        throw new AssertionError("Utility class");
    }

    /**
     * Creates a filter that checks for equality on the ID field
     *
     * @param value the ID value
     * @return the filter
     */
    public static Document eq(final Object value) {
        return new Document(Entity.ID_FIELD, value);
    }
    /**
     * Creates a filter that checks for equality on the passed field
     *
     * @param value the ID value
     * @return the filter
     */
    public static Document eq(final String field, final Object value) {
        return new Document(field, value);
    }

    /**
     * Creates a filter that checks for equality on the passed mappings of fields to values.
     *
     * @param mappings the pairs of fields and values
     * @return the filter
     */
    public static Document eq(final Map<String, Object> mappings) {
        return new Document(mappings);
    }
    /**
     * Creates a filter that checks for a greater than check on the passed field
     *
     * @param value the ID value
     * @return the filter
     */
    public static Document gt(final String field, final Object value) {
        return new Document(field, new Document("$gt", value));
    }
    /**
     * Creates a filter that checks for a greater than or equal to check on the passed field
     *
     * @param value the ID value
     * @return the filter
     */
    public static Document gte(final String field, final Object value) {
        return new Document(field, new Document("$gte", value));
    }
    /**
     * Creates a filter that checks for a less than check on the passed field
     *
     * @param value the ID value
     * @return the filter
     */
    public static Document lt(final String field, final Object value) {
        return new Document(field, new Document("$lt", value));
    }
    /**
     * Creates a filter that checks for a less than or equal to check on the passed field
     *
     * @param value the ID value
     * @return the filter
     */
    public static Document lte(final String field, final Object value) {
        return new Document(field, new Document("$lte", value));
    }
    /**
     * Creates a filter that combined two filters to create a logical AND
     *
     * @param doc1 the first filter
     * @param doc2 the second filter
     * @return the filter
     */
    public static Document and(final Document doc1, final Document doc2) {
        return new Document("$and", List.of(doc1, doc2));
    }
    /**
     * Creates a filter that combined two filters to create a logical OR
     *
     * @param doc1 the first filter
     * @param doc2 the second filter
     * @return the filter
     */
    public static Document or(final Document doc1, final Document doc2) {
        return new Document("$or", List.of(doc1, doc2));
    }
    /**
     * Creates a filter that checks that a field's value is in a set of values
     *
     * @param field the field to check
     * @param values the values to check
     * @return the filter
     */
    public static<T> Document in(final String field, final List<T> values) {
        return new Document(field, new Document("$in", values));
    }
    /**
     * Creates a filter that checks that a field's value is in a set of values, which
     * may be cast from instances of the Object class to another class, if relevant.
     *
     * @param field the field to check
     * @param values the values to check
     * @param clazz the class to cast the values to
     * @return the filter
     */
    public static<T> Document in(final String field, final List<Object> values, final Class<T> clazz) {
        var list = values.stream().map(clazz::cast).collect(Collectors.toList());
        return new Document(field, new Document("$in", list));
    }

    /**
     * Creates a filter that checks that a field's value is <b>not</b> in a set of values
     *
     * @param field the field to check
     * @param values the values to check
     * @return the filter
     */
    public static Document nin(final String field, final Object... values) {
        return new Document(field, new Document("$nin", values));
    }
}
