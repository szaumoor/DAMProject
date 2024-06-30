package com.szaumoor.rumple.model.entities.types.reports;

import com.szaumoor.rumple.db.utils.Pair;
import com.szaumoor.rumple.model.entities.Bill;
import com.szaumoor.rumple.model.entities.PaymentMethod;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class PaymentRatiosList extends RatioList<PaymentMethod> {
    private final List<Bill> billsToCheck;

    public PaymentRatiosList(List<PaymentMethod> items, final List<Bill> billsToCheck) {
        super(items);
        this.billsToCheck = billsToCheck;
    }

    @Override
    public void calculate() {
        var values = new ArrayList<Pair<PaymentMethod, BigDecimal>>();
        items.forEach(pm -> {
            var totalForPm = billsToCheck.stream()
                    .filter(bill -> bill.getPaymentMethod().equals(pm))
                    .map(Bill::getTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            values.add(new Pair<>(pm, totalForPm));
        });
        var total = values.stream()
                .map(Pair::second)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        stats.addAll(values.stream().map(pair -> {
            var first = pair.first();
            var sec = pair.second();
            return new Ratio(first.getName(), sec.divide(total, RoundingMode.UNNECESSARY).doubleValue());
        }).toList());
    }
}
