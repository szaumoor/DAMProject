package com.szaumoor.rumple.model.entities.types.reports.lists;

import com.szaumoor.rumple.model.entities.Bill;

import java.math.BigDecimal;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Class to calculate monthly totals for a list of bills
 */
public class MonthlyTotalsList extends TotalsList<Bill> {
    /**
     * Adds bills to the list
     *
     * @param bills Bills to add
     */
    public MonthlyTotalsList(final List<Bill> bills) {
        super(bills);
    }

    /**
     * Calculates the totals of expenses for each month
     */
    @Override
    public void calculate() {
        Arrays.stream(Month.values()).collect(Collectors.toList()).forEach(month -> {
            var totalForMonth = items.stream()
                    .filter(item -> item.getDate().getMonth().equals(month))
                    .map(Bill::getTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            if (totalForMonth.compareTo(BigDecimal.ZERO) > 0)
                stats.add(new Total(month.getDisplayName(TextStyle.SHORT, Locale.getDefault()), totalForMonth));
        });
    }
}
