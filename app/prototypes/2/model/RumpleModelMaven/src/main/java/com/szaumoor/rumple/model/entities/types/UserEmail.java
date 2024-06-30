package com.szaumoor.rumple.model.entities.types;

import com.sanctionco.jmail.JMail;
import com.szaumoor.rumple.model.interfaces.UserPrimaryField;
import com.szaumoor.rumple.utils.Strings;

import java.util.Objects;
import java.util.Optional;

public record UserEmail(String email) implements UserPrimaryField {

    /**
     * Constructor for a user email. Does no validation of any sort and should only be used to construct objects
     * when it's certain that they are valid, such as when pulling them from a database where insertions were previously
     * validated. Use the static of() method otherwise.
     *
     * @param email The email that this class encapsulates
     */
    public UserEmail {
    }

    public static Optional<UserEmail> of(final String email) {
        if (!Strings.hasContent(email) || !JMail.isValid(email)) return Optional.empty();
        return Optional.of(new UserEmail(email));
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEmail userEmail = (UserEmail) o;
        return Objects.equals(email, userEmail.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}