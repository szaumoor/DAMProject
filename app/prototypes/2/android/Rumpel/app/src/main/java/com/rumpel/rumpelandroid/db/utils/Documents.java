package com.rumpel.rumpelandroid.db.utils;

import static com.szaumoor.rumple.model.entities.Budget.END_DATE_FIELD;
import static com.szaumoor.rumple.model.entities.Budget.HARD_LIMIT_FIELD;
import static com.szaumoor.rumple.model.entities.Budget.SOFT_LIMIT_FIELD;
import static com.szaumoor.rumple.model.entities.Budget.START_DATE_FIELD;
import static com.szaumoor.rumple.model.entities.User.EMAIL_FIELD;
import static com.szaumoor.rumple.model.entities.User.PASS_FIELD;
import static com.szaumoor.rumple.model.entities.User.USERNAME_FIELD;

import com.rumpel.rumpelandroid.db.DAOUser;
import com.szaumoor.rumple.db.BaseDAO;
import com.szaumoor.rumple.db.utils.Pair;
import com.szaumoor.rumple.model.entities.Budget;
import com.szaumoor.rumple.model.entities.PaymentMethod;
import com.szaumoor.rumple.model.entities.Tag;
import com.szaumoor.rumple.model.entities.User;
import com.szaumoor.rumple.model.entities.types.Interval;
import com.szaumoor.rumple.model.entities.types.Limit;
import com.szaumoor.rumple.model.entities.types.UserEmail;
import com.szaumoor.rumple.model.entities.types.UserPass;
import com.szaumoor.rumple.model.entities.types.Username;
import com.szaumoor.rumple.model.interfaces.Entity;
import com.szaumoor.rumple.utils.Dates;

import org.bson.BsonObjectId;
import org.bson.Document;
import org.bson.types.Decimal128;
import org.bson.types.ObjectId;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Currency;
import java.util.Objects;
import java.util.Optional;

public enum Documents {
    ;

    public static Optional<Document> userToDocument(final User user) {
        if (Objects.isNull(user)) return Optional.empty();

        final var username = user.getUsername().username();
        final var email = user.getEmail().email();
        final var password = user.getUserPass().getHashedPass();

        final Document doc = new Document();
        doc.put(USERNAME_FIELD, username);
        doc.put(EMAIL_FIELD, email);
        doc.put(PASS_FIELD, password);

        System.out.println(doc);

        return Optional.of(doc);
    }

    public static Optional<Document> tagToDocument(final Tag tag) {
        if (Objects.isNull(tag)) return Optional.empty();

        var id = (BsonObjectId) tag.getId();
        final var name = tag.getName();
        final var userId = (BsonObjectId) tag.getUser().getId();

        final Document doc = new Document();
        if (id != null) doc.put(Entity.ID_FIELD, id);
        doc.put(Tag.NAME_FIELD, name);
        doc.put(Entity.USER_ID_FIELD, userId);

        return Optional.of(doc);
    }

    public static Optional<Document> paymentMethodToDocument(final PaymentMethod pm) {
        if (Objects.isNull(pm)) return Optional.empty();

        var id = (BsonObjectId) pm.getId();
        final var name = pm.getName();
        final var userId = (BsonObjectId) pm.getUser().getId();

        final Document doc = new Document();
        if (id != null) doc.put(Entity.ID_FIELD, id);
        doc.put(PaymentMethod.NAME_FIELD, name);
        doc.put(Entity.USER_ID_FIELD, userId);

        return Optional.of(doc);
    }

    public static Optional<Document> budgetToDocument(final Budget budget) {
        if (Objects.isNull(budget)) return Optional.empty();

        var id = (BsonObjectId) budget.getId();
        final var startDate = budget.getInterval().startDate();
        final var endDate = budget.getInterval().endDate();
        final var currency = budget.getLimit().currency();
        final var softLimit = new Decimal128(budget.getLimit().softLimit());
        final var hardLimit = new Decimal128(budget.getLimit().hardLimit());
        final var pmId = (BsonObjectId) budget.getPaymentMethod().getId();
        final var userId = (BsonObjectId) budget.getUser().getId();

        final Document doc = new Document();
        if (id != null) doc.put(Entity.ID_FIELD, id);
        doc.put(START_DATE_FIELD, Dates.fromZonedDate(startDate));
        doc.put(END_DATE_FIELD, Dates.fromZonedDate(endDate));
        doc.put(Budget.CURRENCY_FIELD, currency.getCurrencyCode());
        doc.put(SOFT_LIMIT_FIELD, softLimit);
        doc.put(HARD_LIMIT_FIELD, hardLimit);
        doc.put(Budget.PM_ID_FIELD, pmId);
        doc.put(Entity.USER_ID_FIELD, userId);

        return Optional.of(doc);
    }

    public static Tag toTag(final Document doc) {
        var name = doc.getString(Tag.NAME_FIELD);
        var tag = new Tag(name);
        tag.setId(new BsonObjectId(doc.getObjectId(Entity.ID_FIELD)));
        tag.setUser(DAOUser.getLoggedUser());
        return tag;
    }

    public static PaymentMethod toPaymentMethod(final Document doc) {
        var name = doc.getString(PaymentMethod.NAME_FIELD);
        var pm = new PaymentMethod(name);
        pm.setId(new BsonObjectId(doc.getObjectId(Entity.ID_FIELD)));
        pm.setUser(DAOUser.getLoggedUser());
        return pm;
    }

    public static User toUser(final Document doc) {
        User user = new User(new Username(doc.getString(USERNAME_FIELD)),
                new UserEmail(doc.getString(EMAIL_FIELD)),
                new UserPass(doc.getString(PASS_FIELD))
        );
        user.setId(new BsonObjectId(doc.getObjectId(Entity.ID_FIELD)));
        return user;
    }

    public static Pair<Optional<Budget>, BsonObjectId> parseBudget(final Document doc) {
        if (Objects.isNull(doc)) return new Pair<>(Optional.empty(), null);

        var id = new BsonObjectId(doc.getObjectId(Entity.ID_FIELD));
        var startDate = Dates.zonedFromDate(doc.getDate(START_DATE_FIELD));
        var endDate = Dates.zonedFromDate(doc.getDate(END_DATE_FIELD));
        var currency = Currency.getInstance(doc.getString(Budget.CURRENCY_FIELD));
        System.out.println("before limits");
        var hardLimit = doc.get(HARD_LIMIT_FIELD, Decimal128.class).bigDecimalValue();
        var softLimit = doc.get(SOFT_LIMIT_FIELD, Decimal128.class).bigDecimalValue();
        System.out.println("after limits");
        var pmId = doc.getObjectId(Budget.PM_ID_FIELD);
        System.out.println("paenus");
        var value = new Budget(
                new Interval(startDate, endDate),
                new Limit(softLimit, hardLimit, currency),
                null,
                DAOUser.getLoggedUser()
        );
        value.setId(id);

        return new Pair<>(Optional.of(value), pmId != null ? new BsonObjectId(pmId) : null);
    }
}
