package com.rumpel.rumpeldesktop.db;

import com.mongodb.client.model.Filters;
import com.rumpel.rumpeldesktop.db.utils.Documents;
import com.rumpel.rumpeldesktop.db.utils.Outcome;
import com.szaumoor.rumple.model.entities.Budget;
import com.szaumoor.rumple.model.entities.User;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DAOBudget extends DAO<ObjectId, Budget> {

    public static final String COLL_BUDGET = "budgets";

    public static final String USER_ID_FIELD = "user_id";
    public static final String PM_ID_FIELD = "pm_id";
    public static final String START_DATE_FIELD = "start_date";
    public static final String END_DATE_FIELD = "end_date";
    public static final String CURRENCY_FIELD = "currency";
    public static final String HARD_LIMIT_FIELD = "hard_limit";
    public static final String SOFT_LIMIT_FIELD = "soft_limit";

    private final ObjectId userId;
    private final DAOPaymentMethod dao;

    public DAOBudget(final User user) {
        collection = database.getCollection(COLL_BUDGET);
        userId = (ObjectId) user.getId();
        dao = new DAOPaymentMethod(user);
    }

    @Override //unused
    protected Optional<Budget> get(final ObjectId id) {
        return Optional.empty();
    }

    @Override
    public List<Budget> getAll() {
        return collection.find(Filters.eq(USER_ID_FIELD, userId))
                .into(new ArrayList<>())
                .stream()
                .map(doc -> {
                    var pair = Documents.parseBudget(doc);
                    var op = pair.first().orElseThrow();
                    var id = pair.second();
                    var pm = id != null ? dao.getById(id).orElseThrow() : null;
                    op.setPaymentMethod(pm);
                    return op;
                }).collect(Collectors.toList());
    }

    @Override
    public Outcome insert(final Budget object) {
        return null;
    }
}
