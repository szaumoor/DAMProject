package com.szaumoor.rumple.model.entities;

import com.szaumoor.rumple.model.interfaces.AbstractEntity;
import com.szaumoor.rumple.model.interfaces.Formattable;
import com.szaumoor.rumple.model.interfaces.Streamable;
import com.szaumoor.rumple.utils.Money;
import com.szaumoor.rumple.utils.Strings;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * This class encapsulates an item within a bill or expense in the context of this application.
 */
public final class ItemBill extends AbstractEntity<Object> implements Streamable<Tag>, Formattable<String> {

    public static final String NAME_FIELD = "name";
    public static final String PRICE_FIELD = "price";
    public static final String TAGS_FIELD = "tags";

    private String name;
    private BigDecimal price;
    private Bill bill;
    private final List<Tag> tags;

    public ItemBill() {
        tags = new ArrayList<>();
    }

    public ItemBill(final ItemBill item) {
        this.name = item.name;
        this.price = item.price;
        this.bill = item.bill;
        this.tags = item.tags;
    }

    public ItemBill(final String name, final BigDecimal price, final Collection<Tag> tags) {
        this();
        this.name = name;
        this.price = price;
        if (tags != null && !tags.isEmpty()) this.tags.addAll(tags);
    }

    /**
     * Constructor for an item of a bill. Does no validation of any sort and should only be used to construct objects
     * when it's certain that they are valid, such as when pulling them from a database where insertions were previously
     * validated.
     *
     * @param name The name of this item.
     * @param price The price of this item.
     * @param bill The bill this item refers to.
     * @param tags The tags this item has.
     */
    public ItemBill(final String name, final BigDecimal price, final Bill bill, final Collection<Tag> tags) {
        this(name, price, tags);
        this.bill = bill;
    }

    /**
     * Static utility method that attempts to construct a full item for a bill with the passed name, price, bill,
     * and (optionally) a list of tags. Null references for any of them (except tags, since an item may have no tags),
     * or an invalid price passed (negative numbers), or an empty name, will make this construction fail.
     * <br><br>
     * Because of these restrictions, the method will return an Optional which may or may not contain a valid object.
     * <br>
     *
     * @param name  The name of this element, which cannot be null or blank
     * @param price The price of this element, which cannot be null or negative
     * @param tags  The tags associated with this element. Can be null.
     * @return An Optional wrapping either a valid object or nothing if it couldn't be built as per the aforementioned
     * restrictions.
     */
    public static Optional<ItemBill> of(final String name, final BigDecimal price, final Collection<Tag> tags) {
        if (!Strings.hasContent(name) || !Money.inRange(price)) return Optional.empty();
        return Optional.of(new ItemBill(name, price, tags));
    }


    /**
     * Getter for this item's price
     *
     * @return The BigDecimal object representing this object's price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * Getter for this item's name
     *
     * @return The String representing this item's name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for this item's parent bill
     *
     * @return The Bill representing this item's name
     */
    public Bill getBill() {
        return bill;
    }

    /**
     * Setter for the price of this item.
     *
     * @param price The new price for this item. May not be null or negative.
     * @return True if it could be set, false otherwise.
     */
    public boolean setPrice(final BigDecimal price) {
        boolean valid = Money.inRange(price);
        if (valid) this.price = price;
        return valid;
    }

    /**
     * Setter for the name of this item.
     *
     * @param name The new name for this item. May not be null or blank.
     * @return True if it could be set, false otherwise.
     */
    public boolean setName(final String name) {
        boolean valid = Strings.hasContent(name);
        if (valid) this.name = name;
        return valid;
    }

    /**
     * Setter for the parent bill of this item.
     *
     * @param bill The new parent bill for this item. May not be null.
     * @return True if it could be set, false otherwise.
     */
    public boolean setBill(final Bill bill) {
        boolean valid = Objects.nonNull(bill);
        if (valid) this.bill = bill;
        return valid;
    }

    /**
     * Stream for the tags associated with this item.
     *
     * @return A Stream of the tags in this item.
     */
    @Override
    public Stream<Tag> stream() {
        return tags.stream();
    }

    @Override
    public boolean add(final Tag tag) {
        return tags.add(tag);
    }

    @Override
    public boolean addAll(Collection<Tag> elements) {
        return tags.addAll(elements);
    }

    @Override
    public Tag getItem(int index) {
        return tags.get(index);
    }

    @Override
    public int size() {
        return tags.size();
    }

    @Override
    public boolean contains(final Tag tag) {
        return tags.contains(tag);
    }

    @Override
    public void forEach(final Consumer<Tag> consumer) {
        tags.forEach(consumer);
    }

    @Override
    public void clear() {
        tags.clear();
    }

    @Override
    public String format() {
        return Money.formatCurrency(price, bill.getCurrency());
    }
}
