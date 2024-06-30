package com.szaumoor.rumple.model.entities.types.reports;

import com.szaumoor.rumple.model.entities.types.reports.lists.StatList;
import com.szaumoor.rumple.model.entities.types.reports.lists.TotalsList;
import com.szaumoor.rumple.model.interfaces.Entity;

import java.time.Month;
import java.time.Year;
import java.util.Currency;

/**
 * Interface for any report of entities that calculate totals, and not ratios.
 *
 * @param <I> The type of the entities
 */
public interface TotalReport<I extends Entity> {
    StatList<TotalsList.Total, I> getTotalsInWeek(final Currency curr, final Year year, final Month month, final Week week);

    StatList<TotalsList.Total, I> getTotalsInMonth(final Currency curr, final Year year, final Month month);

    StatList<TotalsList.Total, I> getTotalsInYear(final Currency curr, final Year year);

    StatList<TotalsList.Total, I> getTotalsInYears(final Currency curr, final Year beginningYear, final Year endYear);
}
