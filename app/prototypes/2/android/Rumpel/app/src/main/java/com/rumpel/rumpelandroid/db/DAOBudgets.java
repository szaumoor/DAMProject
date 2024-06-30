package com.rumpel.rumpelandroid.db;

import static com.rumpel.rumpelandroid.db.utils.Filters.eq;

import android.content.Context;

import com.rumpel.rumpelandroid.db.utils.Documents;
import com.rumpel.rumpelandroid.utils.Logs;
import com.szaumoor.rumple.db.utils.Outcome;
import com.szaumoor.rumple.model.entities.Budget;
import com.szaumoor.rumple.model.entities.Tag;
import com.szaumoor.rumple.model.interfaces.Entity;

import org.bson.Document;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.realm.mongodb.mongo.result.InsertOneResult;

public class DAOBudgets extends AbstractPluralDAO<Budget> {

    private final DAOPaymentMethods dao;

    public DAOBudgets(Context context) {
        super(context, Budget.COLL_BUDGETS);
        dao = new DAOPaymentMethods(context);
    }

    @Override
    public Optional<Budget> get(Object id) {
        return crudGet(eq(Entity.ID_FIELD, id), this::unpack, false);
    }

    private Budget unpack(final Document doc){
        System.out.println("Unpacking");
        var pair = Documents.parseBudget(doc);
        System.out.println("Pair parsed");
        var op = pair.first();
        var id = pair.second();
        op.ifPresent(budget -> {
            if (id != null) budget.setPaymentMethod(dao.getById(id).orElseThrow());
        });
        return op.orElseThrow();
    }

    @Override
    public Optional<Budget> getById(Object o) {
        return get(o);
    }

    @Override
    public Outcome insert(final Budget budget) {
        if (Objects.isNull(budget)) throw new NullPointerException();
        final Document doc = Documents.budgetToDocument(budget).orElseThrow();
        final var executor = Executors.newSingleThreadExecutor();
        Optional<Budget> pm = getById(budget.getId());
        if (pm.isPresent()) return Outcome.UNIQUE_EXISTS;
        executor.submit(() -> {
            InsertOneResult insertOneResult = coll.insertOne(doc).get();
            budget.setId(insertOneResult.getInsertedId().asObjectId());
        });
        executor.shutdown();
        try {
            boolean b = executor.awaitTermination(10, TimeUnit.SECONDS);
            if (b) return Outcome.SUCCESS;
            else return Outcome.TIMEOUT;
        } catch (InterruptedException e) {
            Logs.interrupt(getClass().getSimpleName());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Outcome modify(Budget entity) {
        return null;
    }

    @Override
    public Outcome delete(Budget budget) {
        return null;
    }

    @Override
    public List<Budget> getAll() {
        return crudGetAll(this::unpack);
    }

    @Override
    public List<Budget> getAllById(List<Object> list) {
        return null;
    }

    @Override
    public Outcome insertAll(List<Budget> elements) {
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
}
