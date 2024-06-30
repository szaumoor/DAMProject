package com.rumpel.rumpelandroid.ui.composables

import android.app.Activity
import android.app.LocaleManager
import android.content.Intent
import android.os.LocaleList
import android.util.Log
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.rumpel.rumpelandroid.R
import com.rumpel.rumpelandroid.db.DAOUser
import com.rumpel.rumpelandroid.ui.activities.ChartViewActivity
import com.rumpel.rumpelandroid.ui.activities.HomeActivity
import com.rumpel.rumpelandroid.ui.activities.utils.SelectableTag
import com.rumpel.rumpelandroid.ui.elements.Black
import com.rumpel.rumpelandroid.ui.elements.TextColor
import com.rumpel.rumpelandroid.ui.elements.White
import com.rumpel.rumpelandroid.ui.elements.currencies
import com.rumpel.rumpelandroid.utils.Popups.snackbar
import com.rumpel.rumpelandroid.utils.Popups.toast
import com.rumpel.rumpelandroid.utils.Preferences
import com.skydoves.orchestra.spinner.Spinner
import com.skydoves.orchestra.spinner.SpinnerProperties
import com.szaumoor.rumple.db.utils.Outcome
import com.szaumoor.rumple.model.entities.PaymentMethod
import com.szaumoor.rumple.model.entities.types.UserPass
import com.szaumoor.rumple.model.entities.types.reports.ReportType
import com.szaumoor.rumple.model.entities.types.reports.ReportType.EXPENSES_IN_MONTH
import com.szaumoor.rumple.model.entities.types.reports.ReportType.EXPENSES_YEAR
import com.szaumoor.rumple.model.entities.types.reports.ReportType.EXPENSES_YEARLY
import com.szaumoor.rumple.model.entities.types.reports.ReportType.PAYMENT_STATS_ACROSS_YEARS
import com.szaumoor.rumple.model.entities.types.reports.ReportType.PAYMENT_STATS_MONTH
import com.szaumoor.rumple.model.entities.types.reports.ReportType.PAYMENT_STATS_YEAR
import com.szaumoor.rumple.model.entities.types.reports.ReportType.TAG_STATS_ACROSS_YEARS
import com.szaumoor.rumple.model.entities.types.reports.ReportType.TAG_STATS_MONTH
import com.szaumoor.rumple.model.entities.types.reports.ReportType.TAG_STATS_YEAR
import com.szaumoor.rumple.utils.Money
import com.szaumoor.utils.Numbers
import com.szaumoor.utils.Strings
import com.szaumoor.utils.Strings.hasContent
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate
import java.time.Month
import java.time.Year
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.TextStyle
import java.util.Currency
import java.util.EnumSet
import java.util.Locale
import java.util.Optional
import java.util.stream.Collectors

/**
 * Generic dialog for user as a backbone in other dialogs
 */
@Composable
private fun GenericDialog(
    open: MutableState<Boolean>,
    headerIcon: Int,
    headerText: String,
    usesDefaultPlatformWidth: Boolean = true,
    content: @Composable () -> Unit

) {
    if (open.value) {
        Dialog(
            onDismissRequest = { open.value = false },
            properties = DialogProperties(
                usePlatformDefaultWidth = usesDefaultPlatformWidth
            )
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(
                        color = White,
                        shape = RectangleShape
                    )
                    .padding(15.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,

                    ) {
                    Icon(
                        painter = painterResource(id = headerIcon),
                        contentDescription = "",
                    )
                }
                HeadingTextComponent(
                    value = headerText
                )
                Spacer(modifier = Modifier.height(10.dp))
                content.invoke()
            }

        }
    }
}

/**
 * Dialog to show an "About" type of window showing information about the project
 */
@Composable
fun AboutDialog(open: MutableState<Boolean>) {
    val context = LocalContext.current
    GenericDialog(
        open = open,
        headerIcon = R.drawable.money_icon,
        headerText = context.getString(R.string.rumpel) + " v1.0"
    ) {
        NormalTextComponent(
            value = stringResource(id = R.string.developer) + ": " + stringResource(id = R.string.author_name),
            align = TextAlign.Start,
            fontSize = 16.sp,
            maxWidth = true
        )
        NormalTextComponent(
            value = stringResource(id = R.string.author_contact) + ": " + stringResource(id = R.string.author_email),
            align = TextAlign.Start,
            fontSize = 16.sp,
            maxWidth = true
        )
        NormalTextComponent(
            value = stringResource(id = R.string.about_powered_by),
            align = TextAlign.Start,
            fontSize = 16.sp,
            maxWidth = true
        )
        Spacer(modifier = Modifier.height(5.dp))
        ButtonComponent(
            value = "OK",
            onClick = {
                open.value = false
            }
        )
    }
}

/**
 * Dialog for adding, modifying or deleting tags
 */
@Composable
fun TagDialog(
    name: MutableState<String> = mutableStateOf(""),
    open: MutableState<Boolean>,
    tagModify: (String) -> Outcome = { Outcome.SUCCESS },
    tagDelete: () -> Outcome = { Outcome.SUCCESS },
    insert: Boolean = true
) {
    val context = LocalContext.current
    val heading = if (insert) R.string.new_tag else R.string.modify_tag
    val buttonName = if (insert) R.string.insert_tag else R.string.modify
    GenericDialog(
        open = open,
        headerIcon = R.drawable.nav_menu_tags,
        headerText = context.getString(heading)
    ) {
        CustomTextField(
            textValue = name.value,
            labelValue = stringResource(id = R.string.tag_name),
            resource = painterResource(id = R.drawable.nav_menu_tags),
            function = {
                return@CustomTextField name
            }
        )
        Spacer(modifier = Modifier.height(5.dp))
        ButtonComponent(
            value = stringResource(id = buttonName),
            onClick = {
                when (tagModify(name.value)) {
                    Outcome.SUCCESS -> open.value = false
                    Outcome.NOT_FOUND -> toast(context, "Tag not found!")
                    else -> toast(context, "Something went wrong!")
                }
            }
        )
        if (!insert) {
            ButtonComponent(
                value = stringResource(id = R.string.delete),
                onClick = {
                    val outcome = tagDelete()
                    if (outcome == Outcome.SUCCESS)
                        open.value = false
                    else toast(context, "Something went wrong!")
                }
            )
        }
    }
}
/**
 * Dialog for adding, modifying or deleting payment methods
 */
@Composable
fun PaymentMethodDialog(
    name: MutableState<String> = mutableStateOf(""),
    open: MutableState<Boolean>,
    pmModify: (String) -> Outcome = { Outcome.SUCCESS },
    pmDelete: () -> Outcome = { Outcome.SUCCESS },
    insert: Boolean = true
) {
    val context = LocalContext.current
    val heading = if (insert) R.string.new_pm else R.string.modify_method
    val buttonName = if (insert) R.string.insert_payment_method else R.string.modify
    GenericDialog(
        open = open,
        headerIcon = R.drawable.payment_methods,
        headerText = context.getString(heading)
    ) {
        CustomTextField(
            textValue = name.value,
            labelValue = stringResource(id = R.string.method_name),
            resource = painterResource(id = R.drawable.payment_methods),
            function = {
                return@CustomTextField name
            }
        )
        Spacer(modifier = Modifier.height(5.dp))
        ButtonComponent(
            value = stringResource(id = buttonName),
            onClick = {
                when (pmModify(name.value)) {
                    Outcome.SUCCESS -> open.value = false
                    Outcome.NOT_FOUND -> toast(context, "Method not found!")
                    else -> toast(context, "Something went wrong!")
                }
            }
        )
        if (!insert) {
            ButtonComponent(
                value = stringResource(id = R.string.delete),
                onClick = {
                    val outcome = pmDelete()
                    if (outcome == Outcome.SUCCESS)
                        open.value = false
                    else toast(context, "Something went wrong!")
                }
            )
        }
    }
}

/**
 * Dialog for adding, modifying or deleting items in expenses
 */
@Composable
fun ItemDialog(
    itemName: MutableState<String> = mutableStateOf(""),
    priceItem: MutableState<String> = mutableStateOf("0.0"),
    selectableTags: MutableList<SelectableTag>,
    openDialog: MutableState<Boolean>,
    itemModify: (String, String) -> Boolean = { _, _ -> true },
    itemDelete: () -> Boolean = { true },
    insert: Boolean = true
) {
    val context = LocalContext.current
    val heading = if (insert) R.string.new_item else R.string.modify_item
    val buttonName = if (insert) R.string.insert_item else R.string.modify
    GenericDialog(
        open = openDialog,
        headerIcon = R.drawable.money_icon,
        headerText = context.getString(heading)
    ) {
        CustomTextField(
            textValue = itemName.value,
            labelValue = stringResource(id = R.string.item_name),
            resource = painterResource(id = R.drawable.shopping_bag),
            function = {
                return@CustomTextField itemName
            }
        )
        Spacer(modifier = Modifier.height(5.dp))
        CustomTextField(
            textValue = priceItem.value,
            labelValue = stringResource(id = R.string.price),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            function = {
                return@CustomTextField priceItem
            },
            resource = painterResource(id = R.drawable.money_icon)
        )
        Spacer(modifier = Modifier.height(5.dp))
        NormalTextComponent(
            value = context.getString(R.string.select_tags),
            align = TextAlign.Center,
            fontSize = 18.sp,
            maxWidth = true
        )
        Spacer(modifier = Modifier.height(3.dp))
        Column {
            CheckBoxList(
                modifier = Modifier
                    .weight(1.0f)
                    .height(180.dp),
                list = selectableTags,
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
        ButtonComponent(
            value = stringResource(id = buttonName),
            onClick = {
                val success = itemModify(itemName.value, priceItem.value)
                if (success)
                    openDialog.value = false
                else toast(context, context.getString(R.string.something_went_wrong))
            }
        )
        if (!insert) {
            ButtonComponent(
                value = stringResource(id = R.string.delete),
                onClick = {
                    val outcome = itemDelete()
                    if (outcome) openDialog.value = false
                    else toast(context, context.getString(R.string.something_went_wrong))
                }
            )
        }
    }
}
/**
 * Dialog for adding, modifying or deleting budgets
 */
@Composable
fun BudgetDialog(
    list: List<PaymentMethod>,
    open: MutableState<Boolean>,
    budgetModify: (ZonedDateTime, ZonedDateTime, BigDecimal, BigDecimal, PaymentMethod?, Currency) -> Outcome = { _, _, _, _, _, _ -> Outcome.SUCCESS },
    budgetDelete: () -> Outcome = { Outcome.SUCCESS },
    insert: Boolean = true,
    startDate: MutableState<LocalDate> = mutableStateOf(LocalDate.now()),
    endDate: MutableState<LocalDate> = mutableStateOf(LocalDate.now().plusDays(7)),
    softLimit: MutableState<String> = mutableStateOf("0.0"),
    hardLimit: MutableState<String> = mutableStateOf("0.0"),
    paymentMethod: MutableState<Optional<PaymentMethod>> = mutableStateOf(Optional.of(PaymentMethod(""))),
    currency: State<String>
) {
    val context = LocalContext.current
    val methodList = remember { list }

    val passedPmName: String =  if (paymentMethod.value.isPresent) {
            paymentMethod.value.get().name
        } else {
            context.getString(R.string.none)
        }

    val selectedItem = remember {
        if (insert)
            mutableStateOf(context.getString(R.string.choose_payment_method))
        else {
            mutableStateOf(passedPmName)
        }
    }

    val heading = if (insert) R.string.new_budget else R.string.modify_budget
    val buttonName = if (insert) R.string.insert_budget else R.string.modify

    val selectedCurrency = remember { mutableStateOf(currency) }

    GenericDialog(
        open = open,
        headerIcon = R.drawable.nav_menu_budgets,
        headerText = context.getString(heading),
        usesDefaultPlatformWidth = false
    ) {
        Column {
            Spinner(
                text = selectedItem.value,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(46.dp)
                    .background(Color.White)
                    .align(Alignment.CenterHorizontally)
                    .border(2.dp, color = Black, shape = CircleShape),
                itemList = methodList.stream().map { it.name }.collect(Collectors.toList()),
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
                    paymentMethod.value = Optional.of(list[index])
                    selectedItem.value = paymentMethod.value.get().name
                }
            )
            Spacer(modifier = Modifier.height(5.dp))
            Spinner(
                text = selectedCurrency.value.value,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(46.dp)
                    .background(Color.White)
                    .align(Alignment.CenterHorizontally)
                    .border(2.dp, color = Black, shape = CircleShape),
                itemList = currencies.stream().map { it.currencyCode }
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
                    selectedCurrency.value = mutableStateOf(currencies[index].currencyCode)
                }
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
        CustomDatePicker(startDate)
        Spacer(modifier = Modifier.height(5.dp))
        CustomDatePicker(endDate)
        Spacer(modifier = Modifier.height(5.dp))
        CustomTextField(
            textValue = softLimit.value,
            labelValue = stringResource(id = R.string.soft_limit),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            function = {
                return@CustomTextField softLimit
            },
            resource = painterResource(id = R.drawable.money_icon)
        )
        Spacer(modifier = Modifier.height(5.dp))
        CustomTextField(
            textValue = hardLimit.value,
            labelValue = stringResource(id = R.string.hard_limit),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            function = {
                return@CustomTextField hardLimit
            },
            resource = painterResource(id = R.drawable.money_icon)
        )
        Spacer(modifier = Modifier.height(5.dp))
        ButtonComponent(
            value = stringResource(id = buttonName),
            onClick = {
                val outcome = budgetModify(
                    startDate.value.atStartOfDay(ZoneId.systemDefault()),
                    endDate.value.atStartOfDay(ZoneId.systemDefault()),
                    BigDecimal(softLimit.value),
                    BigDecimal(hardLimit.value),
                    paymentMethod.value.orElse(null),
                    Currency.getInstance(selectedCurrency.value.value)
                )
                when (outcome) {
                    Outcome.SUCCESS -> open.value = false
                    Outcome.NOT_FOUND -> toast(
                        context,
                        context.getString(R.string.budget_not_found)
                    )
                    else -> toast(context, R.string.something_went_wrong)
                }
            }
        )
        if (!insert) {
            ButtonComponent(
                value = stringResource(id = R.string.delete),
                onClick = {
                    val outcome = budgetDelete()
                    if (outcome == Outcome.SUCCESS)
                        open.value = false
                    else toast(context, "Something went wrong!")
                }
            )
        }
    }
}

/**
 * Dialog for requesting various types of reports
 */
@Composable
fun ReportChooserDialog(
    open: MutableState<Boolean>,
    drawerState: () -> Unit = {},
    initialCurrency: State<String>,
) {
    val context = LocalContext.current
    val reportTypeList = context.resources.getStringArray(R.array.report_types).asList()
    val reportTypeString =
        remember { mutableStateOf(context.getString(R.string.select_report_type)) }
    val reportTypeIndex = remember { mutableIntStateOf(-1) }

    val months = EnumSet.allOf(Month::class.java)
        .map { Strings.capitalize(it.getDisplayName(TextStyle.FULL, Locale.getDefault())) }
    val selectMonthIndex = remember { mutableIntStateOf(-1) }
    val selectedMonth = remember { mutableStateOf(context.getString(R.string.select_month)) }
    val selectedYear = remember { mutableStateOf(Year.now().toString()) }

    val selectedRadioButton = remember { mutableStateOf(true) }
    val selectedRadioString = remember { mutableStateOf("PIE") }

    val currList = Money.currenciesSortedByCode()
    val selectedCurr = remember { mutableStateOf(initialCurrency) }

    GenericDialog(
        open = open,
        headerIcon = R.drawable.report,
        headerText = context.getString(R.string.new_report)
    ) {
        Column {
            Spinner(
                text = reportTypeString.value,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(46.dp)
                    .background(Color.White)
                    .align(Alignment.CenterHorizontally)
                    .border(2.dp, color = Black, shape = CircleShape),
                itemList = context.resources.getStringArray(R.array.report_types).asList(),
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
                    reportTypeIndex.intValue = index
                    reportTypeString.value = reportTypeList[index]
                }
            )
            Spacer(modifier = Modifier.height(5.dp))
            Spinner(
                text = selectedMonth.value,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(46.dp)
                    .background(Color.White)
                    .align(Alignment.CenterHorizontally)
                    .border(2.dp, color = Black, shape = CircleShape),
                itemList = Month.entries.map {
                    Strings.capitalize(
                        it.getDisplayName(
                            TextStyle.FULL,
                            Locale.getDefault()
                        )
                    )
                },
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
                onSpinnerItemSelected = { index, month ->
                    selectedMonth.value = month
                    selectMonthIndex.intValue = index
                }
            )
            Spacer(modifier = Modifier.height(5.dp))
            CustomTextField(
                labelValue = context.getString(R.string.year),
                textValue = selectedYear.value,
                resource = painterResource(id = R.drawable.calendar),
                function = { return@CustomTextField selectedYear },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(5.dp))
            Spinner(
                text = selectedCurr.value.value,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(46.dp)
                    .background(Color.White)
                    .align(Alignment.CenterHorizontally)
                    .border(2.dp, color = Black, shape = CircleShape),
                itemList = currList.stream().map { it.currencyCode }
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
                    selectedCurr.value = mutableStateOf(currList[index].currencyCode)
                }
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top
        ) {
            RadioButton(modifier = Modifier
                .weight(0.15f)
                .width(20.dp), selected = selectedRadioButton.value,
                onClick = {
                    selectedRadioButton.value = !selectedRadioButton.value
                    selectedRadioString.value = "PIE"
                })
            NormalTextComponent(
                modifier = Modifier
                    .weight(0.15f)
                    .padding(top = 12.dp),
                value = context.getString(R.string.pie),
                align = TextAlign.Start,
                fontSize = 18.sp,
                maxWidth = false
            )
            RadioButton(modifier = Modifier.weight(0.15f), selected = !selectedRadioButton.value,
                onClick = {
                    selectedRadioButton.value = !selectedRadioButton.value
                    selectedRadioString.value = "BAR"
                })
            NormalTextComponent(
                modifier = Modifier
                    .weight(0.55f)
                    .padding(top = 12.dp),
                value = context.getString(R.string.bar),
                align = TextAlign.Start,
                fontSize = 18.sp,
                maxWidth = false
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        ButtonComponent(
            value = stringResource(id = R.string.generate_report),
            onClick = {
                if (!Money.isCurrency(selectedCurr.value.value)) {
                    toast(context, context.getString(R.string.must_select_currency))
                    return@ButtonComponent
                }
                val possibleReportType: Optional<ReportType> = when (reportTypeIndex.intValue) {
                    0 -> Optional.of(TAG_STATS_MONTH)
                    1 -> Optional.of(TAG_STATS_YEAR)
                    2 -> Optional.of(TAG_STATS_ACROSS_YEARS)
                    3 -> Optional.of(PAYMENT_STATS_MONTH)
                    4 -> Optional.of(PAYMENT_STATS_YEAR)
                    5 -> Optional.of(PAYMENT_STATS_ACROSS_YEARS)
                    6 -> Optional.of(EXPENSES_IN_MONTH)
                    7 -> Optional.of(EXPENSES_YEAR)
                    8 -> Optional.of(EXPENSES_YEARLY)
                    else -> Optional.empty()
                }

                if (possibleReportType.isEmpty) {
                    toast(context, context.getString(R.string.no_report_type_selected))
                    return@ButtonComponent
                }

                val reportType = possibleReportType.get()
                if (reportType == TAG_STATS_MONTH || reportType == TAG_STATS_YEAR || reportType == TAG_STATS_ACROSS_YEARS) {
                    if (selectedRadioString.value == "PIE") {
                        toast(context, context.getString(R.string.pie_not_available))
                        return@ButtonComponent
                    }
                }

                when (reportType) {
                    TAG_STATS_MONTH, PAYMENT_STATS_MONTH, EXPENSES_IN_MONTH -> {
                        if (!months.contains(selectedMonth.value)) {
                            toast(context, context.getString(R.string.must_select_month))
                            return@ButtonComponent
                        }
                        val canExtractDigit = Numbers.isNumber(selectedYear.value)
                        if (!canExtractDigit) {
                            toast(context, context.getString(R.string.must_select_year))
                            return@ButtonComponent
                        }
                    }

                    TAG_STATS_YEAR, TAG_STATS_ACROSS_YEARS, PAYMENT_STATS_YEAR, PAYMENT_STATS_ACROSS_YEARS, EXPENSES_YEAR, EXPENSES_YEARLY -> {
                        val canExtractDigit =
                            Numbers.isNumber(Strings.removeNonDigits(selectedYear.value))
                        if (!canExtractDigit) {
                            toast(context, context.getString(R.string.must_select_year))
                            return@ButtonComponent
                        }
                    }
                }

                val chartIntent = Intent(context, ChartViewActivity::class.java)
                chartIntent.putExtra(ReportType::class.java.simpleName, reportType.name)
                chartIntent.putExtra("MONTH", selectMonthIndex.intValue + 1)
                chartIntent.putExtra("YEAR", Integer.parseInt(selectedYear.value))
                chartIntent.putExtra("CHART", selectedRadioString.value)
                chartIntent.putExtra("CURRENCY", selectedCurr.value.value)
                context.startActivity(chartIntent)
                open.value = false
                drawerState.invoke()
            }
        )
    }
}

/**
 * Dialog for settings. Currently only supporting language and default currency, hence why a small dialog is enough
 */
@Composable
fun SettingsDialog(
    open: MutableState<Boolean> = mutableStateOf(true),
    initialCurrency: State<String>
) {
    val context = LocalContext.current
    val mutableCurr = remember {
        mutableStateOf(initialCurrency)
    }
    val scope = rememberCoroutineScope()
    val currentLocale = remember {
        mutableStateOf(Locale.getDefault().displayLanguage)
    }
    val selectedIndex = remember {
        mutableIntStateOf(0)
    }
    val langStr = context.resources.getStringArray(R.array.langReadable).toList()
    val lang = context.resources.getStringArray(R.array.lang).toList()
    val selectedStr = remember {
        mutableStateOf(currentLocale.value.capitalize(Locale.getDefault()))
    }

    GenericDialog(
        open = open,
        headerIcon = R.drawable.settings,
        headerText = stringResource(id = R.string.settings),
    ) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .fillMaxWidth(0.5f),
                    text = context.getString(R.string.default_currency) + ":",
                    textAlign = TextAlign.Start,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spinner(
                    text = mutableCurr.value.value,
                    modifier = Modifier
                        .heightIn(20.dp)
                        .fillMaxWidth(1f)
                        //  .width(125.dp)
                        .background(Color.White)
                        .border(1.dp, color = Black, shape = CircleShape),
                    itemList = currencies.map { it.currencyCode },
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
                        mutableCurr.value = mutableStateOf(currencies[index].currencyCode)
                    }
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            ) {

                Text(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .fillMaxWidth(0.5f),
                    text = context.getString(R.string.app_lang) + ":",
                    textAlign = TextAlign.Start,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spinner(
                    text = selectedStr.value,
                    modifier = Modifier
                        .heightIn(20.dp)
                        .fillMaxWidth(1f)
                        .background(Color.White)
                        .border(1.dp, color = Black, shape = CircleShape),
                    itemList = langStr,
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
                    onSpinnerItemSelected = { index, value ->
                        selectedIndex.intValue = index
                        selectedStr.value = langStr[index]
                    }
                )
            }
            Spacer(modifier = Modifier.height(40.dp))
            ButtonComponent(value = context.getString(R.string.save_settings), onClick = {
                scope.launch {
                    Preferences(context).savePreferredCurrency(mutableCurr.value.value)
                    context.getSystemService(LocaleManager::class.java).applicationLocales = LocaleList(Locale.forLanguageTag(lang[selectedIndex.intValue]))
                    open.value = false
                }
            })
        }

    }
}

/**
 * Dialog for account settings. Currently only allows changing password.
 */
@Composable
fun AccountDialog(
    open: MutableState<Boolean> = mutableStateOf(true)
) {
    val context = LocalContext.current
    val passCurrent = remember { mutableStateOf("") }
    val passNew = remember { mutableStateOf("") }
    val passConfirm = remember { mutableStateOf("") }

    GenericDialog(
        open = open,
        headerIcon = R.drawable.user_bigger,
        headerText = stringResource(id = R.string.your_account),
        usesDefaultPlatformWidth = false
    ) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ){
            PasswordTextField(
                labelValue = stringResource(id = R.string.current_pass),
                function = {
                    return@PasswordTextField passCurrent
                }
            )
            PasswordTextField(
                labelValue = stringResource(id = R.string.new_password),
                function = {
                    return@PasswordTextField passNew
                }
            )
            PasswordTextField(
                labelValue = stringResource(id = R.string.confirm_password),
                function = {
                    return@PasswordTextField passConfirm
                }
            )
            Spacer(modifier = Modifier.height(40.dp))
            ButtonComponent(value = context.getString(R.string.change_password), onClick = {
                val user = DAOUser.getLoggedUser()
                if (!hasContent(passCurrent.value, passNew.value, passConfirm.value)) {
                    toast(context, context.getString(R.string.empty_fields_error))
                    return@ButtonComponent
                }
                if (!user.userPass.verify(passCurrent.value)) {
                    toast(context, context.getString(R.string.incorrect_password))
                    return@ButtonComponent
                }
                Log.i(context.javaClass.simpleName,"Password verified, attempting the password change now...")
                if (passNew.value != passConfirm.value) {
                    toast(context, context.getString(R.string.passwords_dont_match))
                    return@ButtonComponent
                }

                val newUserPass = UserPass.of(passNew.value.toCharArray())
                if (newUserPass.isEmpty) {
                    toast(context, context.getString(R.string.password_inadequate))
                    return@ButtonComponent
                }
                Log.i(context.javaClass.simpleName,"All verifications complete, attempting modification now")
                user.setUserPass(newUserPass.get())
                val out = DAOUser(context).modify(user)
                if (out == Outcome.SUCCESS) {
                    val view: View = (context as Activity).findViewById(android.R.id.content)
                    snackbar(view, context.getString(R.string.password_changed))
                    open.value = false
                } else Log.i(context.javaClass.simpleName,"Some unknown error occurred when attempting to modify user password")
            })
//            ButtonComponent(value = context.getString(R.string.change_password), onClick = {
//
//            })
        }
    }
}

/**
 * Dialog for account settings. Currently only allows changing password.
 */
@Composable
fun SelectMonthYearDialog(
    open: MutableState<Boolean> = mutableStateOf(true)
) {
    val context = LocalContext.current
    val mutableYear = remember { mutableStateOf(Year.now().toString()) }
    val theMonth = LocalDate.now().month
    val mutableMonth = remember { mutableStateOf(theMonth.getDisplayName(TextStyle.FULL, Locale.getDefault())) }
    val selectMonthIndex = remember { mutableIntStateOf(theMonth.value) }

    GenericDialog(
        open = open,
        headerIcon = R.drawable.user_bigger,
        headerText = stringResource(id = R.string.select_month),
        usesDefaultPlatformWidth = false
    ) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ){
            Spinner(
                text = mutableMonth.value,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(46.dp)
                    .background(Color.White)
                    .align(Alignment.CenterHorizontally)
                    .border(2.dp, color = Black, shape = CircleShape),
                itemList = Month.entries.map {
                    Strings.capitalize(
                        it.getDisplayName(
                            TextStyle.FULL,
                            Locale.getDefault()
                        )
                    )
                },
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
                onSpinnerItemSelected = { index, month ->
                    mutableMonth.value = month
                    selectMonthIndex.intValue = index
                }
            )
            Spacer(modifier = Modifier.height(5.dp))
            CustomTextField(
                labelValue = context.getString(R.string.year),
                textValue = mutableYear.value,
                resource = painterResource(id = R.drawable.calendar),
                function = { return@CustomTextField mutableYear },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(40.dp))
            ButtonComponent(value = context.getString(R.string.switch_month), onClick = {
                val yearStr = mutableYear.value
                if (!hasContent(yearStr) || !Numbers.isNumber(yearStr)) {
                    toast(context, context.getString(R.string.enter_valid_year))
                    return@ButtonComponent
                }
                val yearInt = yearStr.toInt()
                if (yearInt < 1970 || yearInt > Year.now().value) {
                    toast(context, context.getString(R.string.invalid_year))
                    return@ButtonComponent
                }
                open.value = false
                val intent = Intent(context, HomeActivity::class.java)
                    .putExtra("YEAR", yearInt)
                    .putExtra("MONTH", selectMonthIndex.intValue + 1)
                context.startActivity(intent)
                (context as Activity).finish()
            })
        }
    }
}

/**
 * Dialog for confirmation of operation, in this case deleting a bill
 */
//@Composable
//fun ConfirmDeleteDialog(open: MutableState<Boolean>, bill: Bill) {
//    val context = LocalContext.current
//    GenericDialog(open = open, headerIcon = R.drawable.danger, headerText = "Confirm deletion") {
//        ButtonComponent(value = "D", onClick = {
//            val delete = DAOBill(context).delete(bill)
//            if (delete == Outcome.SUCCESS) {
//                open.value = false
//                toast(context, context.getString(R.string.bill_deleted))
//            } else Log.e(context.javaClass.simpleName, "Error deleting bill")
//        })
//    }
//}

@Composable
@Preview
fun SelectMonthYearDialogPreview() {
    SelectMonthYearDialog()
}
