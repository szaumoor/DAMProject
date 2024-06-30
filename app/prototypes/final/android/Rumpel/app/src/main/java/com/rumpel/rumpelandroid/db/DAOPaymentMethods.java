package com.rumpel.rumpelandroid.db;

import static com.rumpel.rumpelandroid.db.utils.Filters.eq;

import android.content.Context;

import com.rumpel.rumpelandroid.db.utils.Documents;
import com.szaumoor.rumple.db.utils.Outcome;
import com.szaumoor.rumple.model.entities.PaymentMethod;
import com.szaumoor.rumple.model.interfaces.Entity;

import java.util.List;
import java.util.Optional;
/**
 * DAO class that handles PaymentMethod entities and objects.
 */
public class DAOPaymentMethods extends AbstractPluralDAO<PaymentMethod> {
    /**
     * Constructor that initializes the basic fields and retrieves the collection.
     */
    public DAOPaymentMethods(final Context context) {
        super(context, PaymentMethod.COLL_PMS);
    }
    /**
     * Retrieves and returns the payment method with the specified ID.
     *
     * @param id the ID of the payment method to retrieve
     * @return an Optional containing the payment method, or an empty Optional if no payment method is found
     */
    @Override
    public Optional<PaymentMethod> get(final Object id) {
        return crudGet(eq(PaymentMethod.NAME_FIELD, id.toString()), Documents::parsePaymentMethod, false);
    }
    /**
     * Retrieves a payment method by its ID.
     *
     * @param id the ID of the payment method
     * @return an optional containing the payment method if found, otherwise empty
     */
    @Override
    public Optional<PaymentMethod> getById(final Object id) {
        return crudGet(eq(Entity.ID_FIELD, id), Documents::parsePaymentMethod, false);
    }
    /**
     * Inserts a new payment method into the system.
     *
     * @param pm the payment method to be inserted
     * @return the outcome of the insertion
     */
    @Override
    public Outcome insert(final PaymentMethod pm) {
        return crudInsert(pm, () -> get(pm.getName()).isPresent(), Documents::paymentMethodToDocument);
    }
    /**
     * Delete a payment method.
     *
     * @param pm the payment method to delete
     * @return the outcome of the delete operation
     */
    @Override
    public Outcome delete(final PaymentMethod pm) {
        return crudDelete(pm, Documents::paymentMethodToDocument);
    }
    /**
     * Retrieves all payment methods.
     *
     * @return A list of PaymentMethod objects.
     */
    @Override
    public List<PaymentMethod> getAll() {
        return crudGetAll(Documents::parsePaymentMethod);
    }

    @Override //unused
    public List<PaymentMethod> getAllById(final List<Object> list) {
        throw new UnsupportedOperationException();
    }

    @Override //unused
    public Outcome insertAll(final List<PaymentMethod> elements) {
        throw new UnsupportedOperationException();
    }

    @Override //unused
    public Outcome deleteAll(final List<PaymentMethod> list) {
        throw new UnsupportedOperationException();
    }

    @Override //unused
    public Outcome deleteAll() {
        throw new UnsupportedOperationException();
    }
    /**
     * Modify the payment method.
     *
     * @param pm the payment method to be modified
     * @return the outcome of the modification process
     */
    @Override
    public Outcome modify(final PaymentMethod pm) {
        return crudModify(pm, Documents::paymentMethodToDocument);
    }
}
