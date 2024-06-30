package com.rumpel.rumpeldesktop.fxutils;

import com.rumpel.rumpeldesktop.db.AbstractDAO;
import com.rumpel.rumpeldesktop.db.DAOBudget;
import com.szaumoor.rumple.model.entities.Bill;
import com.szaumoor.rumple.model.entities.Budget;
import javafx.application.Platform;
import javafx.concurrent.Task;

import java.math.BigDecimal;
import java.time.Month;
import java.time.Year;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.Objects.isNull;

/**
 * Utility class that encapsulates common tasks such as creating background thread task objects, exiting the app properly,
 * and launched a background thread to check the status of some things (currently, budget status with respect to current expenses)
 */
public final class Tasks {
    /**
     * Private constructor to prevent instantiation
     */
    private Tasks() {
        throw new AssertionError("Utility classes cannot be instantiated");
    }

    /**
     * Creates a task that wraps the given function.
     *
     * @param function the function to be wrapped
     * @return the task that wraps the function
     */
    public static Task<Void> createTask(final Runnable function) {
        return new Task<>() {
            @Override
            protected Void call() {
                function.run();
                return null;
            }
        };
    }

    /**
     * Exit the application, closing the Database client and JavaFX platform.
     */
    public static void exit() { // might need to be reallocated
        AbstractDAO.closeClient();
        Platform.exit();
    }

    /**
     * Check the budget status for a given list of bills, and update the given atomic longs with the result.
     *
     * @param bills             the list of bills
     * @param applicableBudgets the atomic long for applicable budgets
     * @param budgetsInWarning  the atomic long for budgets in warning
     * @param budgetsInDanger   the atomic long for budgets in danger
     * @param year              the year
     * @param month             the month
     */
    public static void checkBudgetStatus(final List<Bill> bills, final AtomicLong applicableBudgets, final AtomicLong budgetsInWarning, final AtomicLong budgetsInDanger, final Year year, final Month month) {
        var budgets = new DAOBudget().getAllFiltered(year, month) // todo possibly make it a subthread in the result of this one
                .stream().filter(budget -> budget.getInterval().endDate().getMonthValue() == month.getValue()
                        && budget.getInterval().endDate().getYear() == year.getValue()).toList();
        if (budgets.isEmpty()) return;
        applicableBudgets.set(budgets.size());
        budgetsInWarning.set(budgets.stream()
                .filter(budget -> {
                    var budgetSoftLimit = budget.getLimit().softLimit();
                    var total = calculateTotalPerBudget(bills, budget);
                    return budgetSoftLimit.compareTo(BigDecimal.ZERO) > 0 && total.compareTo(budgetSoftLimit) >= 0 && total.compareTo(budget.getLimit().hardLimit()) < 0;
                }).count());
        budgetsInDanger.set(budgets.stream()
                .filter(budget -> {
                    var budgetHardLimit = budget.getLimit().hardLimit();
                    var total = calculateTotalPerBudget(bills, budget);
                    return total.compareTo(budgetHardLimit) >= 0;
                }).count());
    }

    /**
     * Calculates the total of expenses in bills per applicable budget.
     *
     * @param bills  a list of bills
     * @param budget the budget to calculate the total for
     * @return the total expenses per budget as a BigDecimal
     */
    private static BigDecimal calculateTotalPerBudget(final List<Bill> bills, final Budget budget) {
        return bills.stream()
                .filter(bill -> isNull(budget.getPaymentMethod()) || bill.getPaymentMethod().equals(budget.getPaymentMethod()))
                .filter(bill -> bill.getCurrency().equals(budget.getLimit().currency()))
                .filter(bill -> bill.getDate().isBefore(budget.getInterval().endDate().plusDays(1)))
                .map(Bill::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
