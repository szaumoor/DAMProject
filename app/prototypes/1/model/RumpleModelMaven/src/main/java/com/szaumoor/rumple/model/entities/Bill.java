package com.szaumoor.rumple.model.entities;

import com.szaumoor.rumple.model.interfaces.Calculable;
import com.szaumoor.rumple.model.interfaces.AbstractEntity;
import com.szaumoor.rumple.model.interfaces.Streamable;
import com.szaumoor.rumple.model.utils.Dates;
import com.szaumoor.rumple.model.utils.Money;
import com.szaumoor.rumple.model.utils.Types;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Stream;

public final class Bill extends AbstractEntity<Long> implements Streamable<ItemBill>, Calculable<BigDecimal> {

    private ZonedDateTime date;

    private User user;
    private Currency currency;
    private BigDecimal total;
    private PaymentMethod paymentMethod;

    private final List<ItemBill> items;

    public Bill() {
        date = ZonedDateTime.now();
        total = BigDecimal.ZERO;
        items = new ArrayList<>(10);
    }

    public Bill(final Currency currency, final PaymentMethod paymentMethod) {
        this();
        this.currency = currency;
        this.paymentMethod = paymentMethod;
    }

    private Bill(final Currency curr, final PaymentMethod method, final List<ItemBill> items) {
        this();
        this.currency = curr;
        this.paymentMethod = method;
        if (items != null) this.items.addAll(items);
    }

    public static Optional<Bill> of(final Currency curr, final PaymentMethod method, final List<ItemBill> items) {
        if (Types.anyNull(curr, method, items) || items.isEmpty()) return Optional.empty();
        final Bill value = new Bill(curr, method, items);
        items.forEach(itemBill -> itemBill.setBill(value));
        return Optional.of(value);
    }

    public User getUser() {
        return user;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public Currency getCurrency() {
        return currency;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public boolean setUser(final User user) {
        final boolean valid = Objects.nonNull(user);
        if (valid) this.user = user;
        return valid;
    }

    public boolean setDate(final ZonedDateTime date) {
        final boolean valid = Objects.nonNull(date) && Dates.notFuture(date);
        if (valid) this.date = date;
        return valid;
    }

    public boolean setCurrency(final Currency currency) {
        final boolean valid = Types.nonNull(currency);
        if (valid) this.currency = currency;
        return valid;
    }

    public boolean setPaymentMethod(final PaymentMethod paymentMethod) {
        final boolean valid = Types.nonNull(paymentMethod);
        if (valid) this.paymentMethod = paymentMethod;
        return valid;
    }

    /**
     * Calculates the total price of this bill according to the price of each, sets the
     * total field to it, and returns the result.
     *
     * @return BigDecimal representing the sum total of the price of all items in the bill.
     */
    @Override
    public BigDecimal calcTotal() {
        this.total = stream().map(ItemBill::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        return this.total;
    }

    @Override
    public String format() {
        return Money.formatCurrency(total, currency);
    }



    /**
     * Stream for the items associated with this bill.
     *
     * @return A Stream of the items in this item.
     */
    @Override
    public Stream<ItemBill> stream() {
        return items.stream();
    }

    @Override
    public boolean addAll(Collection<ItemBill> elements) {
        return items.addAll(elements);
    }

    @Override
    public ItemBill getItem(final int index) {
        return items.get(index);
    }

    @Override
    public int size() {
        return items.size();
    }


}
