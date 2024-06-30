package com.rumpel.rumpelandroid.db;

import static com.rumpel.rumpelandroid.db.utils.Filters.and;
import static com.rumpel.rumpelandroid.db.utils.Filters.eq;

import android.content.Context;

import com.rumpel.rumpelandroid.db.utils.Documents;
import com.szaumoor.rumple.db.utils.Outcome;
import com.szaumoor.rumple.model.entities.Bill;
import com.szaumoor.rumple.model.entities.ItemBill;

import org.bson.Document;

import java.time.Month;
import java.time.Year;
import java.util.Currency;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * DAO class that handles Bill entities and objects.
 */
public final class DAOBill extends AbstractPluralDAO<Bill> {
    private final DAOPaymentMethods daoPm;
    private final DAOTags daoTags;

    /**
     * Constructor that initializes the basic fields and retrieves the collection.
     */
    public DAOBill(final Context context) {
        super(context, Bill.COLL_BILLS);
        daoPm = new DAOPaymentMethods(context);
        daoTags = new DAOTags(context);
    }

    /**
     * Retrieves an Optional object containing a Bill based on the given id object.
     *
     * @param o The object used to find the Bill.
     * @return An Optional object that may contain the found Bill.
     */
    @Override
    public Optional<Bill> get(final Object o) {
        return crudGet(eq(o), this::unpackBill, false);
    }

    /**
     * Retrieves an Optional object containing a Bill based on the given id object.
     * It's functionally equivalent to {@link #get(Object)}
     *
     * @param o The object used to find the Bill.
     * @return An Optional object that may contain the found Bill.
     */
    @Override
    public Optional<Bill> getById(final Object o) {
        return get(o);
    }

    /**
     * Retrieves all the bills.
     *
     * @return a list of bills
     */
    @Override
    public List<Bill> getAll() {
        return crudGetAll(this::unpackBill);
    }


    @Override // unsupported for now, no need
    public List<Bill> getAllById(final List<Object> list) {
        throw new UnsupportedOperationException();
    }

    @Override // unsupported for now, no need
    public Outcome insertAll(final List<Bill> list) {
        throw new UnsupportedOperationException();
    }

    @Override // unsupported for now, no need
    public Outcome deleteAll(final List<Bill> list) {
        throw new UnsupportedOperationException();
    }

    @Override // unsupported for now
    public Outcome deleteAll() {
        throw new UnsupportedOperationException();
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
        return crudModify(bill, Documents::billToDocument);
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
     * Retrieves a list of filtered bills based on the specified year and month.
     *
     * @param year  the year to filter the bills by
     * @param month the month to filter the bills by
     * @return a list of filtered bills
     */
    public List<Bill> getAllFiltered(final Year year, final Month month) {
        return crudGetAllByFilter(this::unpackBill, Documents.monthAndYearToFilter(year, month, Bill.DATE_FIELD));
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
        return crudGetAllByFilter(this::unpackBill, and(eq(Bill.CURRENCY_FIELD, curr.getCurrencyCode()), Documents.monthAndYearToFilter(year, month, Bill.DATE_FIELD)));
    }
    /**
     * Retrieves a list of filtered bills for a specific year.
     *
     * @param year the year to filter the bills
     * @return a list of filtered bills
     */
    public List<Bill> getAllFiltered(final Year year) {
        return crudGetAllByFilter(this::unpackBill, Documents.yearToFilter(year, Bill.DATE_FIELD));
    }
    /**
     * Retrieves a list of filtered bills based on the specified currency and year.
     *
     * @param currency the currency to filter by
     * @param year the year to filter by
     * @return the list of filtered bills
     */
    public List<Bill> getAllFiltered(final Currency currency, final Year year) {
        return crudGetAllByFilter(this::unpackBill, and(eq(Bill.CURRENCY_FIELD, currency.getCurrencyCode()), Documents.yearToFilter(year, Bill.DATE_FIELD)));
    }
    /**
     * Retrieves all the filtered bills based on the given currency, beginning year, and end year.
     *
     * @param currency          the currency to filter the bills by
     * @param beginningYear the starting year to filter the bills by
     * @param endYear       the ending year to filter the bills by
     * @return a list of filtered bills
     */
    public List<Bill> getAllFiltered(final Currency currency, final Year beginningYear, final Year endYear) {
        return crudGetAllByFilter(this::unpackBill, and(eq(Bill.CURRENCY_FIELD, currency.getCurrencyCode()), Documents.yearIntervalToFilter(beginningYear, endYear, Bill.DATE_FIELD)));
    }
    /**
     * Unpacks the items from a list of bills.
     *
     * @param list the list of bills
     * @return the list of unpacked items
     */
    public static List<ItemBill> unpackItems(final List<Bill> list) {
        return list.stream().flatMap(Bill::stream).collect(Collectors.toList());
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
        var pmId = trio.second();

        var opPm = daoPm.getById(pmId);
        if (first.isEmpty() || opPm.isEmpty()) throw new RuntimeException();

        first.get().setPaymentMethod(opPm.get());
        var itemList = trio.third().stream().map(pair -> {
            var item = pair.first();
            var tags = pair.second();
            item.setBill(first.get());
            item.addAll(daoTags.getAllById(tags.stream().map(id -> (Object) id).collect(Collectors.toList())));
            return item;
        }).collect(Collectors.toList());
        first.get().addAll(itemList);
        return first;
    }
}
