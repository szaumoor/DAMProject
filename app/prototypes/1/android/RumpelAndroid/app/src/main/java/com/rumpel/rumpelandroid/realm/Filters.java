package com.rumpel.rumpelandroid.realm;

import org.bson.Document;

import java.util.HashMap;
import java.util.Map;

public enum Filters {
    ;

    public static Document eq(String field, Object value) {
        return new Document(field, value);
    }

    public static Document eq(Map<String, Object> mappings) {
        return new Document(mappings);
    }

    public static Document and(final Document doc1, final Document doc2) {
        final Document doc = new Document();
        doc.putAll(doc1);
        doc.putAll(doc2);
        return doc;
    }

}
