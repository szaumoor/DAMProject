package com.rumpel.rumpeldesktop.db.reports;

import com.rumpel.rumpeldesktop.db.DAOBill;
import com.rumpel.rumpeldesktop.db.DAOPaymentMethod;
import com.szaumoor.rumple.model.entities.Bill;
import com.szaumoor.rumple.model.entities.PaymentMethod;
import com.szaumoor.rumple.model.entities.types.reports.AbstractReport;
import com.szaumoor.rumple.model.entities.types.reports.RatioReport;
import com.szaumoor.rumple.model.entities.types.reports.TotalReport;
import com.szaumoor.rumple.model.entities.types.reports.Week;
import com.szaumoor.rumple.model.entities.types.reports.lists.*;

import java.time.Month;
import java.time.Year;
import java.util.Currency;
import java.util.List;

/**
 * Class that encapsulates a report for expenses (bills) associated with payment methods and is able to produce data for in the for of ratios and totals
 */
public class PaymentMethodReport extends AbstractReport implements RatioReport<PaymentMethod>, TotalReport<PaymentMethod> {
    private final List<PaymentMethod> paymentMethods;

    /**
     * No-args constructor that gets automatically populated with all payment methods
     */
    public PaymentMethodReport() {
        paymentMethods = new DAOPaymentMethod().getAll();
    }

    /**
     * Constructor that takes the passed payment methods and uses it for the calculations
     *
     * @param paymentMethods Payment methods to use
     */
    public PaymentMethodReport(final List<PaymentMethod> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    //unsupported for now
    @Override
    public StatList<RatioList.Ratio, PaymentMethod> getRatiosInWeek(final Currency curr, final Year year, final Month month, final Week week) {
        throw new UnsupportedOperationException();
    }

    /**
     * Retrieves the list of ratios for a specific currency, year, and month.
     *
     * @param curr  the currency to filter the ratios by
     * @param year  the year to filter the ratios by
     * @param month the month to filter the ratios by
     * @return the list of ratios for the given currency, year, and month
     */
    @Override
    public StatList<RatioList.Ratio, PaymentMethod> getRatiosInMonth(final Currency curr, final Year year, final Month month) {
        var list = !infoSet ? new DAOBill().getAllFiltered(curr, year, month) : bills;
        return returnRatioList(list);
    }

    /**
     * Returns a StatList of ratios for a given currency and year.
     *
     * @param curr the currency for which the ratios are requested
     * @param year the year for which the ratios are requested
     * @return a StatList of ratios for the given currency and year
     */
    @Override
    public StatList<RatioList.Ratio, PaymentMethod> getRatiosInYear(final Currency curr, final Year year) {
        var list = !infoSet ? new DAOBill().getAllFiltered(curr, year) : bills;
        return returnRatioList(list);
    }

    /**
     * Retrieves the list of ratios for the specified currency, beginning year, and end year.
     *
     * @param curr          the currency for which ratios are retrieved
     * @param beginningYear the starting year for the ratios
     * @param endYear       the ending year for the ratios
     * @return the list of ratios for the specified currency, beginning year, and end year
     */
    @Override
    public StatList<RatioList.Ratio, PaymentMethod> getRatiosInYears(final Currency curr, final Year beginningYear, final Year endYear) {
        var list = !infoSet ? new DAOBill().getAllFiltered(curr, beginningYear, endYear) : bills;
        return returnRatioList(list);
    }

    //unsupported for now
    @Override
    public StatList<TotalsList.Total, PaymentMethod> getTotalsInWeek(final Currency curr, final Year year, final Month month, final Week week) {
        throw new UnsupportedOperationException();
    }

    /**
     * Retrieves the totals for a specific month.
     *
     * @param curr  the currency to retrieve the totals in
     * @param year  the year of the totals
     * @param month the month of the totals
     * @return the list of totals for the specified month
     */
    @Override
    public StatList<TotalsList.Total, PaymentMethod> getTotalsInMonth(final Currency curr, final Year year, final Month month) {
        var list = !infoSet ? new DAOBill().getAllFiltered(curr, year, month) : bills;
        return returnTotalList(list);
    }

    /**
     * Retrieves the total statistics for a given currency and year.
     *
     * @param curr the currency to filter by
     * @param year the year to filter by
     * @return the list of total statistics
     */
    @Override
    public StatList<TotalsList.Total, PaymentMethod> getTotalsInYear(final Currency curr, final Year year) {
        var list = !infoSet ? new DAOBill().getAllFiltered(curr, year) : bills;
        return returnTotalList(list);
    }

    /**
     * Retrieves the totals of payments made in the specified range of years.
     *
     * @param curr          the currency in which the totals should be calculated
     * @param beginningYear the starting year of the range
     * @param endYear       the ending year of the range
     * @return the list of totals for each payment method
     */
    @Override
    public StatList<TotalsList.Total, PaymentMethod> getTotalsInYears(final Currency curr, final Year beginningYear, final Year endYear) {
        var list = !infoSet ? new DAOBill().getAllFiltered(curr, beginningYear, endYear) : bills;
        return returnTotalList(list);
    }

    /**
     * Generates a PaymentTotalsList based on the provided list of bills. Added for code reuse.
     *
     * @param list the list of bills to calculate the payment totals from
     * @return the generated PaymentTotalsList with calculated payment totals
     */
    private PaymentTotalsList returnTotalList(final List<Bill> list) {
        var totalsList = new PaymentTotalsList(paymentMethods, list);
        totalsList.calculate();
        return totalsList;
    }

    /**
     * Generates a PaymentRatiosList based on the provided list of bills. Added for code reuse.
     *
     * @param list the list of bills to calculate the payment ratios from
     * @return the generated PaymentTotalsList with calculated payment ratios
     */
    private PaymentRatioList returnRatioList(final List<Bill> list) {
        var totalsList = new PaymentRatioList(paymentMethods, list);
        totalsList.calculate();
        return totalsList;
    }
}
