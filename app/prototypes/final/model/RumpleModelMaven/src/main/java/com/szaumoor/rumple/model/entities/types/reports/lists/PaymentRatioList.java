package com.szaumoor.rumple.model.entities.types.reports.lists;

import com.szaumoor.rumple.db.utils.Pair;
import com.szaumoor.rumple.model.entities.Bill;
import com.szaumoor.rumple.model.entities.PaymentMethod;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to calculate monthly ratios of payment methods for a list of bills
 */
public class PaymentRatioList extends RatioList<PaymentMethod> {
    private final List<Bill> billsToCheck;

    /**
     * Creates this list with the provided bills and payment methods to check
     *
     * @param paymentMethods Payment methods to check
     * @param bills Bills to check
     */
    public PaymentRatioList(final List<PaymentMethod> paymentMethods, final List<Bill> bills) {
        super(paymentMethods);
        this.billsToCheck = bills;
    }

    /**
     *  Calculates the ratios of expenses per payment method for each month
     */
    @Override
    public void calculate() {
        var values = new ArrayList<Pair<PaymentMethod, BigDecimal>>();
        items.forEach(pm -> {
            var total = billsToCheck.stream()
                    .filter(bill -> bill.getPaymentMethod().equals(pm))
                    .map(Bill::getTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            if (total.compareTo(BigDecimal.ZERO) > 0) values.add(new Pair<>(pm, total));
        });
        var total = values.stream().map(Pair::second).reduce(BigDecimal.ZERO, BigDecimal::add);
        stats.addAll(values.stream().map(pair -> {
            var first = pair.first();
            var sec = pair.second();
            return new Ratio(first.getName(), divide(sec, total));
        }).toList());
    }
}
