package com.szaumoor.rumple.model.entities.types;

import com.szaumoor.rumple.model.interfaces.UserPrimaryField;
import com.szaumoor.utils.Strings;

import java.util.Objects;
import java.util.Optional;

import static com.szaumoor.utils.Strings.hasContent;

/**
 * Record that encapsulates a username
 *
 * @param value The username. Does no validation of any sort, and should only be
 *              used to construct objects when it's certain that they are valid.
 */
public record Username(String value) implements UserPrimaryField {
    public static final int MIN_SIZE = 3;
    public static final int MAX_SIZE = 30;

    /**
     * Constructor for a username. Does no validation of any sort and should only be used to construct objects
     * when it's certain that they are valid, such as when pulling them from a database where insertions were previously
     * validated.
     *
     * @param value The username of a user;
     */
    public Username {
    }

    /**
     * The preferred way to construct a Username object, as it does validation to make sure it is valid.
     *
     * @param username The username that this class encapsulates
     * @return The Username object wrapped in an optional, which is empty if the username is invalid
     */
    public static Optional<Username> of(final String username) {
        if (!hasContent(username) || !Strings.between(username, MIN_SIZE, MAX_SIZE)) return Optional.empty();
        return Optional.of(new Username(username));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Username username1 = (Username) o;
        return Objects.equals(value, username1.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
