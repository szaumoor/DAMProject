package com.rumpel.rumpeldesktop.mvc.controllers;

import com.rumpel.rumpeldesktop.db.DAOBill;
import com.rumpel.rumpeldesktop.db.DAOUser;
import com.rumpel.rumpeldesktop.fxutils.*;
import com.rumpel.rumpeldesktop.fxutils.threads.FXTaskRunner;
import com.rumpel.rumpeldesktop.mvc.controllers.injectors.BillInjector;
import com.rumpel.rumpeldesktop.mvc.controllers.injectors.DateInjector;
import com.rumpel.rumpeldesktop.mvc.views.*;
import com.rumpel.rumpeldesktop.mvc.views.utils.Messages;
import com.rumpel.rumpeldesktop.mvc.views.utils.Tooltips;
import com.szaumoor.rumple.db.utils.Outcome;
import com.szaumoor.rumple.model.entities.Bill;
import com.szaumoor.rumple.utils.Dates;
import com.szaumoor.rumple.utils.Money;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static com.rumpel.rumpeldesktop.fxutils.Translator.getString;
import static com.rumpel.rumpeldesktop.mvc.views.utils.Alerts.confirmAndWait;
import static com.rumpel.rumpeldesktop.mvc.views.utils.Alerts.errorAndWait;
import static com.rumpel.rumpeldesktop.mvc.views.utils.ViewUtils.showLoginView;
import static java.util.Objects.isNull;

/**
 * Controller for the home view and all the functions
 */
public final class HomeController implements Initializable, BillInjector, DateInjector {
    @FXML
    private Button rightBtn;
    @FXML
    private Button leftBtn;
    @FXML
    private Button monthAndYear;
    @FXML
    private ToolBar toolbar;
    @FXML
    private Button editBtn;
    @FXML
    private Button refreshBtn;
    @FXML
    private Button addBtn;
    @FXML
    private Button deleteBtn;
    @FXML
    private MenuBar menuBar;
    @FXML
    private ProgressIndicator loading;
    @FXML
    private TableColumn<Bill, String> timeCol;
    @FXML
    private TableColumn<Bill, String> pmCol;
    @FXML
    private TableColumn<Bill, Integer> elemCol;
    @FXML
    private TableColumn<Bill, String> totalCol;
    @FXML
    private TableView<Bill> tableView;
    @FXML
    private BorderPane mainPane;

    private static final Logger logger = LogManager.getLogger();
    private DAOBill dao;
    private ObservableList<Bill> bills;
    private Year tableYear;
    private Month tableMonth;

    @FXML // todo: implement this for all manageable close operations
    private void closeApp() {
        Tasks.exit();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableYear = Year.now();
        tableMonth = LocalDate.now().getMonth();
        dao = new DAOBill();
        var loggedUser = DAOUser.getLoggedUser();
        if (isNull(loggedUser)) {
            logger.fatal("Couldn't find authenticated user");
            throw new RuntimeException("Critical error: couldn't find authenticated user");
        }
        configTable();
        Buttons.setIconButton(addBtn, "plus.png");
        addBtn.setTooltip(Tooltips.getDefaultTooltip(Translator.getString("add_new_bill")));
        Buttons.setIconButton(editBtn, "edit.png");
        editBtn.setTooltip(Tooltips.getDefaultTooltip(Translator.getString("edit_selected_bill")));
        Buttons.setIconButton(deleteBtn, "delete.png");
        deleteBtn.setTooltip(Tooltips.getDefaultTooltip(Translator.getString("delete_selected_bill")));
        Buttons.setIconButton(refreshBtn, "refresh.png");
        refreshBtn.setTooltip(Tooltips.getDefaultTooltip(Translator.getString("refresh_table")));
        Buttons.setIconButton(leftBtn, "left-arrow.png");
        leftBtn.setOnAction(event -> {
            if (tableYear.getValue() == 1970 && tableMonth == Month.JANUARY) return;
            tableYear = tableMonth == Month.JANUARY ? tableYear.minusYears(1) : tableYear;
            tableMonth = tableMonth.minus(1);
            refreshTable();
        });
        Buttons.setIconButton(rightBtn, "right-arrow.png");
        rightBtn.setOnAction(event -> {
            var now = LocalDate.now();
            if (now.getYear() == tableYear.getValue() && now.getMonth() == tableMonth) return;
            tableYear = tableMonth == Month.DECEMBER ? tableYear.plusYears(1) : tableYear;
            tableMonth = tableMonth.plus(1);
            refreshTable();
        });
        monthAndYear.setOnMouseClicked(event -> new SelectDateDialog(mainPane.getScene().getWindow(), this, tableYear, tableMonth).show());
        setDateLabel();
        Styles.applyFontSize(16, menuBar);
        Styles.applyFontSize(17, tableView);
        Styles.applyFontSize(16, FontWeight.BOLD, monthAndYear);

        refreshTable();
        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            tableView.requestFocus();
        });
        tableView.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.DELETE) {
                deleteBill();
            }
        });
        Platform.runLater(() -> mainPane.getScene().getWindow().setOnCloseRequest(event -> Tasks.exit()));
    }

    private void getBills() {
        bills = FXCollections.observableArrayList(dao.getAllFiltered(tableYear, tableMonth));
    }

    private void configTable() {
        var newItem = new MenuItem(getString("new_bill") + "...");
        newItem.setOnAction(event -> openNewBillDialog());
        var modify = new MenuItem(getString("edit"));
        modify.setOnAction(event -> editBillDialog());
        var delete = new MenuItem(getString("delete"));
        delete.setOnAction(event -> deleteBill());
        var menu = new ContextMenu(newItem, modify, delete);
        Styles.applyFontSize(16.0, menu);
        tableView.setContextMenu(menu);
        tableView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                editBillDialog();
            }
        });
        timeCol.setCellValueFactory(param -> new SimpleStringProperty(Dates.formatWithTime(param.getValue().getDate())));
        pmCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getPaymentMethod().getName()));
        elemCol.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().size()).asObject());
        totalCol.setCellValueFactory(param -> new SimpleStringProperty(Money.formatCurrency(param.getValue().getTotal(), param.getValue().getCurrency())));
        //Tables.sortByMoneyAmount(totalCol);
    }

    @FXML
    private void logout() {
        final var stage = (Stage) mainPane.getScene().getWindow();
        stage.setFullScreen(false);
        var username = DAOUser.getLoggedUser().getUsername().value();
        DAOUser.logout();
        logger.info("User " + username + " logged out at local date: " + LocalDateTime.now());
        showLoginView(stage);
    }

    @FXML
    private void openAboutDialog() {
        new AboutDialog(mainPane.getScene().getWindow()).show();
    }

    @FXML
    private void openTagsForm() {
        new TagDialog(mainPane.getScene().getWindow()).show();
    }

    @FXML
    public void openPmForm() {
        new PaymentMethodDialog(mainPane.getScene().getWindow()).show();
    }

    @FXML
    public void openBudgetsForm() {
        new BudgetDialog(mainPane.getScene().getWindow()).show();
    }

    @FXML
    public void openNewBillDialog() {
        new BillDialog(mainPane.getScene().getWindow(), this, null).show();
    }

    @FXML
    public void editBillDialog() {
        var bill = tableView.getSelectionModel().getSelectedItem();
        if (bill == null) errorAndWait(getString("no_item_selected"));
        else new BillDialog(mainPane.getScene().getWindow(), this, bill).show();
    }

    @FXML
    public void openAccountDialog() {
        new AccountDialog(mainPane.getScene().getWindow()).show();
    }

    @Override
    public void insertBill(final Bill bill) {
        var date = bill.getDate();
        if (tableYear.getValue() == date.getYear() && tableMonth.getValue() == date.getMonthValue()) {
            tableView.getItems().add(bill);
            showBudgetFeedback();
        }
    }

    @Override
    public void modifyBill(final Bill bill) {
        var date = bill.getDate();
        if (tableYear.getValue() == date.getYear() && tableMonth.getValue() == date.getMonthValue()) {
            var items = tableView.getItems();
            items.set(items.indexOf(bill), bill);
        }
    }

    @FXML
    public void deleteBill() {
        var selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) errorAndWait(getString("no_item_selected"));
        else {
            var option = confirmAndWait(getString("confirmation_delete_bill"));
            if (option.isPresent() && option.get() == ButtonType.OK) {
                var outcome = dao.delete(selected);
                if (outcome == Outcome.SUCCESS) tableView.getItems().remove(selected);
                else logger.error("Some error occurred when trying to delete it");
            }
        }
    }

    /**
     * Refreshes table with whatever information there is for the selected current month and year.
     * It will automatically check if there are applicable budgets for the selected month and year, and will
     * show a notification based on whether there are any applicable budgets and how many are in the warning
     * or danger zone.
     */
    @FXML
    public void refreshTable() {
        toolbar.setDisable(true);
        loading.setVisible(true);
        tableView.setVisible(false);
        var applicableBudgets = new AtomicLong(0);
        var budgetsInWarning = new AtomicLong(0);
        var budgetsInDanger = new AtomicLong(0);
        var tasker = new FXTaskRunner(
                Tasks.createTask(() -> {
                    getBills();
                    Tasks.checkBudgetStatus(bills, applicableBudgets, budgetsInWarning, budgetsInDanger, tableYear, tableMonth);
                }), b -> {
            if (b) {
                tableView.setItems(bills);
                loading.setVisible(false);
                tableView.getSelectionModel().clearSelection();
                tableView.setVisible(true);
                setDateLabel();
                showBudgetFeedback(applicableBudgets, budgetsInWarning, budgetsInDanger);
                toolbar.setDisable(false);

            } else errorAndWait(getString("check_your_conn"));
        });
        tasker.execute(30, TimeUnit.SECONDS);
    }

    private void showBudgetFeedback() {
        refreshBtn.setDisable(true);
        var applicableBudgets = new AtomicLong(0);
        var budgetsInWarning = new AtomicLong(0);
        var budgetsInDanger = new AtomicLong(0);
        showBudgetFeedback(applicableBudgets, budgetsInWarning, budgetsInDanger);
    }

    private void showBudgetFeedback(AtomicLong applicableBudgets, AtomicLong budgetsInWarning, AtomicLong budgetsInDanger) {
        Tasks.checkBudgetStatus(bills, applicableBudgets, budgetsInWarning, budgetsInDanger, tableYear, tableMonth);
        if (applicableBudgets.get() > 0) {
            var msg = "You have " + applicableBudgets.get() + " budgets that apply to this month..."
                    + budgetsInWarning.get() + " in warning and " + budgetsInDanger.get() + " in danger";
            var window = mainPane.getScene().getWindow();

            if (budgetsInDanger.get() > 0) Messages.showDanger(window, msg);
            else if (budgetsInWarning.get() > 0) Messages.showWarning(window, msg);
            else Messages.showInfo(window, msg);

           refreshBtn.setDisable(false);
        }
    }

    private void setDateLabel() {
        monthAndYear.setText(tableMonth.getValue() + "/" + tableYear.getValue());
    }

    @FXML
    public void openReportsDialog() {
        new ChartSelectDialog(mainPane.getScene().getWindow()).show();
    }

    @FXML
    public void openSettingsDialog() {
        new SettingsDialog(mainPane.getScene().getWindow()).show();
    }

    @Override
    public void setDate(final Year year, final Month month) {
        tableYear = year;
        tableMonth = month;
        refreshTable();
    }
}