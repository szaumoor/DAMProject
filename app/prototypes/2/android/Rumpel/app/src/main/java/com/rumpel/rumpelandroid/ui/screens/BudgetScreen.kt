package com.rumpel.rumpelandroid.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.rumpel.rumpelandroid.R
import com.rumpel.rumpelandroid.db.DAOUser
import com.rumpel.rumpelandroid.ui.composables.BudgetDialog
import com.rumpel.rumpelandroid.ui.composables.BudgetList
import com.rumpel.rumpelandroid.ui.composables.LoggedActivitiesBackbone
import com.szaumoor.rumple.db.utils.Outcome
import com.szaumoor.rumple.model.entities.Budget
import com.szaumoor.rumple.model.entities.PaymentMethod
import com.szaumoor.rumple.model.entities.User
import com.szaumoor.rumple.model.entities.types.Interval
import com.szaumoor.rumple.model.entities.types.Limit
import com.szaumoor.rumple.model.entities.types.Username
import com.szaumoor.rumple.utils.Optionals
import java.math.BigDecimal
import java.time.ZonedDateTime
import java.util.Currency
import java.util.Locale

@Composable
fun BudgetScreen(listPm: List<PaymentMethod>, list: List<Budget> = listOf(),
                 addBudget: (Budget) -> Outcome = { Outcome.SUCCESS},
                 modifyBudget: (Budget) -> Outcome = { Outcome.SUCCESS},
                 deleteBudget: (Budget) -> Outcome = { Outcome.SUCCESS}) {
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
        mutableStateOf(Budget())
    }
    LoggedActivitiesBackbone(
        loggedUser = DAOUser.getLoggedUser(),
        composable = {
            if (mode.value) {
                BudgetDialog(
                    list = listPm,
                    open = mutable,
                    insert = true,
                    budgetModify = { startDate: ZonedDateTime, endDate: ZonedDateTime,
                                     softLimit: BigDecimal, hardLimit: BigDecimal,
                                     paymentMethod: PaymentMethod?, currency: Currency ->
                        val opInterval = Interval.of(startDate, endDate)
                        println(opInterval)
                        val opLimit = Limit.of(softLimit, hardLimit, currency)
                        println(opLimit)
                        if (Optionals.anyEmpty(opInterval, opLimit)) return@BudgetDialog Outcome.ERROR

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
                    budgetModify = { _,_,_,_,_,_ ->
                        return@BudgetDialog modifyBudget(selectedBudget.value)
                    },
                    budgetDelete = {
                        return@BudgetDialog deleteBudget(selectedBudget.value)
                    }
                )
            }
            BudgetList(
                list = listOfBudgets,
                onClick = {
                    flipToModify(mode, selectedBudget, it)
                    flipMutator(mutable)
                }
            )
        },
        addButtonName = stringResource(id = R.string.add_budget),

        ) {
        if (!mode.value) mode.value = !mode.value
        flipMutator(mutable)
    }

}

private fun flipMutator(mutable: MutableState<Boolean>){
    mutable.value = !mutable.value
}

private fun flipToModify(mode: MutableState<Boolean>, selectedBudget: MutableState<Budget>, newTag: Budget) {
    if (mode.value) mode.value = !mode.value
    selectedBudget.value = newTag
    println("Selected budget: $selectedBudget")
}

@Preview
@Composable
fun preview() {
    var b1 = Budget(
        Interval(ZonedDateTime.now(), ZonedDateTime.now().plusDays(7)),
        Limit(BigDecimal("1000"), BigDecimal("2000"), Currency.getInstance(Locale.getDefault())),
        PaymentMethod("Cash"),
        User(Username("szaumoor"), null, null)
    )
    var b2 = Budget(
        Interval(ZonedDateTime.now(), ZonedDateTime.now().plusDays(7)),
        Limit(BigDecimal("1000"), BigDecimal("2000"), Currency.getInstance(Locale.getDefault())),
        null,
        User(Username("szaumoor"), null, null)
    )

    BudgetScreen(
        listPm = listOf(),
        list = listOf(b1, b2)
    )
}