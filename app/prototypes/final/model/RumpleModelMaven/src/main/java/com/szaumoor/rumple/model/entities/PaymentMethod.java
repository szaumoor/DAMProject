package com.szaumoor.rumple.model.entities;

import com.szaumoor.rumple.model.interfaces.AbstractEntity;

import java.util.Objects;
import java.util.Optional;

import static com.szaumoor.utils.Strings.hasContent;

/**
 * This class encapsulates a payment method in this application.
 */
public final class PaymentMethod extends AbstractEntity<Object> {

    public static final String COLL_PMS = "payment_methods";
    public static final String NAME_FIELD = "name";

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

    public PaymentMethod(final String name, final User user) {
        this.name = name;
        this.user = user;
    }

    public PaymentMethod(final PaymentMethod method) {
        super(method.getId());
        this.name = method.name;
        this.user = method.user;
    }

    /**
     * Static method to construct a validated object of this class. Do not use the constructor unless there is total
     * certainty the passed values are correct.
     *
     * @param name The name of this payment method. Must not be a null or blank String.
     * @return An Optional containing an object of this class if valid, empty otherwise.
     */
    public static Optional<PaymentMethod> of(final String name) {
        if (!hasContent(name)) return Optional.empty();
        return Optional.of(new PaymentMethod(name));
    }

    public User getUser() {
        return user;
    }

    public String getName() {
        return name;
    }

    public boolean setName(final String name) {
        final boolean valid = hasContent(name);
        if (valid) this.name = name;
        return valid;
    }

    public boolean setUser(final User user) {
        final boolean valid = Objects.nonNull(user);
        if (valid) this.user = user;
        return valid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentMethod that = (PaymentMethod) o;
        return Objects.equals(name, that.name) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, user);
    }

    @Override
    public String toString() {
        return name;
    }
}
