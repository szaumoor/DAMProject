package com.rumpel.rumpeldesktop.db.utils;

import com.rumpel.rumpeldesktop.db.DAOBudget;
import com.rumpel.rumpeldesktop.db.DAOTag;
import com.rumpel.rumpeldesktop.db.DAOUser;
import com.szaumoor.rumple.model.entities.Budget;
import com.szaumoor.rumple.model.entities.PaymentMethod;
import com.szaumoor.rumple.model.entities.Tag;
import com.szaumoor.rumple.model.entities.User;
import com.szaumoor.rumple.model.utils.types.*;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

public enum Documents {
    ;

    public static Optional<User> parseUser(final Document doc) {
        if (Objects.isNull(doc)) return Optional.empty();

        final var userId = doc.get(DAOUser.USER_ID_FIELD);
        final var username = new Username(doc.getString(DAOUser.USERNAME_FIELD));
        final var email = new UserEmail(doc.getString(DAOUser.EMAIL_FIELD));
        final var password = new UserPass(doc.getString(DAOUser.PASS_FIELD));

        final User user = new User(username, email, password);
        user.setId(userId);

        return Optional.of(user);
    }

    public static Optional<Document> userToDocument(final User user) {
        if (Objects.isNull(user)) return Optional.empty();

        final var username = user.getUsername().get();
        final var email = user.getEmail().get();
        final var password = user.getUserPass().getHashedPass();

        Document doc = new Document();
        doc.put(DAOUser.USERNAME_FIELD, username);
        doc.put(DAOUser.EMAIL_FIELD, email);
        doc.put(DAOUser.PASS_FIELD, password);

        return Optional.of(doc);
    }

    public static Optional<Tag> parseTag(final Document doc) {
        if (Objects.isNull(doc)) return Optional.empty();
        Tag tag = new Tag(doc.getString(DAOTag.NAME_FIELD));
        tag.setUser(DAOUser.getLoggedUser().get());
        return Optional.of(tag);
    }

    public static Optional<Document> tagToDocument(final Tag tag) {
        if (Objects.isNull(tag)) return Optional.empty();

        final var tagName = tag.getName();
        final var userId = (ObjectId) tag.getUser().getId();

        Document doc = new Document();
        doc.put(DAOTag.NAME_FIELD, tagName);
        doc.put(DAOTag.USER_ID_FIELD, userId);

        return Optional.of(doc);
    }

    public static Optional<PaymentMethod> parsePaymentMethod(final Document doc) {
        if (Objects.isNull(doc)) return Optional.empty();
        PaymentMethod pm = new PaymentMethod(doc.getString(DAOTag.NAME_FIELD));
        pm.setUser(DAOUser.getLoggedUser().get());
        return Optional.of(pm);
    }

    public static Optional<Document> paymentMethodToDocument(final PaymentMethod pm) {
        if (Objects.isNull(pm)) return Optional.empty();

        final var tagName = pm.getName();
        final var userId = (ObjectId) pm.getUser().getId();

        Document doc = new Document();
        doc.put(DAOTag.NAME_FIELD, tagName);
        doc.put(DAOTag.USER_ID_FIELD, userId);

        return Optional.of(doc);
    }

    public static Pair<Optional<Budget>, ObjectId> parseBudget(final Document doc) {
        if (Objects.isNull(doc)) return new Pair<>(Optional.empty(), null);

        var startDate = ZonedDateTime.ofInstant(doc.getDate(DAOBudget.START_DATE_FIELD).toInstant(), ZoneId.systemDefault());
        var endDate = ZonedDateTime.ofInstant(doc.getDate(DAOBudget.END_DATE_FIELD).toInstant(), ZoneId.systemDefault());
        var currency = Currency.getInstance(doc.getString(DAOBudget.CURRENCY_FIELD));
        var hardLimit = doc.getDouble(DAOBudget.HARD_LIMIT_FIELD);
        var softLimit = doc.getDouble(DAOBudget.SOFT_LIMIT_FIELD);
        var pmId = doc.getObjectId(DAOBudget.PM_ID_FIELD);

        var budget = Optional.of(new Budget(
                new Interval(startDate, endDate),
                new Limit(BigDecimal.valueOf(softLimit), BigDecimal.valueOf(hardLimit), currency),
                null,
                null
                ));
        return new Pair<>(budget, pmId);
    }

    public static Optional<Document> budgetToDocument(final Budget budget) {
        if (Objects.isNull(budget)) return Optional.empty();

        final var startDate = budget.getInterval().getStartDate();
        final var endDate = budget.getInterval().getEndDate();
        final var currency = budget.getLimit().getCurrency();
        final var softLimit = budget.getLimit().getSoftLimit();
        final var hardLimit = budget.getLimit().getHardLimit();
        final var pmId = (ObjectId) budget.getPaymentMethod().getId();

        final var doc = new Document();

        doc.put(DAOBudget.START_DATE_FIELD, Date.from(startDate.toInstant()));
        doc.put(DAOBudget.END_DATE_FIELD, Date.from(endDate.toInstant()));
        doc.put(DAOBudget.CURRENCY_FIELD, currency.toString());
        doc.put(DAOBudget.SOFT_LIMIT_FIELD, softLimit);
        doc.put(DAOBudget.HARD_LIMIT_FIELD, hardLimit);
        doc.put(DAOBudget.PM_ID_FIELD, pmId);

        return Optional.of(doc);
    }

}
