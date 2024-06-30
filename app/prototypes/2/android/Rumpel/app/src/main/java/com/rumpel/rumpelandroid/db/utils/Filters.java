package com.rumpel.rumpelandroid.db.utils;

import org.bson.Document;

import java.util.Map;

public enum Filters {
    ;

    public static Document eq(Object value) {
        return new Document("_id", value);
    }

    public static Document eq(String field, Object value) {
        return new Document(field, value);
    }

    public static Document eq(Map<String, Object> mappings) {
        return new Document(mappings);
    }

    public static Document gt(String field, Object value) {
        return new Document(field, new Document("$gt", value));
    }

    public static Document gte(String field, Object value) {
        return new Document(field, new Document("$gte", value));
    }

    public static Document lt(String field, Object value) {
        return new Document(field, new Document("$lt", value));
    }

    public static Document lte(String field, Object value) {
        return new Document(field, new Document("$lte", value));
    }

    public static Document and(final Document doc1, final Document doc2) {
        final Document doc = new Document();
        doc.putAll(doc1);
        doc.putAll(doc2);
        return doc;
    }

}
