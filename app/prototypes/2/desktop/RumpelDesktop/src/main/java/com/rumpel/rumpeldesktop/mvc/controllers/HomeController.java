package com.rumpel.rumpeldesktop.mvc.controllers;

import com.rumpel.rumpeldesktop.db.DAOBill;
import com.rumpel.rumpeldesktop.db.DAOUser;
import com.rumpel.rumpeldesktop.fxutils.*;
import com.rumpel.rumpeldesktop.mvc.controllers.interfaces.BillInjector;
import com.rumpel.rumpeldesktop.mvc.controllers.interfaces.CredentialsScene;
import com.rumpel.rumpeldesktop.mvc.controllers.interfaces.TranslatableController;
import com.rumpel.rumpeldesktop.mvc.views.*;
import com.rumpel.rumpeldesktop.mvc.views.utils.Dimension;
import com.rumpel.rumpeldesktop.mvc.views.utils.ResourcePaths;
import com.szaumoor.rumple.db.utils.Outcome;
import com.szaumoor.rumple.model.entities.Bill;
import com.szaumoor.rumple.utils.Dates;
import com.szaumoor.rumple.utils.Money;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import static com.rumpel.rumpeldesktop.fxutils.Translator.getString;
import static com.rumpel.rumpeldesktop.mvc.views.utils.Alerts.*;
import static com.rumpel.rumpeldesktop.mvc.views.utils.ViewUtils.switchScene;
import static java.util.Objects.isNull;

public final class HomeController implements TranslatableController, Initializable, BillInjector {

    public static final Dimension dimensions = new Dimension(new Dimension.Width(1024), new Dimension.Height(800));

    private static final Logger logger = LogManager.getLogger();
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
    private Label totalLabel;
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

    private String title;
    private DAOBill dao;
    private ObservableList<Bill> bills;


    @FXML // todo: implement this for all manageable close operations
    private void closeApp() {
        Tasks.exit();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpTranslations();
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
        refreshBtn.setTooltip(Tooltips.getDefaultTooltip(Translator.getString("delete_bill")));
        Buttons.setIconButton(refreshBtn, "refresh.png");
        refreshBtn.setTooltip(Tooltips.getDefaultTooltip(Translator.getString("refresh_table")));
        Styles.applyFontSize(16, menuBar, totalLabel);
        Styles.applyFontSize(17, tableView);

        refreshTable();
        Platform.runLater(() -> mainPane.getScene().getWindow().setOnCloseRequest(event -> Tasks.exit()));
    }

    private void getBills() {
        bills = FXCollections.observableArrayList(dao.getAll());
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
        timeCol.setCellValueFactory(param -> new SimpleStringProperty(Dates.formatWithTime(param.getValue().getDate())));
        pmCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getPaymentMethod().getName()));
        elemCol.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().size()).asObject());
        totalCol.setCellValueFactory(param -> new SimpleStringProperty(Money.formatCurrency(param.getValue().getTotal(), param.getValue().getCurrency())));
    }

    @FXML
    private void logout() {
        final var stage = (Stage) mainPane.getScene().getWindow();
        stage.setFullScreen(false);
        var username = DAOUser.getLoggedUser().getUsername().username();
        DAOUser.logout();
        logger.info("User " + username + " logged out at local date: " + LocalDateTime.now());
        switchScene(stage, ResourcePaths.LOGIN, title, CredentialsScene.dimensions, false);
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

    @Override
    public void setUpTranslations() {
        title = getString("rumpel");
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
        tableView.getItems().add(bill);
    }

    @Override
    public void modifyBill(final Bill bill) {
        var items = tableView.getItems();
        items.set(items.indexOf(bill), bill);
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

    @FXML
    public void refreshTable() {
        toolbar.setDisable(true);
        loading.setVisible(true);
        tableView.setVisible(false);
        var tasker = new FXTaskRunner(
                List.of(Tasks.createTask(this::getBills)), b -> {
            if (b) {
                tableView.setItems(bills);
                loading.setVisible(false);
                tableView.getSelectionModel().clearSelection();
                tableView.setVisible(true);
                totalLabel.setText("Total: TBC");
                toolbar.setDisable(false);
            } else errorAndWait("Oops, couldn't get bills");
        });
        tasker.execute(10, TimeUnit.SECONDS);
    }

    @FXML
    public void openReportsDialog() {
        new AbstractDialog<ChartViewController>(mainPane.getScene().getWindow(), "Charts", ResourcePaths.CHARTS) {
            @Override
            protected void show() {
                dialog.showAndWait();
            }
        }.show();
    }
}