package com.szaumoor.rumple.model.entities.types.reports.lists;

import com.szaumoor.rumple.model.entities.Bill;

import java.math.BigDecimal;
import java.time.Year;
import java.util.List;
import java.util.stream.IntStream;

import static java.lang.String.valueOf;
/**
 * Class to calculate yearly ratios for a list of bills
 */
public class YearlyTotalsList extends TotalsList<Bill> {
    /**
     * Adds bills to the list
     *
     * @param bills Bills to add
     */
    public YearlyTotalsList(final List<Bill> bills) {
        super(bills);
    }

    /**
     * Calculates the ratios of expenses for each month
     */
    @Override
    public void calculate() {
        var years = IntStream.range(1970, Year.now().getValue() + 1);
        years.forEach(year -> {
            var totalForYear = items.stream()
                    .filter(item -> item.getDate().getYear() == year)
                    .map(Bill::getTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            if (totalForYear.compareTo(BigDecimal.ZERO) > 0) stats.add(new Total(valueOf(year), totalForYear));
        });
    }
}
