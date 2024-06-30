package com.szaumoor.rumple.model.entities.types;

import com.sanctionco.jmail.JMail;
import com.szaumoor.rumple.model.interfaces.UserPrimaryField;

import java.util.Objects;
import java.util.Optional;

import static com.szaumoor.utils.Strings.hasContent;

/**
 * Record that encapsulates a user email.
 *
 * @param value The email that this class encapsulates. Does no validation
 */
public record UserEmail(String value) implements UserPrimaryField {

    /**
     * Constructor for a user email. Does no validation of any sort and should only be used to construct objects
     * when it's certain that they are valid, such as when pulling them from a database where insertions were previously
     * validated. Use the static of() method otherwise.
     *
     * @param value The email that this class encapsulates
     */
    public UserEmail {
    }

    /**
     * The preferred way to construct a UserEmail object, as it does validation to make sure the email is valid.
     *
     * @param email The email that this class encapsulates
     * @return The UserEmail object wrapped in an optional, which is empty if the email is invalid
     */
    public static Optional<UserEmail> of(final String email) {
        if (!hasContent(email) || !JMail.isValid(email)) return Optional.empty();
        return Optional.of(new UserEmail(email));
    }

    /**
     * Validates an email address.
     *
     * @param email the email address to be validated
     * @return true if the email address is valid, false otherwise
     */
    public static boolean validate(final String email) {
        return JMail.isValid(email);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEmail userEmail = (UserEmail) o;
        return Objects.equals(value, userEmail.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}