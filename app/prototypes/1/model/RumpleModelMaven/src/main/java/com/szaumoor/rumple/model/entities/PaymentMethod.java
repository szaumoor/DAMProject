package com.szaumoor.rumple.model.entities;


import com.szaumoor.rumple.model.interfaces.AbstractEntity;
import com.szaumoor.rumple.model.utils.Strings;

import java.util.Objects;
import java.util.Optional;

/**
 * This class encapsulates
 */
public final class PaymentMethod extends AbstractEntity<Object> {

    private String name;
    private User user;

    /**
     * Constructor for the payment method. Does no validation of any sort and should only be used to construct objects
     * when it's certain that they are valid, such as when pulling them from a database where insertions were previously
     * validated.
     *
     * @param name The String of this payment method.
     */
    public PaymentMethod(final String name) {
        this.name = name;
    }

    /**
     * Static method to construct a validated object of this class. Do not use the constructor unless there is total
     * certainty the passed values are correct.
     *
     * @param name The name of this payment method. Must not be a null or blank String.
     * @return An Optional containing an object of this class if valid, empty otherwise.
     */
    public static Optional<PaymentMethod> of(final String name) {
        if (!Strings.hasContent(name)) return Optional.empty();
        return Optional.of(new PaymentMethod(name));
    }

    public User getUser() {
        return user;
    }

    public String getName() {
        return name;
    }

    public boolean setName(final String name) {
        final boolean valid = Strings.hasContent(name);
        if (valid) this.name = name;
        return valid;
    }

    public boolean setUser(final User user) {
        final boolean valid = Objects.nonNull(user);
        if (valid) this.user = user;
        return valid;
    }

    @Override
    public String toString() {
        return name;
    }
}
