package com.szaumoor.rumple.model.entities.types.reports.lists;

import com.szaumoor.rumple.db.utils.Pair;
import com.szaumoor.rumple.model.entities.Bill;

import java.math.BigDecimal;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static java.lang.String.valueOf;

/**
 * Class to calculate yearly ratios for a list of bills
 */
public class YearlyRatioList extends RatioList<Bill> {
    /**
     * Adds bills to the list
     *
     * @param bills Bills to add
     */
    public YearlyRatioList(final List<Bill> bills) {
        super(bills);
    }

    /**
     * Calculates the ratios of expenses for each year
     */
    @Override
    public void calculate() {
        var values = new ArrayList<Pair<Year, BigDecimal>>();
        var years = IntStream.range(1970, Year.now().getValue() + 1);
        years.forEach(year -> {
            var total = items.stream()
                    .filter(bill -> bill.getDate().getYear() == year)
                    .map(Bill::getTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            if (total.compareTo(BigDecimal.ZERO) > 0) values.add(new Pair<>(Year.of(year), total));
        });

        var total = getTotalFromPairs(values);
        stats.addAll(values.stream().map(pair -> {
            var first = pair.first();
            var sec = pair.second();
            return new Ratio(valueOf(first.getValue()), divide(sec, total));
        }).toList());
    }
}
