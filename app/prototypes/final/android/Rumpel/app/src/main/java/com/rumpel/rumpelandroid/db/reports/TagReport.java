package com.rumpel.rumpelandroid.db.reports;

import com.rumpel.rumpelandroid.db.DAOBill;
import com.rumpel.rumpelandroid.db.DAOTags;
import com.szaumoor.rumple.model.entities.Tag;
import com.szaumoor.rumple.model.entities.types.reports.*;
import com.szaumoor.rumple.model.entities.types.reports.lists.StatList;
import com.szaumoor.rumple.model.entities.types.reports.lists.TagTotalsList;
import com.szaumoor.rumple.model.entities.types.reports.lists.TotalsList;

import java.time.Month;
import java.time.Year;
import java.util.Currency;
import java.util.List;

import static com.rumpel.rumpelandroid.db.DAOBill.unpackItems;

import android.content.Context;
/**
 * Report for total expenses associated with tags.
 */
public class TagReport extends AbstractAndroidReport implements TotalReport<Tag> {
    private final List<Tag> tags;

    /**
     * Construct a tag report with the provided context.
     *
     * @param context the context
     */
    public TagReport(final Context context) {
        super(context);
        this.tags = new DAOTags(context).getAll();
    }

    /**
     * Construct a tag report with the provided context and list of payment methods.
     *
     * @param context the context
     * @param tags the payment methods
     */
    public TagReport(final Context context, final List<Tag> tags) {
        super(context);
        this.tags = tags;
    }

    @Override // not supported yet
    public StatList<TotalsList.Total, Tag> getTotalsInWeek(final Currency curr, final Year year, final Month month, Week week) {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates an appropriate StatList object for the given year and month and calculates the totals for each day of the week,
     * returning it afterward.
     *
     * @param year The year to get the data for
     * @param month The month to get the data for
     * @return  StatList object for the given year and month holding the calculated totals.
     */
    @Override
    public StatList<TotalsList.Total, Tag> getTotalsInMonth(final Currency curr, final Year year, final Month month) {
        var list = !infoSet ? new DAOBill(context).getAllFiltered(curr, year, month) : bills;
        var items = unpackItems(list);
        var totalsList = new TagTotalsList(tags, items);
        totalsList.calculate();
        return totalsList;
    }

    /**
     * Creates an appropriate StatList object for the given year and calculates the totals for month of that year,
     * returning it afterward.
     *
     * @param year The year to get the data for
     * @return  StatList object for the given year holding the calculated totals.
     */
    @Override
    public StatList<TotalsList.Total, Tag> getTotalsInYear(final Currency curr, final  Year year) {
        var list = !infoSet ? new DAOBill(context).getAllFiltered(curr, year) : bills;
        var items = unpackItems(list);
        var tagTotalsList = new TagTotalsList(tags, items);
        tagTotalsList.calculate();
        return tagTotalsList;
    }

    /**
     * Creates an appropriate StatList object for the given year interval and calculates the totals for each year,
     * returning it afterward.
     *
     * @param beginningYear The beginning year to get the data for, inclusive
     * @param endYear The end year to get the data for, inclusive
     * @return  StatList object for the given year and month holding the calculated totals.
     */
    @Override
    public StatList<TotalsList.Total, Tag> getTotalsInYears(final Currency curr, final Year beginningYear, final Year endYear) {
        var list = !infoSet ? new DAOBill(context).getAllFiltered(curr, beginningYear, endYear) : bills;
        var items = unpackItems(list);
        var tagTotalsList = new TagTotalsList(tags, items);
        tagTotalsList.calculate();
        return tagTotalsList;
    }
}
