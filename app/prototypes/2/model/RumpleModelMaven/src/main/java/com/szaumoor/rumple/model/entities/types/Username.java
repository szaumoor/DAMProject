package com.szaumoor.rumple.model.entities.types;

import com.szaumoor.rumple.model.interfaces.UserPrimaryField;
import com.szaumoor.rumple.utils.Strings;

import java.util.Objects;
import java.util.Optional;

public record Username(String username) implements UserPrimaryField {
    public static final int MIN_SIZE = 3;
    public static final int MAX_SIZE = 30;

    /**
     * Constructor for a username. Does no validation of any sort and should only be used to construct objects
     * when it's certain that they are valid, such as when pulling them from a database where insertions were previously
     * validated.
     *
     * @param username The username of a user;
     */
    public Username {
    }

    public static Optional<Username> of(final String username) {
        if (!Strings.hasContent(username) || !Strings.between(username, MIN_SIZE, MAX_SIZE)) return Optional.empty();
        return Optional.of(new Username(username));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Username username1 = (Username) o;
        return Objects.equals(username, username1.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
