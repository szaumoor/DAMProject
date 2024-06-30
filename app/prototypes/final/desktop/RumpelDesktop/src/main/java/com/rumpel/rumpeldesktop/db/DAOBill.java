package com.rumpel.rumpeldesktop.db;

import com.rumpel.rumpeldesktop.db.utils.Documents;
import com.szaumoor.rumple.db.utils.Outcome;
import com.szaumoor.rumple.model.entities.Bill;
import com.szaumoor.rumple.model.entities.ItemBill;
import com.szaumoor.rumple.model.entities.PaymentMethod;
import com.szaumoor.rumple.model.entities.Tag;
import com.szaumoor.rumple.model.interfaces.Entity;
import org.bson.BsonObjectId;
import org.bson.Document;

import java.time.Month;
import java.time.Year;
import java.util.*;

import static com.mongodb.client.model.Filters.*;
import static com.szaumoor.rumple.model.interfaces.Entity.USER_ID_FIELD;

/**
 * DAO class that handles Bill entities and objects.
 */
public final class DAOBill extends AbstractPluralDAO<Bill> {
    private final DAOPaymentMethod daoPm;
    private final DAOTag daoTags;

    /**
     * No-args constructor initializing the basic fields and retrieves the collection.
     */
    public DAOBill() {
        super(Bill.COLL_BILLS);
        daoPm = new DAOPaymentMethod();
        daoTags = new DAOTag();
    }

    /**
     * Retrieves all the bills.
     *
     * @return a list of bills
     */
    @Override
    public List<Bill> getAll() {
        return crudGetAll(DAOUser.getLoggedUser(), this::unpackBill);
    }

    /**
     * Checks if a bill with the specified tag exists.
     *
     * @param tag the tag to check
     * @return true if a bill with the tag exists, false otherwise
     */
    public boolean billWithTagExists(final Tag tag) {
        return collection.find(and(
                eq(USER_ID_FIELD, DAOUser.getLoggedUser().getId()),
                elemMatch(Bill.ITEMS_FIELD + "." + ItemBill.TAGS_FIELD, new Document("$eq", tag.getId())))
        ).first() != null;
    }

    /**
     * Checks if a bill with the specified payment method exists.
     *
     * @param pm the payment method to check
     * @return true if a bill with the payment method exists, false otherwise
     */
    public boolean billWithPmExists(final PaymentMethod pm) {
        return collection.find(and(
                eq(USER_ID_FIELD, DAOUser.getLoggedUser().getId()),
                eq(Bill.PM_FIELD, pm.getId()))).first() != null;
    }

    /**
     * Retrieves a list of bills based on the provided list of IDs.
     * <br><br> Unsupported. There was no need so far to use it.
     *
     * @param list the list of IDs used to retrieve the bills
     * @return a list of bills matching the provided IDs
     */
    @Override
    public List<Bill> getAllById(final List<Object> list) {
        throw new UnsupportedOperationException();
    }

    /**
     * Inserts a list of bills into the collection.
     * <br><br> Only used to testing purposes. No need in the actual app.
     *
     * @param list the list of bills to insert
     * @return the outcome of the insertion
     */
    @Override
    public Outcome insertAll(final List<Bill> list) {
        var insertMany = collection.insertMany(
                list.stream().map(Documents::billToDocument).map(Optional::orElseThrow).toList()
        );
        if (insertMany.getInsertedIds().size() != list.size()) return Outcome.ERROR;
        return Outcome.SUCCESS;
    }

    /**
     * Deletes a list of bills into the collection.
     * <br><br> Unsupported. There was no need so far to use it.
     *
     * @param list the list of bills to insert
     * @return the outcome of the insertion
     */
    @Override
    public Outcome deleteAll(final List<Bill> list) {
        throw new UnsupportedOperationException();
    }

    /**
     * Deletes all records associated with the logged user.
     *
     * @return the outcome of the delete operation
     */
    @Override
    public Outcome deleteAll() {
        return crudDeleteAll(DAOUser.getLoggedUser(), this::unpackBill);
    }

    /**
     * Retrieves an Optional object containing a Bill based on the given id object.
     *
     * @param o The object used to find the Bill.
     * @return An Optional object that may contain the found Bill.
     */
    @Override
    public Optional<Bill> get(final Object o) {
        return collection.find(and(eq(Entity.ID_FIELD, (BsonObjectId) o), eq(USER_ID_FIELD, DAOUser.getLoggedUser().getId())))
                .map(this::unpackBill).first();
    }

    /**
     * Retrieves a bill by its ID. It's functionally identical to {@link #get(Object)}
     *
     * @param o the ID of the bill
     * @return an optional containing the bill if found, otherwise empty
     */
    @Override
    public Optional<Bill> getById(final Object o) {
        return get(o);
    }

    /**
     * Inserts a bill into the system.
     *
     * @param bill the bill to be inserted
     * @return the outcome of the insertion
     */
    @Override
    public Outcome insert(final Bill bill) {
        return crudInsert(bill, () -> false, Documents::billToDocument);
    }

    /**
     * Modifies a bill in the system.
     *
     * @param bill the bill to be modified
     * @return the outcome of the modification
     */
    @Override
    public Outcome modify(final Bill bill) {
        return crudModify(bill, bill.getUser(), Documents::billToDocument);
    }

    /**
     * Deletes a bill.
     *
     * @param bill the bill to be deleted
     * @return the outcome of the delete operation
     */
    @Override
    public Outcome delete(final Bill bill) {
        return crudDelete(bill, Documents::billToDocument);
    }

    /**
     * Retrieves all the filtered bills based on the given currency, year, and month.
     *
     * @param curr  the currency to filter the bills by
     * @param year  the year to filter the bills by
     * @param month the month to filter the bills by
     * @return the list of filtered bills
     */
    public List<Bill> getAllFiltered(final Currency curr, final Year year, final Month month) {
        return crudGetAllFiltered(DAOUser.getLoggedUser(), this::unpackBill, and(eq(Bill.CURRENCY_FIELD, curr.getCurrencyCode()), Documents.monthAndYearToFilter(year, month, Bill.DATE_FIELD)));
    }

    /**
     * Retrieves a list of filtered bills based on the specified year and month.
     *
     * @param year  the year to filter the bills by
     * @param month the month to filter the bills by
     * @return a list of filtered bills
     */
    public List<Bill> getAllFiltered(final Year year, final Month month) {
        return crudGetAllFiltered(DAOUser.getLoggedUser(), this::unpackBill, Documents.monthAndYearToFilter(year, month, Bill.DATE_FIELD));
    }

    /**
     * Retrieves a list of filtered bills based on the specified currency and year.
     *
     * @param curr the currency to filter by
     * @param year the year to filter by
     * @return the list of filtered bills
     */
    public List<Bill> getAllFiltered(final Currency curr, final Year year) {
        return crudGetAllFiltered(DAOUser.getLoggedUser(), this::unpackBill, and(eq(Bill.CURRENCY_FIELD, curr.getCurrencyCode()), Documents.yearToFilter(year, Bill.DATE_FIELD)));
    }

    /**
     * Retrieves a list of filtered bills for a specific year.
     *
     * @param year the year to filter the bills
     * @return a list of filtered bills
     */
    public List<Bill> getAllFiltered(final Year year) {
        return crudGetAllFiltered(DAOUser.getLoggedUser(), this::unpackBill, Documents.yearToFilter(year, Bill.DATE_FIELD));
    }

    /**
     * Retrieves all the filtered bills based on the given currency, beginning year, and end year.
     *
     * @param curr          the currency to filter the bills by
     * @param beginningYear the starting year to filter the bills by
     * @param endYear       the ending year to filter the bills by
     * @return a list of filtered bills
     */
    public List<Bill> getAllFiltered(final Currency curr, final Year beginningYear, final Year endYear) {
        return crudGetAllFiltered(DAOUser.getLoggedUser(), this::unpackBill, and(eq(Bill.CURRENCY_FIELD, curr.getCurrencyCode()), Documents.yearIntervalToFilter(beginningYear, endYear, Bill.DATE_FIELD)));
    }

    /**
     * Unpacks the items from a list of bills.
     *
     * @param list the list of bills
     * @return the list of unpacked items
     */
    public static List<ItemBill> unpackItems(final List<Bill> list) {
        return list.stream().flatMap(Bill::stream).toList();
    }

    /**
     * Unpacks a bill from the given document.
     *
     * @param document The document containing the bill.
     * @return An Optional that may contain the unpacked Bill object.
     */
    private Optional<Bill> unpackBill(final Document document) {
        var trio = Documents.parseBill(document);
        var first = trio.first();
        var second = trio.second();

        final var byId = daoPm.getById(second);
        if (first.isEmpty()) throw new RuntimeException("uh oh - first is empty");
        if (byId.isEmpty()) throw new RuntimeException("uh oh - second is empty");

        first.get().setPaymentMethod(byId.get());
        var itemList = trio.third().stream().map(pair -> {
            var item = pair.first();
            var tags = pair.second();
            item.setBill(first.get());
            item.addAll(daoTags.getAllById(tags.stream().map(id -> (Object) id).toList()));
            return item;
        }).toList();
        first.get().addAll(itemList);
        return first;
    }
}
