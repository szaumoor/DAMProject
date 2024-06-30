package com.rumpel.rumpelandroid.db;

import static com.rumpel.rumpelandroid.db.utils.Filters.eq;

import android.content.Context;

import com.rumpel.rumpelandroid.db.utils.Documents;
import com.szaumoor.rumple.db.utils.Outcome;
import com.szaumoor.rumple.model.entities.Budget;
import com.szaumoor.rumple.model.interfaces.Entity;

import org.bson.Document;

import java.util.List;
import java.util.Optional;
/**
 * DAO class that handles Budget entities and objects.
 */
public class DAOBudgets extends AbstractPluralDAO<Budget> {
    private final DAOPaymentMethods dao;
    /**
     * Constructor that initializes the basic fields and retrieves the collection.
     */
    public DAOBudgets(final Context context) {
        super(context, Budget.COLL_BUDGETS);
        dao = new DAOPaymentMethods(context);
    }
    /**
     * Retrieves a budget based on the provided id.
     *
     * @param id the id of the budget to retrieve
     * @return an optional containing the retrieved budget, or an empty optional if no budget is found
     */
    @Override
    public Optional<Budget> get(final Object id) {
        return crudGet(eq(Entity.ID_FIELD, id), this::unpackBudgetPair, false);
    }
    /**
     * Retrieves a budget by its unique identifier.
     * Equivalent to {@link #get(Object)}
     *
     * @param id the unique identifier of the budget
     * @return an optional containing the budget if found, otherwise empty
     */
    @Override
    public Optional<Budget> getById(final Object id) {
        return get(id);
    }

    @Override //unsupported for now, no need
    public List<Budget> getAllById(final List<Object> list) {
        throw new UnsupportedOperationException();
    }
    /**
     * Retrieves all budget objects from the user.
     *
     * @return a list of budget objects
     */
    @Override
    public List<Budget> getAll() {
        return crudGetAll(this::unpackBudgetPair);
    }
    /**
     * Inserts a Budget object into the system.
     *
     * @param budget the Budget object to be inserted
     * @return the outcome of the insert operation
     */
    @Override
    public Outcome insert(final Budget budget) {
        return crudInsert(budget, () -> getById(budget.getId()).isPresent(), Documents::budgetToDocument);
    }

    @Override //not needed
    public Outcome insertAll(final List<Budget> elements) {
        throw new UnsupportedOperationException();
    }
    /**
     * Generates a function comment for the given function body.
     *
     * @param budget the budget object to be modified
     * @return the outcome of the modification
     */
    @Override
    public Outcome modify(final Budget budget) {
        return crudModify(budget, Documents::budgetToDocument);
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

    @Override //unsupported for now, no need
    public Outcome deleteAll(final List<Budget> list) {
        throw new UnsupportedOperationException();
    }

    @Override //unsupported for now
    public Outcome deleteAll() {
        throw new UnsupportedOperationException();
    }
    /**
     * Unpacks a budget from the given document and attempts to bind it to its payment method. Internal use only.
     *
     * @param doc the document to unpack the budget pair from
     * @return an optional containing the unpacked budget pair, or an empty optional if the unpacking fails
     */
    private Optional<Budget> unpackBudgetPair(final Document doc){
        var pair = Documents.parseBudget(doc);
        var op = pair.first();
        var id = pair.second();
        op.ifPresent(budget -> {
            if (id != null) budget.setPaymentMethod(dao.getById(id).orElseThrow());
        });
        return op;
    }
}
