package com.rumpel.rumpelandroid.ui.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rumpel.rumpelandroid.R
import com.rumpel.rumpelandroid.db.DAOBudgets
import com.rumpel.rumpelandroid.db.DAOPaymentMethods
import com.rumpel.rumpelandroid.db.DAOUser
import com.rumpel.rumpelandroid.ui.composables.BudgetDialog
import com.rumpel.rumpelandroid.ui.composables.BudgetList
import com.rumpel.rumpelandroid.ui.composables.HeadingTextComponent
import com.rumpel.rumpelandroid.ui.composables.LoggedActivitiesBackbone
import com.rumpel.rumpelandroid.utils.Popups.toast
import com.rumpel.rumpelandroid.utils.Preferences
import com.rumpel.rumpelandroid.utils.threads.BackgroundTask
import com.rumpel.rumpelandroid.utils.threads.TaskRunner
import com.szaumoor.rumple.db.utils.Outcome
import com.szaumoor.rumple.model.entities.Budget
import com.szaumoor.rumple.model.entities.PaymentMethod
import com.szaumoor.rumple.model.entities.types.Interval
import com.szaumoor.rumple.model.entities.types.Limit
import com.szaumoor.utils.Optionals.anyEmpty
import java.math.BigDecimal
import java.time.LocalDate
import java.time.ZonedDateTime
import java.util.Currency
import java.util.Optional

/**
 * Composable function that defines the view in the Budget activity
 */
@Composable
fun BudgetScreen2(
                 addBudget: (Budget) -> Outcome = { Outcome.SUCCESS},
                 modifyBudget: (Budget) -> Outcome = { Outcome.SUCCESS},
                 deleteBudget: (Budget) -> Outcome = { Outcome.SUCCESS},
) {
    val context = LocalContext.current
    val mutable = remember {
        mutableStateOf(false)
    }
    val listOfBudgets = remember {
        mutableStateListOf<Budget>()
    }
    val listPm = remember {
        mutableStateListOf<PaymentMethod>()
    }
    val mode = remember {
        mutableStateOf(true)
    }
    val selectedBudget: MutableState<Optional<Budget>> = remember {
        mutableStateOf(Optional.empty())
    }
    val selectedBudgetStartDate = remember {
        mutableStateOf(selectedBudget.value.map { it.interval.startDate.toLocalDate() }.orElse(
            LocalDate.now()))
    }
    val selectedBudgetEndDate = remember {
        mutableStateOf(selectedBudget.value.map { it.interval.endDate.toLocalDate() }.orElse(
            LocalDate.now()))
    }
    val selectedBudgetPm = remember {
        mutableStateOf(Optional.ofNullable(selectedBudget.value.map { it.paymentMethod }.orElse(null)))
    }
    val selectedBudgetLimitSoft = remember {
        mutableStateOf(selectedBudget.value.map { it.limit.softLimit.toString() }.orElse(""))
    }
    val selectedBudgetLimitHard = remember {
        mutableStateOf(selectedBudget.value.map { it.limit.hardLimit.toString() }.orElse(""))
    }
    val selectedBudgetCurr = remember {
        mutableStateOf(selectedBudget.value.map { it.limit.currency.currencyCode }.orElse(context.getString(R.string.select_currency)))
    }
    val dataStore = Preferences(context)
    val savedCurrency = dataStore.getCurrency.collectAsState(initial = "")

    val thread = remember {
        mutableStateOf(true)
    }

    val listOfTasks: List<BackgroundTask> = listOf(
        BackgroundTask {
            listOfBudgets.addAll(DAOBudgets(context).all)
        },
        BackgroundTask{
            listPm.addAll(DAOPaymentMethods(context).all)
        }
    )

    if (thread.value) {
        TaskRunner.executeAsync(
            BackgroundTask({
                TaskRunner.executeAsync(listOfTasks)
            }, {
                thread.value = false
            })
        )
    }

    LoggedActivitiesBackbone(
        loggedUser = DAOUser.getLoggedUser(),
        withFloatingButton = true,
        composable = {
            if (mode.value) {
                BudgetDialog(
                    list = listPm,
                    open = mutable,
                    insert = true,
                    currency = savedCurrency,
                    budgetModify = { startDate: ZonedDateTime, endDate: ZonedDateTime,
                                     softLimit: BigDecimal, hardLimit: BigDecimal,
                                     paymentMethod: PaymentMethod?, currency: Currency ->
                        val opInterval = Interval.of(startDate, endDate)
                        val opLimit = Limit.of(softLimit, hardLimit, currency)
                        if (anyEmpty(opInterval, opLimit)) return@BudgetDialog Outcome.ERROR

                        val budget = Budget.of(opInterval.get(), opLimit.get(), paymentMethod, DAOUser.getLoggedUser())

                        if (budget.isEmpty) return@BudgetDialog Outcome.ERROR

                        val out = addBudget(budget.get())
                        if (out == Outcome.SUCCESS) listOfBudgets.add(budget.get())
                        return@BudgetDialog out
                    })
            } else {
                BudgetDialog(
                    list = listPm,
                    open = mutable,
                    insert = false,
                    startDate = selectedBudgetStartDate,
                    endDate = selectedBudgetEndDate,
                    softLimit = selectedBudgetLimitSoft,
                    hardLimit = selectedBudgetLimitHard,
                    paymentMethod = selectedBudgetPm,
                    currency = selectedBudgetCurr,
                    budgetModify = { startDate, endDate, softLimit, hardLimit, paymentMethod, currency ->
                        val budget = selectedBudget.value
                        if (budget.isEmpty) return@BudgetDialog Outcome.ERROR
                        val newLimit = Limit.of(softLimit, hardLimit, currency)
                        if (newLimit.isEmpty)  {
                            toast(context, context.getString(R.string.invalid_limit))
                            return@BudgetDialog Outcome.ERROR
                        }
                        val interval = Interval.of(startDate, endDate)
                        if (interval.isEmpty) {
                            toast(context, context.getString(R.string.invalid_interval))
                            return@BudgetDialog Outcome.ERROR
                        }
                        budget.get().interval = interval.get()
                        budget.get().limit = newLimit.get()
                        budget.get().paymentMethod = paymentMethod
                        val modification = modifyBudget(budget.get())
                        if (modification == Outcome.SUCCESS) {
                            listOfBudgets.remove(budget.get())
                            listOfBudgets.add(budget.get())
                        }
                        return@BudgetDialog modification
                    },
                    budgetDelete = {
                        val deletion = deleteBudget(selectedBudget.value.get())
                        if (deletion == Outcome.SUCCESS) listOfBudgets.remove(selectedBudget.value.get())
                        return@BudgetDialog deletion
                    }
                )
            }
            if (listOfBudgets.isNotEmpty()) {
                BudgetList(
                    list = listOfBudgets,
                    onClick = {
                        if (mode.value) mode.value = !mode.value
                        selectedBudget.value = Optional.of(it)
                        println("Selected a budget with an ID of ${it.id}")
                        selectedBudgetPm.value = Optional.ofNullable(it.paymentMethod)
                        selectedBudgetStartDate.value = it.interval.startDate.toLocalDate()
                        selectedBudgetEndDate.value = it.interval.endDate.toLocalDate()
                        selectedBudgetLimitSoft.value = it.limit.softLimit.toString()
                        selectedBudgetLimitHard.value = it.limit.hardLimit.toString()
                        selectedBudgetCurr.value = it.limit.currency.currencyCode
                        flipMutator(mutable)
                    }
                )
            } else {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                )
                HeadingTextComponent(value = context.getString(R.string.no_elements_present))
            }
        },
        addButtonName = stringResource(id = R.string.add_budget),
        toolBarTitle = stringResource(id = R.string.your_budgets)
    ) {
        if (!mode.value) mode.value = !mode.value
        flipMutator(mutable)
    }

}


/**
 * Composable function that defines the view in the Budget activity
 */
@Composable
fun BudgetScreen(listPm: List<PaymentMethod>, list: List<Budget> = listOf(),
                 addBudget: (Budget) -> Outcome = { Outcome.SUCCESS},
                 modifyBudget: (Budget) -> Outcome = { Outcome.SUCCESS},
                 deleteBudget: (Budget) -> Outcome = { Outcome.SUCCESS},
                 initialCurrency: State<String> = mutableStateOf(stringResource(id = R.string.select_currency))
) {
    val context = LocalContext.current
    val mutable = remember {
        mutableStateOf(false)
    }
    val listOfBudgets = remember {
        list.toMutableStateList()
    }
    val mode = remember {
        mutableStateOf(true)
    }
    val selectedBudget: MutableState<Budget> = remember {
        mutableStateOf(listOfBudgets[0])
    }
    val selectedBudgetStartDate = remember {
        mutableStateOf(selectedBudget.value.interval.startDate.toLocalDate())
    }
    val selectedBudgetEndDate = remember {
        mutableStateOf(selectedBudget.value.interval.endDate.toLocalDate())
    }
    val selectedBudgetPm = remember {
        mutableStateOf(Optional.ofNullable(selectedBudget.value.paymentMethod))
    }
    val selectedBudgetLimitSoft = remember {
        mutableStateOf(selectedBudget.value.limit.softLimit.toString())
    }
    val selectedBudgetLimitHard = remember {
        mutableStateOf(selectedBudget.value.limit.hardLimit.toString())
    }
    val selectedBudgetCurr = remember {
        mutableStateOf(selectedBudget.value.limit.currency.currencyCode)
    }
    val dataStore = Preferences(context)
    val savedCurrency = dataStore.getCurrency.collectAsState(initial = "")

    LoggedActivitiesBackbone(
        loggedUser = DAOUser.getLoggedUser(),
        withFloatingButton = true,
        composable = {
            if (mode.value) {
                BudgetDialog(
                    list = listPm,
                    open = mutable,
                    insert = true,
                    currency = savedCurrency,
                    budgetModify = { startDate: ZonedDateTime, endDate: ZonedDateTime,
                                     softLimit: BigDecimal, hardLimit: BigDecimal,
                                     paymentMethod: PaymentMethod?, currency: Currency ->
                        val opInterval = Interval.of(startDate, endDate)
                        val opLimit = Limit.of(softLimit, hardLimit, currency)
                        if (anyEmpty(opInterval, opLimit)) return@BudgetDialog Outcome.ERROR

                        val budget = Budget.of(opInterval.get(), opLimit.get(), paymentMethod, DAOUser.getLoggedUser())

                        if (budget.isEmpty) return@BudgetDialog Outcome.ERROR

                        val out = addBudget(budget.get())
                        if (out == Outcome.SUCCESS) listOfBudgets.add(budget.get())
                        return@BudgetDialog out
                    })
            } else {
                BudgetDialog(
                    list = listPm,
                    open = mutable,
                    insert = false,
                    startDate = selectedBudgetStartDate,
                    endDate = selectedBudgetEndDate,
                    softLimit = selectedBudgetLimitSoft,
                    hardLimit = selectedBudgetLimitHard,
                    paymentMethod = selectedBudgetPm,
                    currency = selectedBudgetCurr,
                    budgetModify = { startDate, endDate, softLimit, hardLimit, paymentMethod, currency ->
                        val budget = selectedBudget.value
                        val newLimit = Limit.of(softLimit, hardLimit, currency)
                        if (newLimit.isEmpty)  {
                            toast(context, context.getString(R.string.invalid_limit))
                            return@BudgetDialog Outcome.ERROR
                        }
                        val interval = Interval.of(startDate, endDate)
                        if (interval.isEmpty) {
                            toast(context, context.getString(R.string.invalid_interval))
                            return@BudgetDialog Outcome.ERROR
                        }
                        budget.interval = interval.get()
                        budget.limit = newLimit.get()
                        budget.paymentMethod = paymentMethod
                        val modification = modifyBudget(budget)
                        if (modification == Outcome.SUCCESS) {
                            listOfBudgets.remove(budget)
                            listOfBudgets.add(budget)
                        }
                        return@BudgetDialog modification
                    },
                    budgetDelete = {
                        val deletion = deleteBudget(selectedBudget.value)
                        if (deletion == Outcome.SUCCESS) listOfBudgets.remove(selectedBudget.value)
                        return@BudgetDialog deletion
                    }
                )
            }
            if (listOfBudgets.isNotEmpty()) {
                BudgetList(
                    list = listOfBudgets,
                    onClick = {
                        if (mode.value) mode.value = !mode.value
                        selectedBudget.value = it
                        println("Selected a budget with an ID of ${it.id}")
                        selectedBudgetPm.value = Optional.ofNullable(it.paymentMethod)
                        selectedBudgetStartDate.value = it.interval.startDate.toLocalDate()
                        selectedBudgetEndDate.value = it.interval.endDate.toLocalDate()
                        selectedBudgetLimitSoft.value = it.limit.softLimit.toString()
                        selectedBudgetLimitHard.value = it.limit.hardLimit.toString()
                        selectedBudgetCurr.value = it.limit.currency.currencyCode
                        flipMutator(mutable)
                    }
                )
            } else {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                )
                HeadingTextComponent(value = context.getString(R.string.no_elements_present))
            }
        },
        addButtonName = stringResource(id = R.string.add_budget),
        toolBarTitle = stringResource(id = R.string.your_budgets)
        ) {
        if (!mode.value) mode.value = !mode.value
        flipMutator(mutable)
    }

}

private fun flipMutator(mutable: MutableState<Boolean>){
    mutable.value = !mutable.value
}

private fun flipToModify(mode: MutableState<Boolean>, selectedBudget: MutableState<Budget>, newBudget: Budget) {
    if (mode.value) mode.value = !mode.value
    selectedBudget.value = newBudget
}