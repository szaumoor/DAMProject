package com.rumpel.rumpeldesktop.mvc.views.utils;

/**
 *  Utility to store resource paths to avoid errors and repetition.
 */
public final class ResourcePaths {

    private ResourcePaths() {
        throw new AssertionError("Utility class cannot be instantiated");
    }

    public static final String HOME = "home-view.fxml";
    public static final String LOGIN = "login_form.fxml";
    public static final String SIGN_UP = "sign_up_form.fxml";
    public static final String ABOUT_DIALOG = "about-dialog.fxml";
    public static final String TAG_DIALOG = "tags_dialog.fxml";
    public static final String PM_DIALOG = "pm_dialog.fxml";
    public static final String BUDGETS_DIALOG = "budgets_dialog.fxml";
    public static final String ITEM_BILL_DIALOG = "bill_item_dialog.fxml";
    public static final String NEW_BILL = "new_bill_form.fxml";
    public static final String ACCOUNT = "account_dialog.fxml";
    public static final String CHARTS = "chart_view.fxml";
    public static final String CHART_SELECTION = "chart_selection_view.fxml";
    public static final String SETTINGS = "settings_dialog.fxml";
    public static final String SELECT_MONTH = "select_month_dialog.fxml";
}
