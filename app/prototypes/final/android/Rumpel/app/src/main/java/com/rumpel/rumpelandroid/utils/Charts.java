package com.rumpel.rumpelandroid.utils;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.szaumoor.rumple.model.entities.types.reports.lists.TotalsList;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to create and set configured charts to views in the app
 */
public final class Charts {
    /**
     * Private constructor to prevent instantiation
     */
    private Charts() {
        throw new AssertionError("Utility class");
    }

    /**
     * Creates a pie chart and sets it to the provided view
     *
     * @param view The view to set the chart to
     * @param title The title of the chart
     * @param legend The legend of the chart
     * @param stats The list of stats the chart uses
     */
    public static void pie(final AnyChartView view, final String title, final String legend, final List<TotalsList.Total> stats) {
        var chart = AnyChart.pie();
        var listData = new ArrayList<DataEntry>(stats.size());
        for (int i = 0; i < stats.size(); i++)
            listData.add(new ValueDataEntry(stats.get(i).name(), stats.get(i).total()));
        chart.data(listData);
        chart.labels().position("outside");
        chart.legend().title().text(legend).padding(0d, 0d, 10d, 0d);
        chart.title(title);
        chart.legend().title().enabled(true);
        chart.legend().position("center-bottom")
                .itemsLayout(LegendLayout.HORIZONTAL)
                .align(Align.CENTER);
        view.setChart(chart);
    }

    /**
     * Creates a bar chart and sets it to the provided view
     *
     * @param view The view to set the chart to
     * @param title The title of the chart
     * @param legend The legend of the chart
     * @param stats The list of stats the chart uses
     */
    public static void bar(final AnyChartView view, final String title, final String legend, final List<TotalsList.Total> stats) {
        var chart = AnyChart.bar();
        var listData = new ArrayList<DataEntry>(stats.size());
        for (int i = 0; i < stats.size(); i++)
            listData.add(new ValueDataEntry(stats.get(i).name(), stats.get(i).total()));
        chart.data(listData);
        chart.labels().position("outside");
        chart.legend().title().text(legend).padding(0d, 0d, 10d, 0d);
        chart.title(title);
        chart.legend().title().enabled(true);
        chart.legend().position("center-bottom")
                .itemsLayout(LegendLayout.HORIZONTAL)
                .align(Align.CENTER);
        view.setChart(chart);
    }
}
