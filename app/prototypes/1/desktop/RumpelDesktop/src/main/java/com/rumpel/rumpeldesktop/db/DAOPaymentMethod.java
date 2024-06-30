package com.rumpel.rumpeldesktop.db;

import com.mongodb.client.model.Filters;
import com.rumpel.rumpeldesktop.db.utils.Documents;
import com.rumpel.rumpeldesktop.db.utils.Outcome;
import com.szaumoor.rumple.model.entities.PaymentMethod;
import com.szaumoor.rumple.model.entities.User;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public final class DAOPaymentMethod extends DAO<String, PaymentMethod>{

    public static final String ID_FIELD = "_id";
    public static final String COLL_PM = "payment_methods";
    public static final String NAME_FIELD = "name";
    public static final String USER_ID_FIELD = "user_id";

    private final ObjectId userId;

    public DAOPaymentMethod(final User user) {
        collection = database.getCollection(COLL_PM);
        userId = (ObjectId) user.getId();
    }



    @Override
    public Optional<PaymentMethod> get(final String id) {
        return Documents.parsePaymentMethod(collection.find(and(
                        eq(USER_ID_FIELD, userId),
                        eq(NAME_FIELD)))
                .first());
    }

    @Override
    protected Optional<PaymentMethod> getById(final ObjectId id) {
        return Documents.parsePaymentMethod(collection.find(
                eq(DAOPaymentMethod.ID_FIELD, id))
                .first());
    }

    @Override
    public List<PaymentMethod> getAll() {
        return collection.find(Filters.eq(USER_ID_FIELD, userId))
                .into(new ArrayList<>())
                .stream()
                .map(Documents::parsePaymentMethod)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    @Override
    public Outcome insert(final PaymentMethod pm) {
        if (Objects.isNull(pm)) return Outcome.NULL;
        if (get(pm.getName()).isPresent()) return Outcome.UNIQUE_EXISTS;
        var opDoc = Documents.paymentMethodToDocument(pm);
        if (opDoc.isEmpty()) return Outcome.ERROR;
        collection.insertOne(opDoc.get());
        return Outcome.SUCCESS;
    }
}
