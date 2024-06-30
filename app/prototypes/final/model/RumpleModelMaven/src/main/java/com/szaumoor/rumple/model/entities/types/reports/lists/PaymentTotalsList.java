package com.szaumoor.rumple.model.entities.types.reports.lists;

import com.szaumoor.rumple.model.entities.Bill;
import com.szaumoor.rumple.model.entities.PaymentMethod;

import java.math.BigDecimal;
import java.util.List;
/**
 * Class to calculate totals of payment methods for a list of bills
 */
public class PaymentTotalsList extends TotalsList<PaymentMethod> {
    private final List<Bill> billsToCheck;

    /**
     * Creates this list with the provided bills and payment methods to check
     *
     * @param paymentMethods Payment methods to check
     * @param bills Bills to check
     */
    public PaymentTotalsList(final List<PaymentMethod> paymentMethods, final List<Bill> bills) {
        super(paymentMethods);
        this.billsToCheck = bills;
    }
    /**
     *  Calculates the totals of expenses per payment method
     */
    @Override
    public void calculate() {
        items.forEach(pm -> {
            var totalForTag = billsToCheck.stream()
                    .filter(bill -> bill.getPaymentMethod().equals(pm))
                    .map(Bill::getTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            stats.add(new Total(pm.getName(), totalForTag));
        });
    }
}
