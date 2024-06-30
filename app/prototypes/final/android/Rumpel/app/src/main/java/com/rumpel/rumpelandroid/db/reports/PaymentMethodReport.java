package com.rumpel.rumpelandroid.db.reports;

import android.content.Context;

import com.rumpel.rumpelandroid.db.DAOBill;
import com.rumpel.rumpelandroid.db.DAOPaymentMethods;
import com.szaumoor.rumple.model.entities.PaymentMethod;
import com.szaumoor.rumple.model.entities.types.reports.RatioReport;
import com.szaumoor.rumple.model.entities.types.reports.TotalReport;
import com.szaumoor.rumple.model.entities.types.reports.Week;
import com.szaumoor.rumple.model.entities.types.reports.lists.PaymentRatioList;
import com.szaumoor.rumple.model.entities.types.reports.lists.PaymentTotalsList;
import com.szaumoor.rumple.model.entities.types.reports.lists.RatioList;
import com.szaumoor.rumple.model.entities.types.reports.lists.StatList;
import com.szaumoor.rumple.model.entities.types.reports.lists.TotalsList;

import java.time.Month;
import java.time.Year;
import java.util.Currency;
import java.util.List;

/**
 * Report for totals and ratios for payment methods. Ratios don't seem to be needed for the AnyCharts library, regardless.
 */
public class PaymentMethodReport extends AbstractAndroidReport implements RatioReport<PaymentMethod>, TotalReport<PaymentMethod> {
    private final List<PaymentMethod> paymentMethods;

    /**
     * Construct a payment method report with the provided context.
     *
     * @param context the context
     */
    public PaymentMethodReport(final Context context) {
        super(context, false);
        this.paymentMethods = new DAOPaymentMethods(context).getAll();
    }

    /**
     * Construct a payment method report with the provided context and list of payment methods.
     *
     * @param context the context
     * @param paymentMethods the payment methods
     */
    public PaymentMethodReport(final Context context, final List<PaymentMethod> paymentMethods) {
        super(context);
        this.paymentMethods = paymentMethods;
    }

    @Override // not supported yet
    public StatList<RatioList.Ratio, PaymentMethod> getRatiosInWeek(final Currency curr, final Year year, final Month month, final Week week) {
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
    public StatList<RatioList.Ratio, PaymentMethod> getRatiosInMonth(final Currency curr, final Year year, final Month month) {
        var bills = infoSet ? super.bills : new DAOBill(context).getAllFiltered(curr,year, month);
        var ratiosList = new PaymentRatioList(paymentMethods, bills);
        ratiosList.calculate();
        return ratiosList;
    }

    /**
     * Creates an appropriate StatList object for the given year and calculates the ratios for month of that year,
     * returning it afterward.
     *
     * @param year The year to get the data for
     * @return  StatList object for the given year holding the calculated ratios.
     */
    @Override
    public StatList<RatioList.Ratio, PaymentMethod> getRatiosInYear(final Currency curr, final Year year) {
        var bills = infoSet ? super.bills : new DAOBill(context).getAllFiltered(curr, year);
        var ratiosList = new PaymentRatioList(paymentMethods, bills);
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
    public StatList<RatioList.Ratio, PaymentMethod> getRatiosInYears(final Currency curr, final Year beginningYear, final Year endYear) {
        var bills = infoSet ? super.bills : new DAOBill(context).getAllFiltered(curr, beginningYear, endYear);
        var ratiosList = new PaymentRatioList(paymentMethods, bills);
        ratiosList.calculate();
        return ratiosList;
    }

    @Override // not supported yet
    public StatList<TotalsList.Total, PaymentMethod> getTotalsInWeek(final Currency curr, final Year year, final Month month, final Week week) {
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
    public StatList<TotalsList.Total, PaymentMethod> getTotalsInMonth(final Currency curr, final Year year, final Month month) {
        var bills = infoSet ? super.bills : new DAOBill(context).getAllFiltered(curr, year, month);
        var totalsList = new PaymentTotalsList(paymentMethods, bills);
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
    public StatList<TotalsList.Total, PaymentMethod> getTotalsInYear(final Currency curr, final Year year) {
        var bills = infoSet ? super.bills : new DAOBill(context).getAllFiltered(curr, year);
        var totalsList = new PaymentTotalsList(paymentMethods, bills);
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
    public StatList<TotalsList.Total, PaymentMethod> getTotalsInYears(final Currency curr, final Year beginningYear, final Year endYear) {
        var bills = infoSet ? super.bills : new DAOBill(context).getAllFiltered(curr, beginningYear, endYear);
        var totalsList = new PaymentTotalsList(paymentMethods, bills);
        totalsList.calculate();
        return totalsList;
    }
}
