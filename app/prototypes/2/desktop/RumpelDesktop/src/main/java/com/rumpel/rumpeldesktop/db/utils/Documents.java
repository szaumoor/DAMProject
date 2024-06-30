package com.rumpel.rumpeldesktop.db.utils;

import com.rumpel.rumpeldesktop.db.DAOUser;
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

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

import static com.szaumoor.rumple.model.entities.Budget.*;
import static com.szaumoor.rumple.model.entities.User.*;

public enum Documents {
    ;

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

    public static Optional<Tag> parseTag(final Document doc) {
        if (Objects.isNull(doc)) return Optional.empty();
        var tag = new Tag(doc.getString(Tag.NAME_FIELD));
        tag.setUser(DAOUser.getLoggedUser());
        tag.setId(new BsonObjectId(doc.getObjectId(ID_FIELD)));
        return Optional.of(tag);
    }

    public static Optional<PaymentMethod> parsePaymentMethod(final Document doc) {
        if (Objects.isNull(doc)) return Optional.empty();
        var pm = new PaymentMethod(doc.getString(Tag.NAME_FIELD));
        pm.setUser(DAOUser.getLoggedUser());
        pm.setId(new BsonObjectId(doc.getObjectId(ID_FIELD)));
        return Optional.of(pm);
    }

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

    public static Trio<Optional<Bill>, BsonObjectId, List<Pair<ItemBill, List<BsonObjectId>>>> parseBill(final Document doc) {
        if (Objects.isNull(doc)) return new Trio<>(Optional.empty(), null, Collections.emptyList());

        final var id = new BsonObjectId(doc.getObjectId(ID_FIELD));
        final var date = doc.getDate(Bill.DATE_FIELD);
        final var total = doc.get(Bill.TOTAL_FIELD, Decimal128.class).bigDecimalValue();
        final var pmId = new BsonObjectId(doc.getObjectId(Bill.PM_FIELD));
        final var currency = Currency.getInstance(doc.getString(Bill.CURRENCY_FIELD));
        final var items = doc
                .getList(Bill.ITEMS_FIELD, Document.class)
                .stream()
                .map(Documents::parseItem)
                .map(pair -> {
                    var first = pair.first();
                    var second = pair.second();
                    return new Pair<>(first.orElseThrow(), second);
                })
                .toList();
        var bill = new Bill();
        bill.setId(id);
        bill.setCurrency(currency);
        bill.setUser(DAOUser.getLoggedUser());
        bill.setDate(Dates.zonedFromDate(date));
        bill.setTotal(total);
        return new Trio<>(Optional.of(bill), pmId, items);
    }

    public static Pair<Optional<ItemBill>, List<BsonObjectId>> parseItem(final Document doc) {
        if (Objects.isNull(doc)) return new Pair<>(Optional.empty(), Collections.emptyList());
        final var name = doc.getString(ItemBill.NAME_FIELD);
        final var price = doc.get(ItemBill.PRICE_FIELD, Decimal128.class).bigDecimalValue();
        final var tags = doc
                .getList(ItemBill.TAGS_FIELD, ObjectId.class)
                .stream()
                .map(BsonObjectId::new)
                .toList();
        var item = new ItemBill();
        item.setName(name);
        item.setPrice(price);
        return new Pair<>(Optional.of(item), tags);
    }

    public static Optional<Document> userToDocument(final User user) {
        if (Objects.isNull(user)) return Optional.empty();

        final var id = user.getId();
        final var username = user.getUsername().username();
        final var email = user.getEmail().email();
        final var password = user.getUserPass().getHashedPass();

        var doc = new Document();
        if (id != null) doc.put(ID_FIELD, id);
        doc.put(USERNAME_FIELD, username);
        doc.put(EMAIL_FIELD, email);
        doc.put(PASS_FIELD, password);

        return Optional.of(doc);
    }

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

    public static Optional<Document> paymentMethodToDocument(final PaymentMethod pm) {
        if (Objects.isNull(pm)) return Optional.empty();

        final var id = pm.getId();
        final var tagName = pm.getName();
        final var userId = (BsonObjectId) pm.getUser().getId();

        var doc = new Document();
        if (id != null) doc.put(ID_FIELD, id);
        doc.put(PaymentMethod.NAME_FIELD, tagName);
        doc.put(USER_ID_FIELD, userId);

        return Optional.of(doc);
    }

    public static Optional<Document> billToDocument(final Bill bill) {
        if (Objects.isNull(bill)) return Optional.empty();

        var id = bill.getId();
        var date = bill.getDate();
        var items = bill.stream()
                .map(Documents::itemToDocument)
                .map(Optional::orElseThrow)
                .toList();
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

    public static Optional<Document> itemToDocument(final ItemBill item) {
        if (Objects.isNull(item)) return Optional.empty();

        var name = item.getName();
        var price = new Decimal128(item.getPrice());
        var tags = item.stream()
                .map(tag -> (BsonObjectId) tag.getId())
                .toList();

        return Optional.of(new Document(ItemBill.NAME_FIELD, name)
                .append(ItemBill.PRICE_FIELD, price)
                .append(ItemBill.TAGS_FIELD, tags));
    }

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
}
