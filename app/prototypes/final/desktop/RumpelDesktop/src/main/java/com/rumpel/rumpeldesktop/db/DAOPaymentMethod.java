package com.rumpel.rumpeldesktop.db;

import com.rumpel.rumpeldesktop.db.utils.Documents;
import com.szaumoor.rumple.db.utils.Outcome;
import com.szaumoor.rumple.model.entities.PaymentMethod;
import org.bson.BsonObjectId;

import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.*;
import static com.szaumoor.rumple.model.interfaces.Entity.ID_FIELD;

/**
 * DAO class that handles PaymentMethod entities and objects.
 */
public final class DAOPaymentMethod extends AbstractPluralDAO<PaymentMethod> {

    /**
     * No-args constructor initializing the basic fields and retrieves the collection.
     */
    public DAOPaymentMethod() {
        super(PaymentMethod.COLL_PMS);
    }

    /**
     * Retrieves and returns the payment method with the specified ID.
     *
     * @param id the ID of the payment method to retrieve
     * @return an Optional containing the payment method, or an empty Optional if no payment method is found
     */
    @Override
    public Optional<PaymentMethod> get(final Object id) {
        return crudGet(eq(PaymentMethod.NAME_FIELD, id.toString()), DAOUser.getLoggedUser(), Documents::parsePaymentMethod);
    }

    /**
     * Retrieves a payment method by its ID.
     *
     * @param id the ID of the payment method
     * @return an optional containing the payment method if found, otherwise empty
     */
    @Override
    public Optional<PaymentMethod> getById(final Object id) {
        return crudGet(eq(ID_FIELD, (BsonObjectId) id), DAOUser.getLoggedUser(), Documents::parsePaymentMethod);
    }

    /**
     * Retrieves all payment methods.
     *
     * @return A list of PaymentMethod objects.
     */
    @Override
    public List<PaymentMethod> getAll() {
        return crudGetAll(DAOUser.getLoggedUser(), Documents::parsePaymentMethod);
    }

    /**
     * Returns a list of PaymentMethod objects based on the given list of objects.
     * Unsupported, as there wasn't any use case for it.
     *
     * @param list a list of objects
     * @return a list of PaymentMethod objects
     */
    @Override
    public List<PaymentMethod> getAllById(final List<Object> list) {
        throw new UnsupportedOperationException();
    }

    /**
     * Inserts all the payment methods from the provided list.
     *
     * @param list the list of payment methods to be inserted
     * @return the outcome of the insertion
     */
    @Override
    public Outcome insertAll(final List<PaymentMethod> list) {
        throw new UnsupportedOperationException();
    }

    /**
     * Deletes all the payment methods in the given list from the database.
     * Unsupported, as there wasn't any use case for it.
     *
     * @param list the list of payment methods to be deleted
     * @return the outcome of the delete operation
     */
    @Override
    public Outcome deleteAll(final List<PaymentMethod> list) {
        throw new UnsupportedOperationException();
    }

    /**
     * Deletes all records related to the logged user's payment methods.
     *
     * @return The outcome of the delete operation.
     */
    @Override
    public Outcome deleteAll() {
        return crudDeleteAll(DAOUser.getLoggedUser(), Documents::parsePaymentMethod);
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
     * Modify the payment method.
     *
     * @param paymentMethod the payment method to be modified
     * @return the outcome of the modification process
     */
    @Override
    public Outcome modify(final PaymentMethod paymentMethod) {
        return crudModify(paymentMethod, paymentMethod.getUser(), Documents::paymentMethodToDocument);
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

}
