package com.szaumoor.rumple.model.entities;

import com.szaumoor.rumple.model.interfaces.Calculable;
import com.szaumoor.rumple.model.interfaces.AbstractEntity;
import com.szaumoor.rumple.model.interfaces.Streamable;
import com.szaumoor.rumple.utils.Dates;
import com.szaumoor.rumple.utils.Money;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static com.szaumoor.utils.Nulls.anyNull;


public final class Bill extends AbstractEntity<Object> implements Streamable<ItemBill>, Calculable<BigDecimal> {

    public static final String COLL_BILLS = "bills";

    public static final String DATE_FIELD = "date";
    public static final String CURRENCY_FIELD = "currency";
    public static final String TOTAL_FIELD = "total";
    public static final String PM_FIELD = "pm_id";
    public static final String ITEMS_FIELD = "items";

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

    public Bill(final Bill bill) {
        super(bill.getId());
        this.date = bill.date;
        this.user = bill.user;
        this.currency = bill.currency;
        this.total = bill.total;
        this.paymentMethod = bill.paymentMethod;
        this.items = bill.items;
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
        if (anyNull(curr, method, items) || items.isEmpty()) return Optional.empty();
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

    @Override
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
        final boolean valid = Objects.nonNull(date) && Dates.pastOrPresent(date);
        if (valid) this.date = date;
        return valid;
    }

    public boolean setCurrency(final Currency currency) {
        final boolean valid = Objects.nonNull(currency);
        if (valid) this.currency = currency;
        return valid;
    }

    public boolean setPaymentMethod(final PaymentMethod paymentMethod) {
        final boolean valid = Objects.nonNull(paymentMethod);
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

    public boolean setTotal(final BigDecimal total) {
        final boolean valid = Money.inRange(total);
        if (valid) this.total = total;
        return valid;
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
    public boolean add(final ItemBill item) {
        return items.add(item);
    }

    @Override
    public boolean addAll(final Collection<ItemBill> elements) {
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

    @Override
    public boolean contains(final ItemBill item) {
        return items.contains(item);
    }

    @Override
    public void forEach(final Consumer<ItemBill> consumer) {
        items.forEach(consumer);
    }

    @Override
    public void clear() {
        items.clear();
    }

    public static BigDecimal sum(final Bill ... bills) {
        return Arrays.stream(bills)
                .map(Bill::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bill bill = (Bill) o;
        return Objects.equals(getId(), bill.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
