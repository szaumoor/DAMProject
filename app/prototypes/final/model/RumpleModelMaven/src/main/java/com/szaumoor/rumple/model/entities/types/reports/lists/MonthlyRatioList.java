package com.szaumoor.rumple.model.entities.types.reports.lists;

import com.szaumoor.rumple.db.utils.Pair;
import com.szaumoor.rumple.model.entities.Bill;

import java.math.BigDecimal;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Class to calculate monthly ratios for a list of bills
 */
public class MonthlyRatioList extends RatioList<Bill> {
    /**
     * Adds bills to the list
     *
     * @param bills Bills to add
     */
    public MonthlyRatioList(final List<Bill> bills) {
        super(bills);
    }

    /**
     * Calculates the ratios of expenses for each month
     */
    @Override
    public void calculate() {
        var values = new ArrayList<Pair<Month, BigDecimal>>();
        Arrays.stream(Month.values()).forEach(month -> {
            var total = items.stream()
                    .filter(bill -> bill.getDate().getMonth().equals(month))
                    .map(Bill::getTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            if (total.compareTo(BigDecimal.ZERO) > 0) values.add(new Pair<>(month, total));
        });
        var total = getTotalFromPairs(values);
        stats.addAll(values.stream().map(pair -> {
            var first = pair.first();
            var sec = pair.second();
            return new Ratio(first.getDisplayName(TextStyle.SHORT, Locale.getDefault()), divide(sec, total));
        }).toList());
    }
}
