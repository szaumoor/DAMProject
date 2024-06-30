package com.rumpel.rumpeldesktop.db;

import com.rumpel.rumpeldesktop.db.utils.Documents;
import com.szaumoor.rumple.db.utils.Outcome;
import com.szaumoor.rumple.model.entities.Bill;
import com.szaumoor.rumple.model.entities.Budget;
import com.szaumoor.rumple.model.entities.PaymentMethod;
import com.szaumoor.rumple.model.interfaces.Entity;
import org.bson.BsonObjectId;
import org.bson.Document;

import java.time.Month;
import java.time.Year;
import java.util.Currency;
import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.*;
import static com.szaumoor.rumple.model.interfaces.Entity.USER_ID_FIELD;

/**
 * DAO class that handles Budget entities and objects.
 */
public class DAOBudget extends AbstractPluralDAO<Budget> {

    private final DAOPaymentMethod dao;

    /**
     * No-args constructor initializing the basic fields and retrieves the collection.
     */
    public DAOBudget() {
        super(Budget.COLL_BUDGETS);
        dao = new DAOPaymentMethod();
    }

    /**
     * Retrieves a budget based on the provided id.
     *
     * @param id the id of the budget to retrieve
     * @return an optional containing the retrieved budget, or an empty optional if no budget is found
     */
    @Override
    public Optional<Budget> get(final Object id) {
        return crudGet(eq(Entity.ID_FIELD, (BsonObjectId) id), DAOUser.getLoggedUser(), this::unpackBudgetPair);
    }

    /**
     * Checks if a budget with the specified payment method exists for the logged-in user.
     *
     * @param pm the payment method to check
     * @return true if a budget with the payment method exists, false otherwise
     */
    public boolean budgetWithPmExists(final PaymentMethod pm) {
        return collection.find(and(
                eq(USER_ID_FIELD, DAOUser.getLoggedUser().getId()),
                eq(Budget.PM_ID_FIELD, pm.getId()))
        ).first() != null;
    }

    /**
     * Retrieves a budget by its unique identifier.
     * Equivalent to {@link #get(Object)}
     *
     * @param id the unique identifier of the budget
     * @return an optional containing the budget if found, otherwise empty
     */
    @Override
    public Optional<Budget> getById(Object id) {
        return get(id);
    }

    /**
     * Retrieves all budget objects from the user.
     *
     * @return a list of budget objects
     */
    @Override
    public List<Budget> getAll() {
        return crudGetAll(DAOUser.getLoggedUser(), this::unpackBudgetPair);
    }

    /**
     * Retrieves all filtered budgets for a specific year and month.
     *
     * @param year  the year of the budgets to retrieve
     * @param month the month of the budgets to retrieve
     * @return a list of Budget objects that match the specified year and month
     */
    public List<Budget> getAllFiltered(final Year year, final Month month) {
        return crudGetAllFiltered(DAOUser.getLoggedUser(), this::unpackBudgetPair, Documents.budgetsInInterval(year, month));
    }

    /**
     * Retrieves a list of filtered Budget objects based on the provided criteria.
     *
     * @param year     the year of the budget
     * @param month    the month of the budget
     * @param currency the currency of the budget
     * @param pm       the payment method of the budget
     * @return a list of filtered Budget objects
     */
    public List<Budget> getAllFiltered(final Year year, final Month month, final Currency currency, final PaymentMethod pm) {
        return crudGetAllFiltered(DAOUser.getLoggedUser(), this::unpackBudgetPair, and(and(eq(Bill.CURRENCY_FIELD, currency), eq(Bill.PM_FIELD, pm.getId())), Documents.budgetsInInterval(year, month)));
    }

    /**
     * Retrieves a list of budgets based on the provided list of IDs.
     * <br><br> Unsupported. There was no need so far to use it.
     *
     * @param list the list of IDs used to retrieve the budgets
     * @return a list of budgets matching the provided IDs
     */
    @Override
    public List<Budget> getAllById(final List<Object> list) {
        throw new UnsupportedOperationException();
    }

    /**
     * Inserts a list of budgets into the collection.
     * <br><br> Unsupported. There was no need so far to use it.
     *
     * @param list the list of budgets to insert
     * @return the outcome of the insertion
     */
    @Override // unused
    public Outcome insertAll(final List<Budget> list) {
        throw new UnsupportedOperationException();
    }

    /**
     * Deletes a list of bills from the collection.
     * <br><br> Unsupported. There was no need so far to use it.
     *
     * @param list the list of bills to remove
     * @return the outcome of the deletion
     */
    @Override
    public Outcome deleteAll(final List<Budget> list) {
        throw new UnsupportedOperationException();
    }

    /**
     * Delete all records from the collection associated with the logged-in user.
     *
     * @return the outcome of the operation
     */
    @Override
    public Outcome deleteAll() {
        return crudDeleteAll(DAOUser.getLoggedUser(), this::unpackBudgetPair);
    }

    /**
     * Inserts a Budget object into the system.
     *
     * @param budget the Budget object to be inserted
     * @return the outcome of the insert operation
     */
    @Override
    public Outcome insert(final Budget budget) {
        return crudInsert(budget, () -> get(budget.getId()).isPresent(), Documents::budgetToDocument);
    }

    /**
     * Generates a function comment for the given function body.
     *
     * @param budget the budget object to be modified
     * @return the outcome of the modification
     */
    @Override
    public Outcome modify(final Budget budget) {
        return crudModify(budget, budget.getUser(), Documents::budgetToDocument);
    }

    /**
     * Deletes a budget.
     *
     * @param budget the budget to be deleted
     * @return the outcome of the deletion operation
     */
    @Override
    public Outcome delete(final Budget budget) {
        return crudDelete(budget, Documents::budgetToDocument);
    }

    /**
     * Unpacks a budget from the given document and attempts to bind it to its payment method. Internal use only.
     *
     * @param doc the document to unpack the budget pair from
     * @return an optional containing the unpacked budget pair, or an empty optional if the unpacking fails
     */
    private Optional<Budget> unpackBudgetPair(final Document doc) {
        var pair = Documents.parseBudget(doc);
        var op = pair.first();
        var id = pair.second();
        op.ifPresent(budget -> {
            if (id != null) budget.setPaymentMethod(dao.getById(id).orElseThrow());
        });
        return op;
    }
}
