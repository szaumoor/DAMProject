package com.szaumoor.rumple.model.entities.types.reports;

import com.szaumoor.rumple.model.entities.types.reports.lists.RatioList;
import com.szaumoor.rumple.model.entities.types.reports.lists.StatList;
import com.szaumoor.rumple.model.interfaces.Entity;

import java.time.Month;
import java.time.Year;
import java.util.Currency;

/**
 * Interface for any report of entities that calculate ratios, and not totals.
 *
 * @param <I> The type of the entities
 */
public interface RatioReport<I extends Entity> {
    StatList<RatioList.Ratio, I> getRatiosInWeek(final Currency curr, final Year year, final Month month, final Week week);

    StatList<RatioList.Ratio, I> getRatiosInMonth(final Currency curr, final Year year, final Month month);

    StatList<RatioList.Ratio, I> getRatiosInYear(final Currency curr, final Year year);

    StatList<RatioList.Ratio, I> getRatiosInYears(final Currency curr, final Year beginningYear, final Year endYear);

}
