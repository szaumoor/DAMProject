package com.szaumoor.rumple.model.entities.types.reports.lists;

import com.szaumoor.rumple.model.entities.Bill;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static com.szaumoor.utils.Strings.capitalize;

/**
 * Class to calculate weekday totals for a list of bills
 */
public class WeekdayTotalsList extends TotalsList<Bill> {
    /**
     * Adds bills to the list
     *
     * @param bills Bills to add
     */
    public WeekdayTotalsList(final List<Bill> bills) {
        super(bills);
    }

    /**
     * Calculates the totals of expenses for each day of the week
     */
    @Override
    public void calculate() {
        Arrays.stream(DayOfWeek.values()).forEach(day -> {
            var totalPerDay = items.stream()
                    .filter(bill -> bill.getDate().getDayOfWeek().equals(day))
                    .map(Bill::getTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            if (totalPerDay.compareTo(BigDecimal.ZERO) > 0) stats.add(new Total(capitalize(day.getDisplayName(TextStyle.SHORT, Locale.getDefault())), totalPerDay));
        });
    }
}
