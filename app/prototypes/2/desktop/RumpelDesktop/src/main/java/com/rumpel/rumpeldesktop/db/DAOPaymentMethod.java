package com.rumpel.rumpeldesktop.db;

import com.rumpel.rumpeldesktop.db.utils.Documents;
import com.szaumoor.rumple.db.utils.Outcome;
import com.szaumoor.rumple.model.entities.PaymentMethod;
import org.bson.BsonObjectId;

import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;
import static com.szaumoor.rumple.model.interfaces.Entity.ID_FIELD;

public final class DAOPaymentMethod extends AbstractPluralDAO<PaymentMethod> {

    public DAOPaymentMethod() {
        super(PaymentMethod.COLL_PMS);
    }

    @Override
    public Optional<PaymentMethod> get(final Object id) {
        return crudGet(eq(PaymentMethod.NAME_FIELD, id.toString()), DAOUser.getLoggedUser(), Documents::parsePaymentMethod);
    }

    @Override
    public Optional<PaymentMethod> getById(final Object id) {
        return crudGet(eq(ID_FIELD, (BsonObjectId) id), DAOUser.getLoggedUser(), Documents::parsePaymentMethod);
    }

    @Override
    public List<PaymentMethod> getAll() {
        return crudGetAll(DAOUser.getLoggedUser(), Documents::parsePaymentMethod);
    }

    @Override
    public List<PaymentMethod> getAllById(List<Object> list) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Outcome insertAll(List<PaymentMethod> list) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Outcome deleteAll(List<PaymentMethod> list) {
        return null;
    }

    @Override
    public Outcome deleteAll() {
        return crudDeleteAll(DAOUser.getLoggedUser(), Documents::parsePaymentMethod);
    }

    @Override
    public Outcome insert(final PaymentMethod pm) {
        return crudInsert(pm, ()-> get(pm.getName()).isPresent(), Documents::paymentMethodToDocument);
    }

    @Override
    public Outcome modify(final PaymentMethod paymentMethod) {
        return crudModify(paymentMethod, paymentMethod.getUser(), Documents::paymentMethodToDocument);
    }

    @Override
    public Outcome delete(final PaymentMethod pm) {
        return crudDelete(pm, Documents::paymentMethodToDocument, null);
    }
}
