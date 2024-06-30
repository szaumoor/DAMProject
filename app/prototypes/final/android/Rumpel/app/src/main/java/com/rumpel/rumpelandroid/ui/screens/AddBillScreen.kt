package com.rumpel.rumpelandroid.ui.screens

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.rumpel.rumpelandroid.R
import com.rumpel.rumpelandroid.db.DAOBill
import com.rumpel.rumpelandroid.db.DAOPaymentMethods
import com.rumpel.rumpelandroid.db.DAOUser
import com.rumpel.rumpelandroid.ui.activities.HomeActivity
import com.rumpel.rumpelandroid.ui.activities.utils.SelectableTag
import com.rumpel.rumpelandroid.ui.composables.ButtonComponent
import com.rumpel.rumpelandroid.ui.composables.ButtonDropdown
import com.rumpel.rumpelandroid.ui.composables.CustomDatePicker
import com.rumpel.rumpelandroid.ui.composables.HeadingTextComponent
import com.rumpel.rumpelandroid.ui.composables.ItemDialog
import com.rumpel.rumpelandroid.ui.composables.ItemList
import com.rumpel.rumpelandroid.ui.composables.LoggedActivitiesBackbone
import com.rumpel.rumpelandroid.ui.elements.Black
import com.rumpel.rumpelandroid.ui.elements.TextColor
import com.rumpel.rumpelandroid.ui.elements.White
import com.rumpel.rumpelandroid.ui.elements.currencies
import com.rumpel.rumpelandroid.utils.Popups.toast
import com.rumpel.rumpelandroid.utils.Preferences
import com.skydoves.orchestra.spinner.Spinner
import com.skydoves.orchestra.spinner.SpinnerProperties
import com.szaumoor.rumple.db.utils.Outcome
import com.szaumoor.rumple.model.entities.Bill
import com.szaumoor.rumple.model.entities.ItemBill
import com.szaumoor.rumple.model.entities.PaymentMethod
import com.szaumoor.rumple.model.entities.Tag
import com.szaumoor.rumple.utils.Dates
import com.szaumoor.rumple.utils.Money
import com.szaumoor.utils.Numbers
import java.math.BigDecimal
import java.time.LocalDate
import java.util.Currency
import java.util.stream.Collectors

/**
 * Kotlin object to allow Java code to call a composable function
 */
object ComposeContent {
    fun setContentFromJavaActivity(
        activity: AppCompatActivity,
        onScan: () -> Unit,
        onSelectImage: () -> Unit,
        listOfTags: List<Tag>,
        bill: Bill?
    ) {
        activity.setContent {
            AddBillScreen(
                listOfTags = listOfTags.map { SelectableTag(it) }.toMutableList(),
                onScan = onScan,
                onSelectImage = onSelectImage,
                bill = bill
            )
        }
    }
}

/**
 * Composable function that defined the view in the AddBillActivity
 */
@Composable
fun AddBillScreen(
    listOfItems: List<ItemBill> = emptyList(),
    listOfTags: MutableList<SelectableTag> = mutableListOf(),
    onScan: () -> Unit,
    onSelectImage: () -> Unit,
    bill: Bill? = null,
) {
    val context = LocalContext.current
    val pmList = DAOPaymentMethods(context).all
    val mutableDate = remember {
        if (bill != null) {
            mutableStateOf(bill.date.toLocalDate())
        } else mutableStateOf(LocalDate.now())
    }
    val itemList = remember {
        if (bill != null) {
            bill.stream().collect(Collectors.toList()).toMutableStateList()
        } else listOfItems.toMutableStateList()
    }

    val pm = remember {
        if (bill != null) mutableStateOf(bill.paymentMethod) else mutableStateOf<PaymentMethod?>(
            null
        )
    }
    val pmName = remember {
        if (bill != null) {
            mutableStateOf(bill.paymentMethod.name)
        } else mutableStateOf(context.getString(R.string.pm))
    }

    val selectedItem = remember {
        if (bill != null) {
            mutableStateOf(bill.getItem(0))
        } else mutableStateOf(ItemBill())
    }

    val mutable = remember { mutableStateOf(false) }
    val mode = remember { mutableStateOf(false) }

    val initialBill = bill ?: Bill()
    initialBill.user = DAOUser.getLoggedUser()

    val currentBill = remember { mutableStateOf(initialBill) }

    val dataStore = Preferences(context)
    val savedCurrency = dataStore.getCurrency.collectAsState(initial = "")
    val selectedCurr = remember { mutableStateOf(savedCurrency) }

    val btnName = if (bill != null) context.getString(R.string.modify_bill) else context.getString(R.string.insert_bill)
    val itemDialogName = remember {
        if (bill != null) mutableStateOf(selectedItem.value.name)
        else mutableStateOf("")
    }

    val itemDialogPrice = remember {
        if (bill != null) mutableStateOf(selectedItem.value.price.toString())
        else mutableStateOf("")
    }

    val toolbarTitle = if (bill != null) context.getString(R.string.modify_bill) else context.getString(R.string.new_bill)

    LoggedActivitiesBackbone(
        loggedUser = DAOUser.getLoggedUser(),
        withFloatingButton = true,
        composable = {
            if (mode.value) {
                ItemDialog(
                    openDialog = mutable,
                    insert = true,
                    selectableTags = listOfTags,
                    itemModify = { name, price ->
                        if (!Numbers.isNumber(price)) {
                            toast(context, context.getString(R.string.price_must_be_number))
                            false
                        } else {
                            val map = listOfTags.filter { it.selected.value }.map { it.tag }
                            val opItem = ItemBill.of(name, BigDecimal(price), map)
                            if (opItem.isPresent) {
                                val item = opItem.get()
                                item.bill = currentBill.value
                                return@ItemDialog itemList.add(item)
                            } else false
                        }
                    })
            } else {
                ItemDialog(
                    itemName = itemDialogName,
                    priceItem = itemDialogPrice,
                    openDialog = mutable,
                    insert = false,
                    selectableTags = listOfTags,
                    itemModify = { name, price ->
                        val item = selectedItem.value
                        if (!Numbers.isNumber(price)) {
                            toast(context, context.getString(R.string.price_must_be_number))
                            false
                        } else {
                            if (ItemBill().setName(name) && ItemBill().setPrice(BigDecimal(price))) {
                                item.name = name
                                item.price = BigDecimal(price)
                                item.clear()
                                item.addAll(listOfTags.filter { it.selected.value }.map { it.tag })
                                itemList.remove(item)
                                itemList.add(item)
                                return@ItemDialog true
                            } else false
                        }
                    },
                    itemDelete = {
                        return@ItemDialog itemList.remove(selectedItem.value)
                    }
                )
            }
            CustomDatePicker(mutableDate)
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
            )
            Spinner(
                text = pmName.value,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(46.dp)
                    .background(Color.White)
                    .border(2.dp, Color.Black, shape = RectangleShape),
                itemList = pmList.stream().map { it.name }.collect(Collectors.toList()),
                style = MaterialTheme.typography.bodyLarge,
                properties = SpinnerProperties(
                    color = TextColor,
                    textAlign = TextAlign.Center,
                    dividerColor = Black,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    spinnerPadding = 16.dp,
                    spinnerBackgroundColor = White
                ),
                onSpinnerItemSelected = { index, _ ->
                    pm.value = pmList[index]
                    if (pm.value != null)
                        pmName.value = pm.value!!.name
                }
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
            )
            Spinner(
                text = selectedCurr.value.value,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(46.dp)
                    .background(Color.White)
                    .border(2.dp, color = Black, shape = RectangleShape),
                itemList = Money.currenciesSortedByCode().stream().map { it.currencyCode }
                    .collect(Collectors.toList()),
                style = MaterialTheme.typography.bodyLarge,
                properties = SpinnerProperties(
                    color = TextColor,
                    textAlign = TextAlign.Center,
                    dividerColor = Black,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    spinnerPadding = 16.dp,
                    spinnerBackgroundColor = White
                ),
                onSpinnerItemSelected = { index, _ ->
                    selectedCurr.value = mutableStateOf(currencies[index].currencyCode)
                }
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
            )
            val expanded = remember { mutableStateOf(false) }
            ButtonDropdown(
                text = context.getString(R.string.btn_scan_bill),
                expanded = expanded,
                itemActionPairs = listOf(
                    Pair(context.getString(R.string.btn_scan_bill), onScan),
                    Pair(context.getString(R.string.select_image), onSelectImage)
                )
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(15.dp)
            )
            ButtonComponent(
                value = btnName,
                onClick = {
                    if (pm.value == null || !Money.isCurrency(selectedCurr.value.value)) {
                        toast(context, context.getString(R.string.error_missing_pm_curr))
                        return@ButtonComponent
                    }
                    if (itemList.isEmpty()) {
                        toast(context, context.getString(R.string.no_bill_items_error))
                        return@ButtonComponent
                    }
                    val dao = DAOBill(context)
                    val theBill = currentBill.value

                    theBill.paymentMethod = pm.value
                    theBill.date = Dates.zonedFromDate(mutableDate.value)
                    theBill.currency = Currency.getInstance(selectedCurr.value.value)
                    theBill.clear()
                    theBill.addAll(itemList)
                    theBill.calcTotal()

                    if (bill != null) {
                        val outcome = dao.modify(theBill)
                        if (outcome == Outcome.SUCCESS) {
                            toast(context, context.getString(R.string.bill_modified))
                            (context as Activity).startActivityIfNeeded(
                                Intent(
                                    context,
                                    HomeActivity::class.java
                                ), 1
                            )
                        }
                    } else {
                        val insertion = dao.insert(theBill)
                        if (insertion == Outcome.SUCCESS) {
                            toast(context, context.getString(R.string.bill_added))
                            (context as Activity).startActivityIfNeeded(
                                Intent(
                                    context,
                                    HomeActivity::class.java
                                ), 1
                            )
                            context.finish()
                        } else toast(context, context.getString(R.string.error_insertion))
                    }

                })
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            )
            if (itemList.isNotEmpty()) {
                Divider()
                ItemList(list = itemList, onClick = {
                    if (mode.value) mode.value = !mode.value
                    selectedItem.value = it
                    itemDialogName.value = it.name
                    itemDialogPrice.value = it.price.toString()
                    listOfTags.forEach{ selectableTag ->
                        selectableTag.selected.value = selectedItem.value.contains(selectableTag.tag)
                    }
                    flipMutator(mutable)
                })
            } else {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                )
                HeadingTextComponent(value = context.getString(R.string.no_elements_present))
            }
        },
        addButtonName = stringResource(id = R.string.add_item),
        toolBarTitle = toolbarTitle,
    ) {
        if (!mode.value) mode.value = !mode.value
        listOfTags.forEach{ selectableTag -> selectableTag.selected.value = false }
        flipMutator(mutable)
    }
}

private fun flipMutator(mutable: MutableState<Boolean>) {
    mutable.value = !mutable.value
}

private fun flipToModify(
    mode: MutableState<Boolean>,
    selectedItem: MutableState<ItemBill>,
    newItemBill: ItemBill
) {
    selectedItem.value = newItemBill
    if (mode.value) mode.value = !mode.value
}
