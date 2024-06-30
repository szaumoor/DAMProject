package com.rumpel.rumpeldesktop.mvc.controllers;

import com.rumpel.rumpeldesktop.db.DAOBill;
import com.rumpel.rumpeldesktop.db.DAOPaymentMethod;
import com.rumpel.rumpeldesktop.db.DAOTag;
import com.rumpel.rumpeldesktop.fxutils.prefs.Preferences;
import com.szaumoor.rumple.model.entities.Bill;
import com.szaumoor.rumple.model.entities.types.reports.PaymentRatiosList;
import com.szaumoor.rumple.model.entities.types.reports.RatioList;
import com.szaumoor.rumple.model.entities.types.reports.TagTotalsList;
import com.szaumoor.rumple.model.entities.types.reports.TotalsList;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;

public class ChartViewController implements Initializable {

    public BorderPane mainPane;
    @FXML
    private PieChart chart;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TagTotalsList list = new TagTotalsList(
                new DAOTag().getAll(),
                new DAOBill().getAll().stream().map(bill -> bill.stream().toList()).flatMap(Collection::stream).toList());
        list.calculate();
        CategoryAxis catAxis = new CategoryAxis(FXCollections.observableArrayList(list.getStats().stream().map(TotalsList.Total::name).toList()));
        NumberAxis numAxis = new NumberAxis("Expenses", 0.0, 1000.0, 100.0);
        BarChart<String, Number> barChart = new BarChart<>
                (
                    catAxis, numAxis
                );

        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.getData().addAll(
                list.getStats().stream()
                        .map(total -> new XYChart.Data<String, Number>(total.name(), total.total()))
                        .toList());
        barChart.getData().add(series1);

//
//                list.getStats().stream().map(total -> new XYChart.Data<String, Number>(total.name() + " 2", total.total())).toList());
//        barChart.getData().addAll(series1);

//        PaymentRatiosList list2 = new PaymentRatiosList(
//                new DAOPaymentMethod()
//                        .getAll()
//                        .stream()
//                        .toList(), new DAOBill().getAll());
//        list2.calculate();
//        CategoryAxis catAxis = new CategoryAxis(FXCollections.observableArrayList(list2.getStats().stream().map(RatioList.Ratio::name).toList()));
//        NumberAxis numAxis = new NumberAxis("Expenses", 0.0, 100.0, 10.0);
//        BarChart<String, Number> barChart = new BarChart<>
//                (
//                        catAxis, numAxis
//                );
//
//        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
//        series2.getData().addAll(
//                list2.getStats().stream().map(total -> new XYChart.Data<String, Number>(total.name(), total.asPercentage())).toList());
//        barChart.getData().addAll(series2);

        mainPane.setBottom(barChart);
        chart.getData().addAll(
                new PieChart.Data("Item 1", 25),
                new PieChart.Data("Item 2", 25),
                new PieChart.Data("Item 3", 25),
                new PieChart.Data("Item 4", 25)
        );
    }
}
