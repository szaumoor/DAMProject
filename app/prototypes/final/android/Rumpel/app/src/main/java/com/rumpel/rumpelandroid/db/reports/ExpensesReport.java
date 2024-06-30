package com.rumpel.rumpelandroid.db.reports;

import android.content.Context;

import com.rumpel.rumpelandroid.db.DAOBill;
import com.szaumoor.rumple.model.entities.Bill;
import com.szaumoor.rumple.model.entities.types.reports.RatioReport;
import com.szaumoor.rumple.model.entities.types.reports.TotalReport;
import com.szaumoor.rumple.model.entities.types.reports.Week;
import com.szaumoor.rumple.model.entities.types.reports.lists.MonthlyRatioList;
import com.szaumoor.rumple.model.entities.types.reports.lists.MonthlyTotalsList;
import com.szaumoor.rumple.model.entities.types.reports.lists.RatioList;
import com.szaumoor.rumple.model.entities.types.reports.lists.StatList;
import com.szaumoor.rumple.model.entities.types.reports.lists.TotalsList;
import com.szaumoor.rumple.model.entities.types.reports.lists.WeekdayRatioList;
import com.szaumoor.rumple.model.entities.types.reports.lists.WeekdayTotalsList;
import com.szaumoor.rumple.model.entities.types.reports.lists.YearlyRatioList;
import com.szaumoor.rumple.model.entities.types.reports.lists.YearlyTotalsList;

import java.time.Month;
import java.time.Year;
import java.util.Currency;

/**
 * Report for totals and ratios for expenses. Ratios don't seem to be needed for the AnyCharts library, regardless.
 */
public class ExpensesReport extends AbstractAndroidReport implements RatioReport<Bill>, TotalReport<Bill> {
    /**
     * Construct a expenses report with the provided context.
     *
     * @param context the context
     */
    public ExpensesReport(final Context context) {
        super(context);
    }

    @Override // not supported so far
    public StatList<RatioList.Ratio, Bill> getRatiosInWeek(final Currency curr, final Year year, final Month month, final Week week) {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates an appropriate StatList object for the given year and month and calculates the ratios for each day of the week,
     * returning it afterward.
     *
     * @param year The year to get the data for
     * @param month The month to get the data for
     * @return  StatList object for the given year and month holding the calculated ratios.
     */
    @Override
    public StatList<RatioList.Ratio, Bill> getRatiosInMonth(final Currency curr, final Year year, final Month month) {
        var list = new WeekdayRatioList(new DAOBill(context).getAllFiltered(curr, year, month));
        list.calculate();
        return list;
    }
    /**
     * Creates an appropriate StatList object for the given year and calculates the ratios for month of that year,
     * returning it afterward.
     *
     * @param year The year to get the data for
     * @return  StatList object for the given year holding the calculated ratios.
     */
    @Override
    public StatList<RatioList.Ratio, Bill> getRatiosInYear(final Currency curr, final Year year) {
        var list = !infoSet ? new DAOBill(context).getAllFiltered(curr, year) : bills;
        var ratiosList = new MonthlyRatioList(list);
        ratiosList.calculate();
        return ratiosList;
    }

    /**
     * Creates an appropriate StatList object for the given year interval and calculates the ratios for each year,
     * returning it afterward.
     *
     * @param beginningYear The beginning year to get the data for, inclusive
     * @param endYear The end year to get the data for, inclusive
     * @return  StatList object for the given years holding the calculated ratios.
     */
    @Override
    public StatList<RatioList.Ratio, Bill> getRatiosInYears(final Currency curr, final Year beginningYear, final Year endYear) {
        var list = !infoSet ? new DAOBill(context).getAllFiltered(curr, beginningYear, endYear) : bills;
        var ratiosList = new YearlyRatioList(list);
        ratiosList.calculate();
        return ratiosList;
    }

    @Override // not supported so far
    public StatList<TotalsList.Total, Bill> getTotalsInWeek(final Currency curr, final Year year, final Month month, final Week week) {
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
    public StatList<TotalsList.Total, Bill> getTotalsInMonth(final Currency curr, final Year year, final Month month) {
        var list = !infoSet ? new DAOBill(context).getAllFiltered(curr, year, month) : bills;
        var totalsList = new WeekdayTotalsList(list);
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
    public StatList<TotalsList.Total, Bill> getTotalsInYear(final Currency curr, final Year year) {
        var list = !infoSet ? new DAOBill(context).getAllFiltered(curr, year) : bills;
        var totalsList = new MonthlyTotalsList(list);
        totalsList.calculate();
        return totalsList;
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
    public StatList<TotalsList.Total, Bill> getTotalsInYears(final Currency curr, final Year beginningYear, final Year endYear) {
        var list = !infoSet ? new DAOBill(context).getAllFiltered(curr, beginningYear, endYear) : bills;
        var totalsList = new YearlyTotalsList(list);
        totalsList.calculate();
        return totalsList;
    }
}
