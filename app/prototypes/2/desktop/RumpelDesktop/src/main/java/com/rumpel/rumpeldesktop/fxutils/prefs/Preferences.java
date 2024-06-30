package com.rumpel.rumpeldesktop.fxutils.prefs;

import com.rumpel.rumpeldesktop.App;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public final class Preferences {
    private static List<Category> categories = new ArrayList<>(20);

    public static final class Category {
        Map<String, String> mappings;
        String name;

        Category(String name) {
            this.name = name;
            mappings = new HashMap<>(10);
        }

        Category(String name, Map<String, String> mappings) {
            this.mappings = mappings;
            this.name = name;
        }

        public String get(final String key) {
            return mappings.get(key);
        }

        public String put(final String key, final String value) {
            return mappings.put(key, value);
        }

        @Override
        public String toString() {
            return name + "{" + mappings + "}";
        }
    }

    public static void parsePreferences() {
        try (var stream = Files.lines(Paths.get(App.class.getResource("prefs.ini").getPath().substring(3)))) {
            var preferences = stream.toList();
            var cat = new AtomicReference<Category>();
            preferences.forEach(s -> {
                if (s.startsWith("[")) {
                    cat.set(new Category(s));
                    categories.add(cat.get());
                }
                else {
                    var values = s.split("\\s?=\\s?");
                    cat.get().put(values[0], values[1]);
                }
            });

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void printOut() {
        System.out.println(categories);
    }

    public static void readFirstFromSecond() {
        System.out.println(categories.get(1));
    }

    public static Currency getPreferredCurrency() {
        return null;
    }

}
