package com.rumpel.rumpeldesktop.mvc.controllers;

import com.rumpel.rumpeldesktop.db.DAOBill;
import com.rumpel.rumpeldesktop.db.reports.ExpensesReport;
import com.rumpel.rumpeldesktop.db.reports.PaymentMethodReport;
import com.rumpel.rumpeldesktop.db.reports.TagReport;
import com.rumpel.rumpeldesktop.fxutils.Buttons;
import com.rumpel.rumpeldesktop.fxutils.IO;
import com.rumpel.rumpeldesktop.fxutils.Tasks;
import com.rumpel.rumpeldesktop.mvc.views.utils.Tooltips;
import com.rumpel.rumpeldesktop.fxutils.prefs.Preferences;
import com.rumpel.rumpeldesktop.fxutils.threads.FXTaskRunner;
import com.szaumoor.rumple.model.entities.Bill;
import com.szaumoor.rumple.model.entities.PaymentMethod;
import com.szaumoor.rumple.model.entities.Tag;
import com.szaumoor.rumple.model.entities.types.reports.AbstractReport;
import com.szaumoor.rumple.model.entities.types.reports.ReportType;
import com.szaumoor.rumple.model.entities.types.reports.lists.RatioList;
import com.szaumoor.rumple.model.entities.types.reports.lists.StatList;
import com.szaumoor.rumple.model.entities.types.reports.lists.TotalsList;
import com.szaumoor.rumple.model.interfaces.Entity;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.Month;
import java.time.Year;
import java.time.format.TextStyle;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static com.rumpel.rumpeldesktop.fxutils.Translator.getString;
import static javafx.collections.FXCollections.observableArrayList;

public final class ChartViewController implements Initializable {
    @FXML
    private Label topText;
    @FXML
    private BorderPane mainPane;
    @FXML
    private ToolBar toolBar;
    @FXML
    private Button pdfExport;
    @FXML
    private Button imageExport;

    private static final Logger logger = LogManager.getLogger(ChartViewController.class);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Buttons.setIconButton(pdfExport, "pdf.png");
        pdfExport.setTooltip(Tooltips.getDefaultTooltip(getString("export_as_pdf")));
        pdfExport.setOnAction(e -> export(true));

        Buttons.setIconButton(imageExport, "image.png");
        imageExport.setTooltip(Tooltips.getDefaultTooltip(getString("export_as_image")));
        imageExport.setOnAction(e -> export(false));
    }

    public void setParams(final Currency curr, final Year year, final Month month, final ReportType type) {
        switch (type) {
            case TAG_STATS_MONTH -> generateTagCharts(curr, year, month);
            case TAG_STATS_YEAR -> generateTagChartsMonthly(curr, year);
            case TAG_STATS_ACROSS_YEARS -> generateTagChartsYearly(curr, year);
            case PAYMENT_STATS_MONTH -> generatePmInMonthCharts(curr, year, month);
            case PAYMENT_STATS_YEAR -> generatePmMonthlyCharts(curr, year);
            case PAYMENT_STATS_ACROSS_YEARS -> generatePmYearlyCharts(curr, year);
            case EXPENSES_YEAR -> generateExpensesMonthlyCharts(curr, year);
            case EXPENSES_YEARLY -> generateExpensesYearlyCharts(curr, year);
            case EXPENSES_IN_MONTH -> generateExpensesInMonthCharts(curr, year, month);
        }
    }

    @FXML
    private void export(final boolean pdf) {
        var fileChooser = new FileChooser();
        fileChooser.setTitle(getString("export") + (pdf ? " PDF" : " PNG"));
        fileChooser.setInitialDirectory(new File(Preferences.instance.getDefaultOutputDirectory()));
        if (pdf) fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
        else fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image", "*.png"));
        File file = fileChooser.showSaveDialog(mainPane.getScene().getWindow());
        try {
            if (file != null) {
                if (pdf) IO.extractPdf(file, mainPane);
                else IO.extractImage(file, mainPane);
            }
        } catch (IOException ex) {
            logger.error("Export failed!", ex);
        }
    }

    private void generateTagCharts(final Currency curr, final Year year, final Month month) {
        var tagReport = new TagReport();
        AtomicReference<StatList<TotalsList.Total, Tag>> list = new AtomicReference<>();
        var taskRunner = new FXTaskRunner(Tasks.createTask(() -> list.set(tagReport.getTotalsInMonth(curr, year, month))),
                b -> {
                    topText.setText(year + " - " + month.getDisplayName(TextStyle.FULL, Locale.getDefault()));
                    mainPane.setCenter(getBarChart(curr, list.get()));
                    toolBar.setVisible(true);
                });
        taskRunner.execute(120, TimeUnit.SECONDS);
    }

    private void generateTagChartsMonthly(final Currency curr, final Year year) {
        var tagReport = new TagReport();
        AtomicReference<StatList<TotalsList.Total, Tag>> list = new AtomicReference<>();
        var listOfTasks = generateTasksForMonthlyCharts(tagReport, curr, year);
        var taskBills = new FXTaskRunner(listOfTasks, aBoolean -> {
            var taskRunner = new FXTaskRunner(Tasks.createTask(() -> list.set(tagReport.getTotalsInYear(curr, year))),
                    b -> {
                        topText.setText(year.toString());
                        mainPane.setCenter(getBarChart(curr, list.get()));
                        toolBar.setVisible(true);
                    });
            taskRunner.execute(120, TimeUnit.SECONDS);
        });
        taskBills.execute(120, TimeUnit.SECONDS);
    }

    private void generateTagChartsYearly(final Currency curr, final Year year) {
        var tagReport = new TagReport();
        AtomicReference<StatList<TotalsList.Total, Tag>> list = new AtomicReference<>();
        var listOfTasks = generateTasksForYearlyCharts(tagReport, curr, year);
        var billRetrieverThread = new FXTaskRunner(listOfTasks, aBoolean -> {
            var taskRunner = new FXTaskRunner(Tasks.createTask(() -> list.set(tagReport.getTotalsInYears(curr, year, Year.now()))),
                    b -> {
                        topText.setText(year + " - " + Year.now());
                        mainPane.setCenter(getBarChart(curr, list.get()));
                        toolBar.setVisible(true);
                    });
            taskRunner.execute(120, TimeUnit.SECONDS);
        });
        billRetrieverThread.execute(120, TimeUnit.SECONDS);
    }

    private void generatePmInMonthCharts(final Currency curr, final Year year, final Month month) {
        var pmReport = new PaymentMethodReport();
        AtomicReference<StatList<TotalsList.Total, PaymentMethod>> totalsList = new AtomicReference<>();
        AtomicReference<List<RatioList.Ratio>> ratiosList = new AtomicReference<>();
        var taskRunner = new FXTaskRunner(List.of(Tasks.createTask(() -> totalsList.set(pmReport.getTotalsInMonth(curr, year, month))),
                        Tasks.createTask(() -> ratiosList.set(pmReport.getRatiosInMonth(curr, year, month).getStats()))),
                b -> {
                    topText.setText(year + " - " + month.getDisplayName(TextStyle.FULL, Locale.getDefault()));
                    setChartsForNonTags(ratiosList.get(), totalsList.get(), curr);
                });
        taskRunner.execute(120, TimeUnit.SECONDS);
    }

    private void generatePmMonthlyCharts(final Currency curr, final Year year) {
        var pmReport = new PaymentMethodReport();
        AtomicReference<StatList<TotalsList.Total, PaymentMethod>> totalsList = new AtomicReference<>();
        AtomicReference<List<RatioList.Ratio>> ratiosList = new AtomicReference<>();
        var listOfTasks = generateTasksForMonthlyCharts(pmReport, curr, year);
        var taskBills = new FXTaskRunner(listOfTasks, aBoolean -> {
            var taskRunner = new FXTaskRunner(List.of(Tasks.createTask(() -> totalsList.set(pmReport.getTotalsInYear(curr, year))),
                            Tasks.createTask(() -> ratiosList.set(pmReport.getRatiosInYear(curr, year).getStats()))),
                    b -> {
                        topText.setText(year.toString());
                        setChartsForNonTags(ratiosList.get(), totalsList.get(), curr);
                    });
            taskRunner.execute(120, TimeUnit.SECONDS);
        });
        taskBills.execute(120, TimeUnit.SECONDS);
    }

    private void generatePmYearlyCharts(final Currency curr, final Year year) {
        var pmReport = new PaymentMethodReport();
        AtomicReference<StatList<TotalsList.Total, PaymentMethod>> totalsList = new AtomicReference<>();
        AtomicReference<List<RatioList.Ratio>> ratiosList = new AtomicReference<>();
        var listOfTasks = generateTasksForYearlyCharts(pmReport, curr, year);
        var billRetrieverThread = new FXTaskRunner(listOfTasks, aBoolean -> {
            var taskRunner = new FXTaskRunner(List.of(Tasks.createTask(() -> totalsList.set(pmReport.getTotalsInYears(curr, year, Year.now()))),
                            Tasks.createTask(() -> ratiosList.set(pmReport.getRatiosInYears(curr, year, Year.now()).getStats()))),
                    b -> {
                        topText.setText(year + " - " + Year.now());
                        setChartsForNonTags(ratiosList.get(), totalsList.get(), curr);
                    });
            taskRunner.execute(120, TimeUnit.SECONDS);
        });
        billRetrieverThread.execute(120, TimeUnit.SECONDS);
    }

    private void generateExpensesMonthlyCharts(final Currency curr, final Year year) {
        var monthlyReport = new ExpensesReport();
        AtomicReference<StatList<TotalsList.Total, Bill>> totalsList = new AtomicReference<>();
        AtomicReference<List<RatioList.Ratio>> ratiosList = new AtomicReference<>();
        var listOfTasks = generateTasksForMonthlyCharts(monthlyReport, curr, year);
        var taskBills = new FXTaskRunner(listOfTasks, aBoolean -> {
            var taskRunner = new FXTaskRunner(List.of(Tasks.createTask(() -> totalsList.set(monthlyReport.getTotalsInYear(curr, year))),
                            Tasks.createTask(() -> ratiosList.set(monthlyReport.getRatiosInYear(curr, year).getStats()))),
                    b -> {
                        topText.setText(year.toString());
                        setChartsForNonTags(ratiosList.get(), totalsList.get(), curr);
                    });
            taskRunner.execute(120, TimeUnit.SECONDS);
        });
        taskBills.execute(120, TimeUnit.SECONDS);
    }

    private void generateExpensesInMonthCharts(final Currency curr, final Year year, final Month month) {
        var monthlyReport = new ExpensesReport();
        AtomicReference<StatList<TotalsList.Total, Bill>> totalsList = new AtomicReference<>();
        AtomicReference<List<RatioList.Ratio>> ratiosList = new AtomicReference<>();
        var billCollector = new FXTaskRunner(Tasks.createTask(() -> monthlyReport.addAll(new DAOBill().getAllFiltered(year, month))), b1 -> {
            var taskRunner = new FXTaskRunner(List.of(Tasks.createTask(() -> totalsList.set(monthlyReport.getTotalsInMonth(curr, year, month))),
                    Tasks.createTask(() -> ratiosList.set(monthlyReport.getRatiosInMonth(curr, year, month).getStats()))),
                    b2 -> {
                        topText.setText(year + " - " + month.getDisplayName(TextStyle.FULL, Locale.getDefault()));
                        setChartsForNonTags(ratiosList.get(), totalsList.get(), curr);
                    });
            taskRunner.execute(120, TimeUnit.SECONDS);
        });
        billCollector.execute(120, TimeUnit.SECONDS);
    }

    private void generateExpensesYearlyCharts(final Currency curr, final Year year) {
        var yearlyReport = new ExpensesReport();
        AtomicReference<StatList<TotalsList.Total, Bill>> totalsList = new AtomicReference<>();
        AtomicReference<List<RatioList.Ratio>> ratiosList = new AtomicReference<>();
        var listOfTasks = generateTasksForYearlyCharts(yearlyReport, curr, year);
        var billRetrieverThread = new FXTaskRunner(listOfTasks, aBoolean -> {
            var taskRunner = new FXTaskRunner(List.of(Tasks.createTask(() -> totalsList.set(yearlyReport.getTotalsInYears(curr, year, Year.now()))),
                            Tasks.createTask(() -> ratiosList.set(yearlyReport.getRatiosInYears(curr, year, Year.now()).getStats()))),
                    b -> {
                        topText.setText(year + " - " + Year.now());
                        setChartsForNonTags(ratiosList.get(), totalsList.get(), curr);
                    });
            taskRunner.execute(120, TimeUnit.SECONDS);
        });
        billRetrieverThread.execute(120, TimeUnit.SECONDS);
    }


    private <T extends Entity> BarChart<String, Number> getBarChart(final Currency curr, final StatList<TotalsList.Total, T> list) {
        var maxInList = list.getStats().stream().map(TotalsList.Total::total).max(BigDecimal::compareTo).orElseThrow();
        var catAxis = new CategoryAxis(observableArrayList(list.getStats().stream().map(TotalsList.Total::name).toList()));
        var numAxis = new NumberAxis(getString("expenses_in") + " " + curr.getSymbol(), 0.0, maxInList.doubleValue(), maxInList.doubleValue() / 10.0);
        BarChart<String, Number> barChart = new BarChart<>(catAxis, numAxis);
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.getData().addAll(list.getStats().stream()
                        .map(total -> new XYChart.Data<String, Number>(total.name(), total.total()))
                        .toList());
        barChart.getData().add(series1);
        barChart.setAnimated(false);
        return barChart;
    }

    private <T extends Entity> void setChartsForNonTags(final List<RatioList.Ratio> pieChartList, StatList<TotalsList.Total, T> list, final Currency curr) {
        mainPane.setCenter(new PieChart(observableArrayList(pieChartList.stream().map(ratio -> new PieChart.Data(ratio.name(), ratio.asPercentage())).toList())));
        mainPane.setBottom(getBarChart(curr, list));
        toolBar.setVisible(true);
    }

    private List<Task<Void>> generateTasksForYearlyCharts(final AbstractReport report, final Currency curr, final Year year) {
        int numberOfThreads = Year.now().getValue() - year.getValue() + 1;
        var listOfTasks = new ArrayList<Task<Void>>(numberOfThreads * 12);
        for (int i = 0; i < numberOfThreads; i++) {
            int finalI = i;
            for (int j = 0; j < 12; j++) {
                int finalJ = j;
                listOfTasks.add(Tasks.createTask(() -> report.addAll(new DAOBill().getAllFiltered(curr, year.plusYears(finalI), Month.JANUARY.plus(finalJ)))));
            }
        }
        return listOfTasks;
    }

    private List<Task<Void>> generateTasksForMonthlyCharts(final AbstractReport report, final Currency curr, final Year year) {
        var listOfTasks = new ArrayList<Task<Void>>(12);
        for (int i = 0; i < 12; i++) {
            int finalI = i;
            listOfTasks.add(Tasks.createTask(() -> report.addAll(new DAOBill().getAllFiltered(curr, year, Month.JANUARY.plus(finalI)))));
        }
        return listOfTasks;
    }
}