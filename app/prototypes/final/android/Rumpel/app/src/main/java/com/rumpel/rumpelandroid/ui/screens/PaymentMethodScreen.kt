package com.rumpel.rumpelandroid.ui.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rumpel.rumpelandroid.R
import com.rumpel.rumpelandroid.db.DAOPaymentMethods
import com.rumpel.rumpelandroid.db.DAOUser
import com.rumpel.rumpelandroid.ui.composables.EntityList
import com.rumpel.rumpelandroid.ui.composables.HeadingTextComponent
import com.rumpel.rumpelandroid.ui.composables.LoggedActivitiesBackbone
import com.rumpel.rumpelandroid.ui.composables.PaymentMethodDialog
import com.rumpel.rumpelandroid.utils.threads.BackgroundTask
import com.rumpel.rumpelandroid.utils.threads.TaskRunner
import com.szaumoor.rumple.db.utils.Outcome
import com.szaumoor.rumple.model.entities.PaymentMethod
import java.util.Optional

/**
 * Composable function defining
 */
@Composable
fun PaymentMethodScreen2(
    addPm: (PaymentMethod) -> Outcome = { Outcome.SUCCESS },
    modifyPm: (PaymentMethod) -> Outcome = { Outcome.SUCCESS },
    deletePm: (PaymentMethod) -> Outcome = { Outcome.SUCCESS }
) {
    val context = LocalContext.current
    val thread = remember {
        mutableStateOf(true)
    }
    val mutable = remember {
        mutableStateOf(false)
    }
    val mode = remember {
        mutableStateOf(true)
    }
    val pms = remember {
        mutableStateListOf<PaymentMethod>()
    }
    val selectedPm: MutableState<Optional<PaymentMethod>> = remember {
        mutableStateOf(Optional.empty())
    }
    val selectedPmName: MutableState<String> = remember {
        mutableStateOf(selectedPm.value.map { it.name }.orElse(""))
    }
    if (thread.value) {
        TaskRunner.executeAsync(BackgroundTask(
            {
                pms.addAll(DAOPaymentMethods(context).all)
            },{
            thread.value = false
        }))
    }

    LoggedActivitiesBackbone(
        loggedUser = DAOUser.getLoggedUser(),
        withFloatingButton = true,
        composable = {
            if (mode.value) {
                PaymentMethodDialog(
                    open = mutable,
                    insert = true,
                    pmModify = { name ->
                        val opPm = PaymentMethod.of(name)
                        if (opPm.isPresent) {
                            val pm = opPm.get()
                            pm.user = DAOUser.getLoggedUser()
                            pms.add(pm)
                            return@PaymentMethodDialog addPm.invoke(pm)
                        } else Outcome.ERROR
                    })
            } else {
                PaymentMethodDialog(
                    name = selectedPmName,
                    open = mutable,
                    insert = false,
                    pmModify = { name ->
                        val pm = selectedPm.value
                        if (pm.isEmpty) return@PaymentMethodDialog Outcome.ERROR
                        if (PaymentMethod("").setName(name)) {
                            pm.get().name = name
                            val invoke = modifyPm.invoke(pm.get())
                            if (invoke == Outcome.SUCCESS) {
                                pms.remove(pm.get())
                                pms.add(pm.get())
                            }
                            return@PaymentMethodDialog invoke
                        } else Outcome.ERROR
                    },
                    pmDelete = {
                        if (selectedPm.value.isEmpty) return@PaymentMethodDialog Outcome.ERROR
                        val deletion = deletePm(selectedPm.value.get())
                        if (deletion == Outcome.SUCCESS) pms.remove(selectedPm.value.get())
                        return@PaymentMethodDialog deletion
                    }
                )
            }
            if (pms.isNotEmpty()) {
                Divider()
                EntityList(
                    list = pms,
                    onClick = {
                        if (mode.value) mode.value = !mode.value
                        selectedPm.value = Optional.of(it)
                        selectedPmName.value = it.name
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
        addButtonName = stringResource(id = R.string.add_pm),
        toolBarTitle = stringResource(id = R.string.your_payment_methods)

    ) {
        if (!mode.value) mode.value = !mode.value
        flipMutator(mutable)
    }

}

/**
 * Composable function defining
 */
@Composable
fun PaymentMethodScreen(
    list: MutableList<PaymentMethod> = mutableListOf(),
    addPm: (PaymentMethod) -> Outcome = { Outcome.SUCCESS },
    modifyPm: (PaymentMethod) -> Outcome = { Outcome.SUCCESS },
    deletePm: (PaymentMethod) -> Outcome = { Outcome.SUCCESS }
) {
    val context = LocalContext.current
    val mutable = remember {
        mutableStateOf(false)
    }
    val pms = remember {
        list.toMutableStateList()
    }
    val mode = remember {
        mutableStateOf(true)
    }
    val selectedPm: MutableState<PaymentMethod> = remember {
        mutableStateOf(pms[0])
    }

    val selectedPmName = remember {
        mutableStateOf(selectedPm.value.name)
    }

    LoggedActivitiesBackbone(
        loggedUser = DAOUser.getLoggedUser(),
        withFloatingButton = true,
        composable = {
            if (mode.value) {
                PaymentMethodDialog(
                    open = mutable,
                    insert = true,
                    pmModify = { name ->
                        val opPm = PaymentMethod.of(name)
                        if (opPm.isPresent) {
                            val pm = opPm.get()
                            pm.user = DAOUser.getLoggedUser()
                            pms.add(pm)
                            return@PaymentMethodDialog addPm.invoke(pm)
                        } else Outcome.ERROR
                    })
            } else {
                PaymentMethodDialog(
                    name = selectedPmName,
                    open = mutable,
                    insert = false,
                    pmModify = { name ->
                        val pm = selectedPm.value
                        if (PaymentMethod("").setName(name)) {
                            pm.name = name
                            val invoke = modifyPm.invoke(pm)
                            if (invoke == Outcome.SUCCESS) {
                                pms.remove(pm)
                                pms.add(pm)
                            }
                            return@PaymentMethodDialog invoke
                        } else Outcome.ERROR
                    },
                    pmDelete = {
                        val deletion = deletePm(selectedPm.value)
                        if (deletion == Outcome.SUCCESS) pms.remove(selectedPm.value)
                        return@PaymentMethodDialog deletion
                    }
                )
            }
            if (pms.isNotEmpty()) {
                Divider()
                EntityList(
                    list = pms.sortedBy(selector = { it.name }).toMutableList(),
                    onClick = {
                        if (mode.value) mode.value = !mode.value
                        selectedPm.value = it
                        selectedPmName.value = it.name
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
        addButtonName = stringResource(id = R.string.add_pm),
        toolBarTitle = stringResource(id = R.string.your_payment_methods)

    ) {
        if (!mode.value) mode.value = !mode.value
        flipMutator(mutable)
    }

}

private fun flipMutator(mutable: MutableState<Boolean>) {
    mutable.value = !mutable.value
}

private fun flipToModify(
    mode: MutableState<Boolean>,
    selectedPm: MutableState<PaymentMethod>,
    newPm: PaymentMethod
) {
    if (mode.value) mode.value = !mode.value
    selectedPm.value = newPm
}