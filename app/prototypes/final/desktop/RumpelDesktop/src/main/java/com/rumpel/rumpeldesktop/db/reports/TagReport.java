package com.rumpel.rumpeldesktop.db.reports;

import com.rumpel.rumpeldesktop.db.DAOBill;
import com.rumpel.rumpeldesktop.db.DAOTag;
import com.szaumoor.rumple.model.entities.Bill;
import com.szaumoor.rumple.model.entities.Tag;
import com.szaumoor.rumple.model.entities.types.reports.*;
import com.szaumoor.rumple.model.entities.types.reports.lists.StatList;
import com.szaumoor.rumple.model.entities.types.reports.lists.TagTotalsList;
import com.szaumoor.rumple.model.entities.types.reports.lists.TotalsList;

import java.time.Month;
import java.time.Year;
import java.util.Currency;
import java.util.List;

import static com.rumpel.rumpeldesktop.db.DAOBill.unpackItems;

/**
 * Class that encapsulates a report for expenses (bills) associated with tags and is able to produce data for in the for of ratios and totals
 */
public class TagReport extends AbstractReport implements TotalReport<Tag> {
    private final List<Tag> tags;

    /**
     * No-args constructor that gets automatically populated with all tags
     */
    public TagReport() {
        tags = new DAOTag().getAll();
    }

    /**
     * Constructor that takes the passed payment methods and uses it for the calculations
     *
     * @param tags Tags to use
     */
    public TagReport(final List<Tag> tags) {
        this.tags = tags;
    }

    //not supported for now
    @Override
    public StatList<TotalsList.Total, Tag> getTotalsInWeek(final Currency curr, final Year year, final Month month, Week week) {
        throw new UnsupportedOperationException();
    }

    /**
     * Retrieves the list of totals for a given currency, year, and month.
     *
     * @param curr  the currency to filter the totals by
     * @param year  the year to filter the totals by
     * @param month the month to filter the totals by
     * @return the list of totals for the specified currency, year, and month
     */
    @Override
    public StatList<TotalsList.Total, Tag> getTotalsInMonth(final Currency curr, final Year year, final Month month) {
        var list = !infoSet ? new DAOBill().getAllFiltered(curr, year, month) : bills;
        return returnList(list);
    }

    /**
     * Retrieves the list of total statistics for a given currency and year.
     *
     * @param curr The currency to filter the statistics by.
     * @param year The year to filter the statistics by.
     * @return The list of total statistics for the given currency and year.
     */
    @Override
    public StatList<TotalsList.Total, Tag> getTotalsInYear(final Currency curr, final Year year) {
        var list = !infoSet ? new DAOBill().getAllFiltered(curr, year) : bills;
        return returnList(list);
    }

    /**
     * Retrieves the list of total statistics for a specific currency, between two given years.
     *
     * @param curr          the currency to filter the totals by
     * @param beginningYear the starting year of the range
     * @param endYear       the ending year of the range
     * @return the list of total statistics within the specified currency and year range
     */
    @Override
    public StatList<TotalsList.Total, Tag> getTotalsInYears(final Currency curr, final Year beginningYear, final Year endYear) {
        var list = !infoSet ? new DAOBill().getAllFiltered(curr, beginningYear, endYear) : bills;
        return returnList(list);
    }

    /**
     * Generates a list of tag totals based on a given list of bills.
     *
     * @param list the list of bills
     * @return the list of tag totals
     */
    private TagTotalsList returnList(final List<Bill> list) {
        var items = unpackItems(list);
        var tagTotalsList = new TagTotalsList(tags, items);
        tagTotalsList.calculate();
        return tagTotalsList;
    }
}
