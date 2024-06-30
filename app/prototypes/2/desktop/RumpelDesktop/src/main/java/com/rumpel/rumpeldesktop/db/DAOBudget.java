package com.rumpel.rumpeldesktop.db;

import com.rumpel.rumpeldesktop.db.utils.Documents;
import com.szaumoor.rumple.db.utils.Outcome;
import com.szaumoor.rumple.model.entities.Budget;
import com.szaumoor.rumple.model.interfaces.Entity;
import org.bson.BsonObjectId;

import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;

public class DAOBudget extends AbstractPluralDAO<Budget> {

    private final DAOPaymentMethod dao;

    public DAOBudget() {
        super(Budget.COLL_BUDGETS);
        dao = new DAOPaymentMethod();
    }

    @Override
    public Optional<Budget> get(final Object id) {
        return crudGet(eq(Entity.ID_FIELD, (BsonObjectId) id), DAOUser.getLoggedUser(), d -> {
            var pair = Documents.parseBudget(d);
            var op = pair.first();
            var pmId = pair.second();
            var pm = dao.getById(pmId);
            if (pm.isEmpty()) return Optional.empty();
            op.ifPresent(budget -> budget.setPaymentMethod(pm.get()));
            return op;
        });
    }

    @Override
    public Optional<Budget> getById(Object id) {
        return get(id);
    }

    @Override
    public List<Budget> getAll() {
        return crudGetAll(DAOUser.getLoggedUser(), doc -> {
            var pair = Documents.parseBudget(doc);
            var op = pair.first();
            var id = pair.second();
            var pm = id != null ? dao.getById(id).orElseThrow() : null;
            op.ifPresent(b -> b.setPaymentMethod(pm));
            return op;
        });
    }

    @Override
    public List<Budget> getAllById(List<Object> list) {
        return null;
    }

    @Override
    public Outcome insertAll(List<Budget> list) {
        return null;
    }

    @Override
    public Outcome deleteAll(List<Budget> list) {
        return null;
    }

    @Override
    public Outcome deleteAll() {
        return null;
    }

    @Override
    public Outcome insert(final Budget budget) {
        return crudInsert(budget, () -> get(budget.getId()).isPresent(), Documents::budgetToDocument);
    }

    @Override
    public Outcome modify(final Budget budget) {
        return crudModify(budget, budget.getUser(), Documents::budgetToDocument);
    }

    @Override
    public Outcome delete(final Budget budget) {
        return crudDelete(budget, Documents::budgetToDocument, null);
    }
}
