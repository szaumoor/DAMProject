package com.rumpel.rumpelandroid.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.res.stringResource
import com.rumpel.rumpelandroid.R
import com.rumpel.rumpelandroid.db.DAOUser
import com.rumpel.rumpelandroid.ui.composables.EntityList
import com.rumpel.rumpelandroid.ui.composables.LoggedActivitiesBackbone
import com.rumpel.rumpelandroid.ui.composables.PaymentMethodDialog
import com.szaumoor.rumple.db.utils.Outcome
import com.szaumoor.rumple.model.entities.PaymentMethod

@Composable
fun PaymentMethodScreen(list: List<PaymentMethod> = listOf(),
                        addPm: (PaymentMethod) -> Outcome = { Outcome.SUCCESS},
                        modifyPm: (PaymentMethod) -> Outcome = { Outcome.SUCCESS},
                        deletePm: (PaymentMethod) -> Outcome = { Outcome.SUCCESS}) {
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
        mutableStateOf(PaymentMethod(""))
    }
    LoggedActivitiesBackbone(
        loggedUser = DAOUser.getLoggedUser(),
        composable = {
            if (mode.value) {
                PaymentMethodDialog(
                    open = mutable,
                    insert = true,
                    pmModify = { name ->
                        println("Tried to create programmatically method of name: $name")
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
                    open = mutable,
                    insert = false,
                    pmModify = { name ->
                        val pm = selectedPm.value
                        if (PaymentMethod("").setName(name)) {
                            pms.remove(pm)
                            pm.name = name
                            pms.add(pm)
                            return@PaymentMethodDialog modifyPm.invoke(pm)
                        } else Outcome.ERROR
                    },
                    pmDelete = {
                        println("Attempting to delete: " + selectedPm.value)
                        println("Id of object attempting to delete: " + selectedPm.value.id)
                        val deletion = deletePm(selectedPm.value)
                        if (deletion == Outcome.SUCCESS) pms.remove(selectedPm.value)
                        return@PaymentMethodDialog deletion
                    }
                )
            }
            EntityList(
                list = pms.sortedBy(selector = { it.name }),
                onClick = {
                    flipToModify(mode, selectedPm, it)
                    flipMutator(mutable)
                }
            )
        },
        addButtonName = stringResource(id = R.string.add_pm),

        ) {
        if (!mode.value) mode.value = !mode.value
        flipMutator(mutable)
    }

}

private fun flipMutator(mutable: MutableState<Boolean>){
    mutable.value = !mutable.value
}

private fun flipToModify(mode: MutableState<Boolean>, selectedTag: MutableState<PaymentMethod>, newTag: PaymentMethod) {
    if (mode.value) mode.value = !mode.value
    selectedTag.value = newTag
    println("Selected tag: $selectedTag")
}