package com.rumpel.rumpeldesktop.db;

import com.rumpel.rumpeldesktop.db.utils.Documents;
import com.szaumoor.rumple.db.utils.Outcome;
import com.szaumoor.rumple.db.utils.Pair;
import com.szaumoor.rumple.model.entities.Bill;
import com.szaumoor.rumple.model.entities.ItemBill;
import com.szaumoor.rumple.model.interfaces.Entity;
import org.bson.BsonObjectId;

import java.util.*;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.szaumoor.rumple.model.interfaces.Entity.USER_ID_FIELD;

public final class DAOBill extends AbstractPluralDAO<Bill> {

    private final DAOPaymentMethod daoPm;
    private final DAOTag daoTags;

    public DAOBill() {
        super(Bill.COLL_BILLS);
        daoPm = new DAOPaymentMethod();
        daoTags = new DAOTag();
    }

    @Override
    public List<Bill> getAll() {
        return crudGetAll(DAOUser.getLoggedUser(), document -> {
            var trio = Documents.parseBill(document);
            var first = trio.first();
            var second = trio.second();

            final var byId = daoPm.getById(second);
            if (first.isEmpty()) throw new RuntimeException("uh oh - first is empty");
            if (byId.isEmpty()) throw new RuntimeException("uh oh - second is empty");

            first.get().setPaymentMethod(byId.get());
            trio.third().forEach(i -> {
                var item = i.first();
                var tags = i.second();
                item.setBill(first.get());
                item.addAll(daoTags.getAllById(tags.stream().map(bsonObjectId -> (Object) bsonObjectId).toList()));
            });
            first.get().addAll(trio.third().stream().map(Pair::first).toList());
            return first;
        });
    }

    @Override
    public List<Bill> getAllById(final List<Object> list) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Outcome insertAll(final List<Bill> list) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Outcome deleteAll(final List<Bill> list) {
        return null;
    }

    @Override
    public Outcome deleteAll() {
        return null;
    }

    @Override
    public Optional<Bill> get(final Object o) {
        return collection.find(and(eq(Entity.ID_FIELD, (BsonObjectId) o), eq(USER_ID_FIELD, DAOUser.getLoggedUser().getId())))
                .map(d -> {
                    var trio = Documents.parseBill(d);
                    var op = trio.first();
                    var pmId = trio.second();
                    var list = trio.third();
                    var opPm = daoPm.get(pmId);
                    if (op.isEmpty() || opPm.isEmpty()) throw new RuntimeException();
                    op.get().setPaymentMethod(opPm.get());
                    var listOfItems = new ArrayList<ItemBill>();
                    list.forEach(pair -> {
                        ItemBill first = pair.first();
                        first.setBill(op.get());
                        listOfItems.add(first);
                        var tags = daoTags.getAllById(Collections.singletonList(pair.second()));
                        first.addAll(tags);
                    });
                    op.get().addAll(listOfItems);
                    return op;
                }).first();
    }

    @Override
    public Optional<Bill> getById(final Object o) {
        return Optional.empty();
    }

    @Override
    public Outcome insert(final Bill bill) {
        if (Objects.isNull(bill)) return Outcome.NULL;
        var opDoc = Documents.billToDocument(bill);
        if (opDoc.isEmpty()) return Outcome.ERROR;
        collection.insertOne(opDoc.get());
        return Outcome.SUCCESS;
    }

    @Override
    public Outcome modify(final Bill bill) {
        return crudModify(bill, bill.getUser(), Documents::billToDocument);
    }

    @Override
    public Outcome delete(final Bill bill) {
        return crudDelete(bill, Documents::billToDocument, null);
    }
}
