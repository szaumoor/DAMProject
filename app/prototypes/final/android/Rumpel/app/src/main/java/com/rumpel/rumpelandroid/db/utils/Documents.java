package com.rumpel.rumpelandroid.db.utils;

import com.rumpel.rumpelandroid.db.DAOUser;
import com.szaumoor.rumple.db.utils.Pair;
import com.szaumoor.rumple.db.utils.Trio;
import com.szaumoor.rumple.model.entities.*;
import com.szaumoor.rumple.model.entities.types.*;
import com.szaumoor.rumple.model.interfaces.Entity;
import com.szaumoor.rumple.utils.Dates;
import org.bson.BsonObjectId;
import org.bson.Document;
import org.bson.types.Decimal128;
import org.bson.types.ObjectId;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

import static com.rumpel.rumpelandroid.db.utils.Filters.and;
import static com.rumpel.rumpelandroid.db.utils.Filters.gte;
import static com.rumpel.rumpelandroid.db.utils.Filters.lt;
import static com.szaumoor.rumple.model.entities.Budget.*;
import static com.szaumoor.rumple.model.entities.User.*;

/**
 * Utility class for the various functions to convert between documents and entities and vice versa
 */
public final class Documents {
    /**
     * Private constructor to prevent instantiation
     */
    private Documents() {
        throw new AssertionError("Utility class");
    }
    /**
     * Parses a Document object and returns an Optional<User> object.
     *
     * @param doc the Document object to be parsed
     * @return an Optional<User> object containing the parsed User data if the Document is not null, otherwise an empty Optional
     */
    public static Optional<User> parseUser(final Document doc) {
        if (Objects.isNull(doc)) return Optional.empty();

        final var userId = new BsonObjectId(doc.getObjectId(ID_FIELD));
        final var username = new Username(doc.getString(USERNAME_FIELD));
        final var email = new UserEmail(doc.getString(EMAIL_FIELD));
        final var password = new UserPass(doc.getString(PASS_FIELD));

        final User user = new User(username, email, password);
        user.setId(userId);

        return Optional.of(user);
    }
    /**
     * Converts a User object to a Document object.
     *
     * @param user the User object to be converted
     * @return an Optional containing the converted Document object, or an empty Optional if the input User is null
     */
    public static Optional<Document> userToDocument(final User user) {
        if (Objects.isNull(user)) return Optional.empty();

        final var id = user.getId();
        final var username = user.getUsername().value();
        final var email = user.getEmail().value();
        final var password = user.getUserPass().getHashedPass();

        var doc = new Document();
        if (id != null) doc.put(ID_FIELD, id);
        doc.put(USERNAME_FIELD, username);
        doc.put(EMAIL_FIELD, email);
        doc.put(PASS_FIELD, password);

        return Optional.of(doc);
    }
    /**
     * Parses a tag from a document.
     *
     * @param doc the document to parse the tag from
     * @return an optional Tag object representing the parsed tag, or an empty optional if the document is null
     */
    public static Optional<Tag> parseTag(final Document doc) {
        if (Objects.isNull(doc)) return Optional.empty();
        var tag = new Tag(doc.getString(Tag.NAME_FIELD));
        tag.setUser(DAOUser.getLoggedUser());
        tag.setId(new BsonObjectId(doc.getObjectId(ID_FIELD)));
        return Optional.of(tag);
    }
    /**
     * Parses a Document object to create a PaymentMethod object.
     *
     * @param doc the Document object to be parsed
     * @return an Optional containing the parsed PaymentMethod object, or an empty Optional if the input Document is null
     */
    public static Optional<PaymentMethod> parsePaymentMethod(final Document doc) {
        if (Objects.isNull(doc)) return Optional.empty();
        var pm = new PaymentMethod(doc.getString(Tag.NAME_FIELD));
        pm.setUser(DAOUser.getLoggedUser());
        pm.setId(new BsonObjectId(doc.getObjectId(ID_FIELD)));
        return Optional.of(pm);
    }
    /**
     * Parses a budget document and returns a pair containing an optional Budget object and a BsonObjectId representing the associated
     * payment method. It's up to a secondary database operation to find that object.
     *
     * @param doc the document to be parsed
     * @return a pair containing an optional Budget object and a BsonObjectId
     */
    public static Pair<Optional<Budget>, BsonObjectId> parseBudget(final Document doc) {
        if (Objects.isNull(doc)) return new Pair<>(Optional.empty(), null);

        var id = new BsonObjectId(doc.getObjectId(ID_FIELD));
        var startDate = ZonedDateTime.ofInstant(doc.getDate(START_DATE_FIELD).toInstant(), ZoneId.systemDefault());
        var endDate = ZonedDateTime.ofInstant(doc.getDate(END_DATE_FIELD).toInstant(), ZoneId.systemDefault());
        var currency = Currency.getInstance(doc.getString(Budget.CURRENCY_FIELD));
        var hardLimit = doc.get(HARD_LIMIT_FIELD, Decimal128.class).bigDecimalValue();
        var softLimit = doc.get(SOFT_LIMIT_FIELD, Decimal128.class).bigDecimalValue();
        var pmId = doc.getObjectId(Budget.PM_ID_FIELD);

        var value = new Budget(new Interval(startDate, endDate), new Limit(softLimit, hardLimit, currency),null, DAOUser.getLoggedUser());
        value.setId(id);
        var budget = Optional.of(value);
        return new Pair<>(budget, pmId != null ? new BsonObjectId(pmId) : null);
    }
    /**
     * Parses the given document and returns a Trio object containing the parsed bill information.
     * This Trio object contains the parsed bill (wrapped in an optional), the payment method ID,
     * and a list of pairs containing the parsed item bills with the associated tags.
     * <p>
     * This is an intermediate operation that requires further unpacking through other database queries.
     *
     * @param doc the document to be parsed
     * @return a Trio object containing the parsed bill, the payment method ID, and the list of item bills
     */
    public static Trio<Optional<Bill>, BsonObjectId, List<Pair<ItemBill, List<BsonObjectId>>>> parseBill(final Document doc) {
        if (Objects.isNull(doc)) return new Trio<>(Optional.empty(), null, Collections.emptyList());

        final var id = new BsonObjectId(doc.getObjectId(ID_FIELD));
        final var date = doc.getDate(Bill.DATE_FIELD);
        final var total = doc.get(Bill.TOTAL_FIELD, Decimal128.class).bigDecimalValue();
        final var pmId = new BsonObjectId(doc.getObjectId(Bill.PM_FIELD));
        final var currency = Currency.getInstance(doc.getString(Bill.CURRENCY_FIELD));
        final var items = doc.getList(Bill.ITEMS_FIELD, Document.class)
                .stream()
                .map(Documents::parseItem)
                .map(pair -> {
                    var first = pair.first();
                    var second = pair.second();
                    return new Pair<>(first.orElseThrow(), second);
                })
                .collect(Collectors.toList());
        var bill = new Bill();
        bill.setId(id);
        bill.setCurrency(currency);
        bill.setUser(DAOUser.getLoggedUser());
        bill.setDate(Dates.zonedFromDate(date));
        bill.setTotal(total);
        return new Trio<>(Optional.of(bill), pmId, items);
    }
    /**
     * Parses a Document and returns an Optional ItemBill and a List of BsonObjectIds representing the associated tags.
     *
     * @param doc the Document to be parsed
     * @return a Pair containing an Optional ItemBill and a List of BsonObjectIds of the associated tags
     */
    public static Pair<Optional<ItemBill>, List<BsonObjectId>> parseItem(final Document doc) {
        if (Objects.isNull(doc)) return new Pair<>(Optional.empty(), Collections.emptyList());
        final var name = doc.getString(ItemBill.NAME_FIELD);
        final var price = doc.get(ItemBill.PRICE_FIELD, Decimal128.class).bigDecimalValue();
        final var tags = doc
                .getList(ItemBill.TAGS_FIELD, ObjectId.class)
                .stream()
                .map(BsonObjectId::new)
                .collect(Collectors.toList());
        var item = new ItemBill();
        item.setName(name);
        item.setPrice(price);
        return new Pair<>(Optional.of(item), tags);
    }
    /**
     * Converts a Tag object to a Document object.
     *
     * @param tag the Tag object to be converted
     * @return an Optional containing the converted Document object, or an empty Optional if the input tag is null
     */
    public static Optional<Document> tagToDocument(final Tag tag) {
        if (Objects.isNull(tag)) return Optional.empty();

        final var id = tag.getId();
        final var tagName = tag.getName();
        final var userId = (BsonObjectId) tag.getUser().getId();

        var doc = new Document();
        if (id != null) doc.put(ID_FIELD, id);
        doc.put(Tag.NAME_FIELD, tagName);
        doc.put(USER_ID_FIELD, userId);

        return Optional.of(doc);
    }
    /**
     * Generates a document from a PaymentMethod object.
     *
     * @param pm The PaymentMethod object to convert to a document.
     * @return An Optional<Document> object representing the converted document.
     */
    public static Optional<Document> paymentMethodToDocument(final PaymentMethod pm) {
        if (Objects.isNull(pm)) return Optional.empty();

        final var id = pm.getId();
        final var tagName = pm.getName();
        final var userId =  pm.getUser().getId();

        var doc = new Document();
        if (id != null) doc.put(ID_FIELD, id);
        doc.put(PaymentMethod.NAME_FIELD, tagName);
        doc.put(USER_ID_FIELD, userId);

        return Optional.of(doc);
    }
    /**
     * Converts a Bill object to a Document object and returns it as an Optional.
     *
     * @param bill the Bill object to be converted
     * @return an Optional containing the converted Document object, or an empty Optional if the input Bill is null
     */
    public static Optional<Document> billToDocument(final Bill bill) {
        if (Objects.isNull(bill)) return Optional.empty();

        var id = bill.getId();
        var date = bill.getDate();
        var items = bill.stream()
                .map(Documents::itemToDocument)
                .map(Optional::orElseThrow)
                .collect(Collectors.toList());
        var total = new Decimal128(bill.getTotal());
        var userId = (BsonObjectId) bill.getUser().getId();
        var paymentMethodId = (BsonObjectId) bill.getPaymentMethod().getId();
        var currency = bill.getCurrency();
        var doc = new Document();
        if (id != null) doc.put(ID_FIELD, id);
        doc.append(Bill.DATE_FIELD, Dates.fromZonedDate(date))
                .append(Bill.ITEMS_FIELD, items)
                .append(Bill.TOTAL_FIELD, total)
                .append(Bill.USER_ID_FIELD, userId)
                .append(Bill.PM_FIELD, paymentMethodId)
                .append(Bill.CURRENCY_FIELD, currency.toString());
        return Optional.of(doc);
    }
    /**
     * Converts an ItemBill object to a Document object.
     *
     * @param item the ItemBill object to be converted
     * @return an Optional<Document> object representing the converted ItemBill object,
     * or an empty Optional if the item is null
     */
    public static Optional<Document> itemToDocument(final ItemBill item) {
        if (Objects.isNull(item)) return Optional.empty();

        var name = item.getName();
        var price = new Decimal128(item.getPrice());
        var tags = item.stream()
                .map(tag -> (BsonObjectId) tag.getId())
                .collect(Collectors.toList());

        return Optional.of(new Document(ItemBill.NAME_FIELD, name)
                .append(ItemBill.PRICE_FIELD, price)
                .append(ItemBill.TAGS_FIELD, tags));
    }
    /**
     * Converts a Budget object to a Document representation.
     *
     * @param budget the Budget object to be converted
     * @return an Optional<Document> representing the converted Budget object, or Optional.empty() if the input is null
     */
    public static Optional<Document> budgetToDocument(final Budget budget) {
        if (Objects.isNull(budget)) return Optional.empty();
        final var id = budget.getId();
        final var startDate = budget.getInterval().startDate();
        final var endDate = budget.getInterval().endDate();
        final var currency = budget.getLimit().currency();
        final var softLimit = new Decimal128(budget.getLimit().softLimit());
        final var hardLimit = new Decimal128(budget.getLimit().hardLimit());
        final var pmId = budget.getPaymentMethod() != null ? (BsonObjectId) budget.getPaymentMethod().getId() : null;

        final var doc = new Document();

        if (id != null) doc.put(ID_FIELD, id);
        doc.put(START_DATE_FIELD, Date.from(startDate.toInstant()));
        doc.put(END_DATE_FIELD, Date.from(endDate.toInstant()));
        doc.put(Budget.CURRENCY_FIELD, currency.toString());
        doc.put(SOFT_LIMIT_FIELD, softLimit);
        doc.put(HARD_LIMIT_FIELD, hardLimit);
        doc.put(Budget.PM_ID_FIELD, pmId);
        doc.put(Entity.USER_ID_FIELD, DAOUser.getLoggedUser().getId());

        return Optional.of(doc);
    }
    /**
     * Generates a Bson filter query for documents that have a date in the specified month and year.
     *
     * @param year the year
     * @param month the month
     * @param field the field to filter on
     * @return the filter
     */
    public static Document monthAndYearToFilter(final Year year, final Month month, final String field) {
        var date = Dates.fromLocalDate(LocalDate.of(year.getValue(), month.getValue(), 1));
        int fixedYear = month == Month.DECEMBER ? year.getValue() + 1 : year.getValue();
        return and(gte(field, date), lt(field, Dates.fromLocalDate(LocalDate.of(fixedYear, month.plus(1), 1))));
    }
    /**
     * Generates a Bson filter query for documents that have a date in the specified year.
     *
     * @param year the year
     * @param field the field to filter on
     * @return the filter
     */
    public static Document yearToFilter(final Year year, final String field) {
        var localDate = LocalDate.of(year.getValue(), 1, 1);
        return and(gte(field, Dates.fromLocalDate(localDate)), lt(field, Dates.fromLocalDate(localDate.plusYears(1))));
    }
    /**
     * Generates a Bson filter to query for documents with a date greater than or equal to the start of the specified year.
     * <br><br>
     * Unused, too inefficient to query a full year in a free MongoDB cluster. The task is divided in 12 months instead
     *
     * @param year  the year to generate the filter for
     * @param field the name of the field to filter on
     * @return a Bson filter representing the greater than or equal to condition
     */
    public static Document fromYearToFilter(final Year year, String field) {
        return gte(field, Dates.fromLocalDate(LocalDate.of(year.getValue(), 1, 1)));
    }
    /**
     * Generates a filter for a given year interval.
     *
     * @param beginningYear the beginning year of the interval
     * @param endYear       the end year of the interval
     * @param field         the field to filter on
     * @return the filter expression
     */
    public static Document yearIntervalToFilter(final Year beginningYear, final Year endYear, String field) {
        var firstDayBeginningYear = LocalDate.of(beginningYear.getValue(), 1, 1);
        var firstDayEndYear = LocalDate.of(endYear.getValue() + 1, 1, 1);
        return and(gte(field, Dates.fromLocalDate(firstDayBeginningYear)), lt(field, Dates.fromLocalDate(firstDayEndYear)));
    }
}
