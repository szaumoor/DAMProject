package com.rumpel.rumpeldesktop.db.reports;

import com.rumpel.rumpeldesktop.db.DAOBill;
import com.szaumoor.rumple.model.entities.Bill;
import com.szaumoor.rumple.model.entities.types.reports.AbstractReport;
import com.szaumoor.rumple.model.entities.types.reports.RatioReport;
import com.szaumoor.rumple.model.entities.types.reports.TotalReport;
import com.szaumoor.rumple.model.entities.types.reports.Week;
import com.szaumoor.rumple.model.entities.types.reports.lists.*;

import java.time.Month;
import java.time.Year;
import java.util.Currency;

/**
 * Class that encapsulates a report for expenses (bills) and is able to produce data for in the for of ratios and totals
 */
public class ExpensesReport extends AbstractReport implements RatioReport<Bill>, TotalReport<Bill> {

    @Override
    public StatList<RatioList.Ratio, Bill> getRatiosInWeek(final Currency curr, final Year year, final Month month, final Week week) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    /**
     * Creates an appropriate StatList object for the given year and month and calculates the ratios for each day of the week,
     * returning it afterward.
     *
     * @param year  The year to get the data for
     * @param month The month to get the data for
     * @return StatList object for the given year and month holding the calculated ratios.
     */
    @Override
    public StatList<RatioList.Ratio, Bill> getRatiosInMonth(final Currency curr, final Year year, final Month month) {
        var list = new WeekdayRatioList(!infoSet ? new DAOBill().getAllFiltered(curr, year, month) : bills);
        list.calculate();
        return list;
    }

    /**
     * Creates an appropriate StatList object for the given year and calculates the ratios for month of that year,
     * returning it afterward.
     *
     * @param year The year to get the data for
     * @return StatList object for the given year holding the calculated ratios.
     */
    @Override
    public StatList<RatioList.Ratio, Bill> getRatiosInYear(final Currency curr, final Year year) {
        var list = !infoSet ? new DAOBill().getAllFiltered(curr, year) : bills;
        var ratiosList = new MonthlyRatioList(list);
        ratiosList.calculate();
        return ratiosList;
    }

    /**
     * Creates an appropriate StatList object for the given year interval and calculates the ratios for each year,
     * returning it afterward.
     *
     * @param beginningYear The beginning year to get the data for, inclusive
     * @param endYear       The end year to get the data for, inclusive
     * @return StatList object for the given year and month holding the calculated ratios.
     */
    @Override
    public StatList<RatioList.Ratio, Bill> getRatiosInYears(final Currency curr, final Year beginningYear, final Year endYear) {
        var list = !infoSet ? new DAOBill().getAllFiltered(curr, beginningYear, endYear) : bills;
        var ratiosList = new YearlyRatioList(list);
        ratiosList.calculate();
        return ratiosList;
    }

    @Override
    public StatList<TotalsList.Total, Bill> getTotalsInWeek(final Currency curr, final Year year, final Month month, final Week week) {
        return null;
    }

    /**
     * Creates an appropriate StatList object for the given year and month and calculates the totals for each day of the week,
     * returning it afterward.
     *
     * @param year  The year to get the data for
     * @param month The month to get the data for
     * @return StatList object for the given year and month holding the calculated totals.
     */
    @Override
    public StatList<TotalsList.Total, Bill> getTotalsInMonth(final Currency curr, final Year year, final Month month) {
        var list = !infoSet ? new DAOBill().getAllFiltered(curr, year, month) : bills;
        var totalsList = new WeekdayTotalsList(list);
        totalsList.calculate();
        return totalsList;
    }

    /**
     * Creates an appropriate StatList object for the given year and calculates the totals for month of that year,
     * returning it afterward.
     *
     * @param year The year to get the data for
     * @return StatList object for the given year holding the calculated totals.
     */
    @Override
    public StatList<TotalsList.Total, Bill> getTotalsInYear(final Currency curr, final Year year) {
        var list = !infoSet ? new DAOBill().getAllFiltered(curr, year) : bills;
        var totalsList = new MonthlyTotalsList(list);
        totalsList.calculate();
        return totalsList;
    }

    /**
     * Creates an appropriate StatList object for the given year interval and calculates the totals for each year,
     * returning it afterward.
     *
     * @param beginningYear The beginning year to get the data for, inclusive
     * @param endYear       The end year to get the data for, inclusive
     * @return StatList object for the given year and month holding the calculated totals.
     */
    @Override
    public StatList<TotalsList.Total, Bill> getTotalsInYears(final Currency curr, final Year beginningYear, final Year endYear) {
        var list = !infoSet ? new DAOBill().getAllFiltered(curr, beginningYear, endYear) : bills;
        var totalsList = new YearlyTotalsList(list);
        totalsList.calculate();
        return totalsList;
    }
}
