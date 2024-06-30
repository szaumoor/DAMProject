package com.szaumoor.rumple.model.entities.types.reports.lists;

import com.szaumoor.rumple.db.utils.Pair;
import com.szaumoor.rumple.model.entities.Bill;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static com.szaumoor.utils.Strings.capitalize;

/**
 * Class to calculate weekday ratios for a list of bills
 */
public class WeekdayRatioList extends RatioList<Bill> {
    /**
     * Adds bills to the list
     *
     * @param bills Bills to add
     */
    public WeekdayRatioList(final List<Bill> bills) {
        super(bills);
    }

    /**
     * Calculates the ratios of expenses for each day of the week
     */
    @Override
    public void calculate() {
        var values = new ArrayList<Pair<DayOfWeek, BigDecimal>>();
        var days = DayOfWeek.values();
        Arrays.stream(days).forEach(dayOfWeek -> {
            var total = items.stream()
                    .filter(bill -> bill.getDate().getDayOfWeek() == dayOfWeek)
                    .map(Bill::getTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            if (total.compareTo(BigDecimal.ZERO) > 0) values.add(new Pair<>(dayOfWeek, total));
        });
        var total = getTotalFromPairs(values);
        stats.addAll(values.stream().map(pair -> {
            var first = pair.first();
            var sec = pair.second();
            return new Ratio(capitalize(first.getDisplayName(TextStyle.SHORT, Locale.getDefault())), divide(sec, total));
        }).toList());
    }
}
