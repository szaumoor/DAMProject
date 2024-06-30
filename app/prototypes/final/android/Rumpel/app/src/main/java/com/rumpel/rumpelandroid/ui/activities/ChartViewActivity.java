package com.rumpel.rumpelandroid.ui.activities;

// needs refactoring, too much repeated code that could be generified
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChartView;
import com.rumpel.rumpelandroid.R;
import com.rumpel.rumpelandroid.db.DAOBill;
import com.rumpel.rumpelandroid.db.reports.AbstractAndroidReport;
import com.rumpel.rumpelandroid.db.reports.ExpensesReport;
import com.rumpel.rumpelandroid.db.reports.PaymentMethodReport;
import com.rumpel.rumpelandroid.db.reports.TagReport;
import com.rumpel.rumpelandroid.utils.Charts;
import com.rumpel.rumpelandroid.utils.threads.BackgroundTask;
import com.rumpel.rumpelandroid.utils.threads.TaskRunner;
import com.szaumoor.rumple.model.entities.types.reports.ReportType;

import java.time.Month;
import java.time.Year;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

/**
 * Activity to view charts.
 */
public class ChartViewActivity extends AppCompatActivity {
    private AnyChartView chartView;
    private TextView waitingLabel;
    private ProgressBar loadingBar;
    private ReportType reportType;
    private Currency currency;

    /**
     * Helper menu to identify chart types
     */
    public enum ChartType {PIE, BAR}
    private ChartType chartType;
    private Year year;

    private void unpackBundle(final Bundle bundle) {
        if (bundle != null) {
            var constant = bundle.getString(ReportType.class.getSimpleName());
            reportType = ReportType.valueOf(constant);
            chartType = ChartType.valueOf(bundle.getString("CHART"));
            currency = Currency.getInstance(bundle.getString("CURRENCY"));
            year = Year.of(bundle.getInt("YEAR"));
            decideChart(bundle);
        } else Log.e(getClass().getSimpleName(), "Bundle is null");
    }

    private void decideChart(final Bundle bundle) {
        switch(reportType) {
            case TAG_STATS_MONTH -> {
                var month = Month.of(bundle.getInt("MONTH"));
                prepareForMonthChartTags(year, month);
            }
            case TAG_STATS_YEAR -> prepareForYearChartTags(year);
            case TAG_STATS_ACROSS_YEARS -> prepareForYearlyChartTags(year);
            case PAYMENT_STATS_MONTH -> {
                var month = Month.of(bundle.getInt("MONTH"));
                prepareForMonthChartPm(year, month);
            }
            case PAYMENT_STATS_YEAR -> prepareForYearChartPm(year);
            case PAYMENT_STATS_ACROSS_YEARS -> prepareForYearlyChartPm(year);
            case EXPENSES_YEAR -> prepareForMonthlyExpenses(year);
            case EXPENSES_YEARLY -> prepareForYearlyExpenses(year);
            case EXPENSES_IN_MONTH -> {
                var month = Month.of(bundle.getInt("MONTH"));
                prepareForMonthExpenses(year, month);
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_view);
        unpackBundle(getIntent().getExtras());
        loadingBar = findViewById(R.id.progressBar);
        waitingLabel = findViewById(R.id.waitingLabel);
        chartView = findViewById(R.id.theChart);
    }

    private void prepareForMonthChartTags(final Year year, final Month month) {
        var report = new TagReport(this);
        var task = new BackgroundTask(() -> report.addAll(new DAOBill(this).getAllFiltered(currency, year, month)), null);
        TaskRunner.executeAsync(new BackgroundTask(task, () -> {
            var stats = report.getTotalsInMonth(currency, year, month).getStats();
            Charts.pie(chartView, year + " - " + month.getDisplayName(TextStyle.FULL, Locale.getDefault()), getString(R.string.tags), stats);
            chartView.setVisibility(View.VISIBLE);
            loadingBar.setVisibility(View.GONE);
            waitingLabel.setVisibility(View.GONE);
        }));
    }

    private void prepareForYearChartTags(final Year year) {
        var report = new TagReport(this);
        var listOfTasks = generateListOfTasksForYear(report, year);
        TaskRunner.executeAsync((new BackgroundTask(() -> TaskRunner.executeAsync(listOfTasks), () -> {
            var stats = report.getTotalsInYear(currency, year).getStats();
            Charts.pie(chartView, year.toString(), getString(R.string.tags), stats);
            chartView.setVisibility(View.VISIBLE);
            loadingBar.setVisibility(View.GONE);
            waitingLabel.setVisibility(View.GONE);
        })));
    }

    private void prepareForYearlyChartTags(final Year beginningYear) {
        var report = new TagReport(this);
        var listOfTasks = generateTasksForYearlyCharts(report, beginningYear);
        TaskRunner.executeAsync((new BackgroundTask(() -> TaskRunner.executeAsync(listOfTasks), () -> {
            var stats = report.getTotalsInYear(currency, beginningYear).getStats();
            Charts.pie(chartView, beginningYear + " - " + Year.now(), getString(R.string.tags), stats);
            chartView.setVisibility(View.VISIBLE);
            loadingBar.setVisibility(View.GONE);
            waitingLabel.setVisibility(View.GONE);
        })));
    }

    private void prepareForMonthChartPm(final Year year, final Month month) {
        var report = new PaymentMethodReport(this);
        var task = new BackgroundTask(() -> report.addAll(new DAOBill(this).getAllFiltered(currency, year, month)), null);
        TaskRunner.executeAsync(new BackgroundTask(task, () -> {
            var stats = report.getTotalsInMonth(currency, year, month).getStats();
            if (chartType == ChartType.PIE) Charts.pie(chartView, year + " - " + month.getDisplayName(TextStyle.FULL, Locale.getDefault()), getString(R.string.pms), stats);
            else Charts.bar(chartView, year + " - " + month.getDisplayName(TextStyle.FULL, Locale.getDefault()), getString(R.string.pms), stats);
            chartView.setVisibility(View.VISIBLE);
            loadingBar.setVisibility(View.GONE);
            waitingLabel.setVisibility(View.GONE);
        }));
    }

    private void prepareForYearChartPm(final Year year) {
        var report = new PaymentMethodReport(this);
        var listOfTasks = generateListOfTasksForYear(report, year);
        TaskRunner.executeAsync((new BackgroundTask(() -> TaskRunner.executeAsync(listOfTasks), () -> {
            var stats = report.getTotalsInYear(currency, year).getStats();
            if (chartType == ChartType.PIE) Charts.pie(chartView, year.toString(), getString(R.string.pms), stats);
            else Charts.bar(chartView, year.toString(), getString(R.string.pms), stats);
            chartView.setVisibility(View.VISIBLE);
            loadingBar.setVisibility(View.GONE);
            waitingLabel.setVisibility(View.GONE);
        })));
    }

    private void prepareForYearlyChartPm(final Year beginningYear) {
        var report = new PaymentMethodReport(this);
        var listOfTasks = generateTasksForYearlyCharts(report, beginningYear);
        TaskRunner.executeAsync((new BackgroundTask(() -> TaskRunner.executeAsync(listOfTasks), () -> {
            var stats = report.getTotalsInYear(currency, beginningYear).getStats();
            if (chartType == ChartType.PIE) Charts.pie(chartView, beginningYear + " - " + Year.now(), getString(R.string.pms), stats);
            else Charts.bar(chartView, beginningYear + " - " + Year.now(), getString(R.string.pms), stats);
            chartView.setVisibility(View.VISIBLE);
            loadingBar.setVisibility(View.GONE);
            waitingLabel.setVisibility(View.GONE);
        })));
    }

    private void prepareForMonthlyExpenses(final Year year) {
        var report = new ExpensesReport(this);
        var listOfTasks = generateListOfTasksForYear(report, year);
        TaskRunner.executeAsync(new BackgroundTask(() -> TaskRunner.executeAsync(listOfTasks), () -> {
            var stats = report.getTotalsInYear(currency, year).getStats();
            if (chartType == ChartType.PIE) Charts.pie(chartView, year.toString(), getString(R.string.months), stats);
            else Charts.bar(chartView, year.toString(), getString(R.string.months), stats);
            chartView.setVisibility(View.VISIBLE);
            loadingBar.setVisibility(View.GONE);
            waitingLabel.setVisibility(View.GONE);
        }));
    }

    private void prepareForYearlyExpenses(final Year year) {
        var report = new ExpensesReport(this);
        var listOfTasks = generateTasksForYearlyCharts(report, year);
        TaskRunner.executeAsync(new BackgroundTask(() -> TaskRunner.executeAsync(listOfTasks), () -> {
            var stats = report.getTotalsInYears(currency, year, Year.now()).getStats();
            if (chartType == ChartType.PIE) Charts.pie(chartView, year + " - " + Year.now(), getString(R.string.years), stats);
            else Charts.bar(chartView, year + " - " + Year.now() , getString(R.string.years), stats);
            chartView.setVisibility(View.VISIBLE);
            loadingBar.setVisibility(View.GONE);
            waitingLabel.setVisibility(View.GONE);
        }));
    }

    private void prepareForMonthExpenses(final Year year, final Month month) {
        var report = new ExpensesReport(this);
        var task = new BackgroundTask(() -> report.addAll(new DAOBill(this).getAllFiltered(currency, year, month)), null);
        TaskRunner.executeAsync(new BackgroundTask(task, () -> {
            var stats = report.getTotalsInMonth(currency, year, month).getStats();
            if (chartType == ChartType.PIE) Charts.pie(chartView, year + " - " + month.getDisplayName(TextStyle.FULL, Locale.getDefault()), getString(R.string.days_of_the_week), stats);
            else Charts.bar(chartView, year + " - " + month.getDisplayName(TextStyle.FULL, Locale.getDefault()) , getString(R.string.days_of_the_week), stats);
            chartView.setVisibility(View.VISIBLE);
            loadingBar.setVisibility(View.GONE);
            waitingLabel.setVisibility(View.GONE);
        }));
    }

    private List<BackgroundTask> generateListOfTasksForYear(final AbstractAndroidReport report, final Year year) {
        final var listOfTasks = new ArrayList<BackgroundTask>(12);
        var daoBill = new DAOBill(this);
        for (int i = 0; i < 12; i++) {
            int finalI = i;
            listOfTasks.add(new BackgroundTask(() -> report.addAll(daoBill.getAllFiltered(currency, year, Month.JANUARY.plus(finalI))), null));
        }
        return listOfTasks;
    }

    private List<BackgroundTask> generateTasksForYearlyCharts(final AbstractAndroidReport report, final Year year) {
        int numberOfThreads = Year.now().getValue() - year.getValue() + 1;
        var listOfTasks = new ArrayList<BackgroundTask>(numberOfThreads * 12);
        var daoBill = new DAOBill(this);
        for (int i = 0; i < numberOfThreads; i++) {
            int finalI = i;
            for (int j = 0; j < 12; j++) {
                int finalJ = j;
                listOfTasks.add(new BackgroundTask(() -> report.addAll(daoBill.getAllFiltered(currency, year.plusYears(finalI), Month.JANUARY.plus(finalJ))), null));
            }
        }
        return listOfTasks;
    }
}
