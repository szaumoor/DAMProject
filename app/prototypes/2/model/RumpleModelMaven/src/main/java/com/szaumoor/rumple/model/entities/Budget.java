package com.szaumoor.rumple.model.entities;

import com.szaumoor.rumple.model.interfaces.AbstractEntity;
import com.szaumoor.rumple.utils.Nulls;
import com.szaumoor.rumple.model.entities.types.Interval;
import com.szaumoor.rumple.model.entities.types.Limit;

import java.util.Objects;
import java.util.Optional;

public final class Budget extends AbstractEntity<Object> {

    public static final String COLL_BUDGETS = "budgets";

    public static final String START_DATE_FIELD = "start_date";
    public static final String END_DATE_FIELD = "end_date";
    public static final String HARD_LIMIT_FIELD = "hard_limit";
    public static final String SOFT_LIMIT_FIELD = "soft_limit";
    public static final String CURRENCY_FIELD = "currency";
    public static final String PM_ID_FIELD = "pm_id";

    private Interval interval;
    private Limit limit;
    private PaymentMethod method;
    private User user;


    public Budget() {

    }

    public Budget(final User user) {
        this.user = user;
    }

    /**
     * Constructor for a budget. Does no validation of any sort and should only be used to construct objects
     * when it's certain that they are valid, such as when pulling them from a database where insertions were previously
     * validated.
     *
     * @param interval The interval in which this budget applies.
     * @param limit The monetary limit of this budget.
     * @param method The payment method that may or may not be associated with the budget.
     * @param user The user this budget is associated with.
     */
    public Budget(final Interval interval, final Limit limit, final PaymentMethod method, final User user) {
        this.interval = interval;
        this.limit = limit;
        this.method = method;
        this.user = user;
    }

    public Budget(final Budget budget) {
        super(budget.getId());
        this.interval = budget.interval;
        this.limit = budget.limit;
        this.method = budget.method;
        this.user = budget.user;
    }

    public static Optional<Budget> of(final Interval interval, final Limit limit, final PaymentMethod method, final User user){
        if (Nulls.anyNull(interval, limit, method, user)) return Optional.empty();
        else return Optional.of(new Budget(interval, limit, method, user));
    }

    public Interval getInterval() {
        return interval;
    }

    public Limit getLimit() {
        return limit;
    }

    public PaymentMethod getPaymentMethod() {
        return method;
    }

    public User getUser() {
        return user;
    }

    public boolean setInterval(final Interval interval) {
        boolean valid = Objects.nonNull(interval);
        if (valid) {
            this.interval = interval;
        }
        return valid;
    }

    public boolean setLimit(final Limit limit) {
        boolean valid = Objects.nonNull(limit);
        if (valid) {
            this.limit = limit;
        }
        return valid;
    }

    public boolean setPaymentMethod(final PaymentMethod method) {
        boolean valid = Objects.nonNull(method);
        if (valid) this.method = method;
        return valid;
    }

    public boolean setUser(final User user) {
        boolean valid = Objects.nonNull(user);
        if (valid) this.user = user;
        return valid;
    }

    @Override
    public String toString() {
        return interval + " || " + limit;
    }
}
