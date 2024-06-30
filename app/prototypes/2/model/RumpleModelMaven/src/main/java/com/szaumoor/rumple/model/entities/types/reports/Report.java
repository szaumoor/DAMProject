package com.szaumoor.rumple.model.entities.types.reports;

import com.szaumoor.rumple.model.interfaces.Entity;

import java.time.Month;
import java.time.Year;

public interface Report<I extends Entity> {

    StatList<RatioList.Ratio, I> getRatiosPerWeek(final Month month, Week week);
    StatList<RatioList.Ratio, I> getRatiosPerMonth(final Month month);
    StatList<RatioList.Ratio, I> getRatiosPerYear(final Year year);

    StatList<TotalsList.Total, I> getTotalsPerWeek(final Month month, Week week);
    StatList<TotalsList.Total, I> getTotalsPerMonth(final Month month);
    StatList<TotalsList.Total, I> getTotalsPerYear(final Year year);
}
