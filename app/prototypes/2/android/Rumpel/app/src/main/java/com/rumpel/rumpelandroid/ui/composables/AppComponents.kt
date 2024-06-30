package com.rumpel.rumpelandroid.ui.composables

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.rumpel.rumpelandroid.R
import com.rumpel.rumpelandroid.db.DAOUser
import com.rumpel.rumpelandroid.ui.activities.BudgetActivity
import com.rumpel.rumpelandroid.ui.activities.HomeActivity
import com.rumpel.rumpelandroid.ui.activities.LoginActivity
import com.rumpel.rumpelandroid.ui.activities.PaymentMethodActivity
import com.rumpel.rumpelandroid.ui.activities.TagActivity
import com.rumpel.rumpelandroid.ui.elements.BackgroundColor
import com.rumpel.rumpelandroid.ui.elements.Black
import com.rumpel.rumpelandroid.ui.elements.Gray
import com.rumpel.rumpelandroid.ui.elements.LinkColor
import com.rumpel.rumpelandroid.ui.elements.MediumGray
import com.rumpel.rumpelandroid.ui.elements.Primary
import com.rumpel.rumpelandroid.ui.elements.PurpleHeader
import com.rumpel.rumpelandroid.ui.elements.Secondary
import com.rumpel.rumpelandroid.ui.elements.TextColor
import com.rumpel.rumpelandroid.ui.elements.White
import com.rumpel.rumpelandroid.ui.elements.componentShapes
import com.rumpel.rumpelandroid.utils.Popups.toast
import com.skydoves.orchestra.spinner.Spinner
import com.skydoves.orchestra.spinner.SpinnerProperties
import com.szaumoor.rumple.db.utils.Outcome
import com.szaumoor.rumple.model.entities.Budget
import com.szaumoor.rumple.model.entities.PaymentMethod
import com.szaumoor.rumple.model.entities.User
import com.szaumoor.rumple.model.interfaces.Entity
import com.szaumoor.rumple.utils.Dates
import com.szaumoor.rumple.utils.Money
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Currency
import java.util.stream.Collectors

@Composable
fun NormalTextComponent(
    value: String, align: TextAlign,
    fontSize: TextUnit, maxWidth: Boolean,
    textColor: Color = TextColor
) {
    val modifier = if (maxWidth) {
        Modifier.fillMaxWidth()
    } else {
        Modifier.heightIn(min = 40.dp)
    }

    Text(
        text = value,
        modifier = modifier,
        style = TextStyle(
            fontSize = fontSize,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal
        ),
        color = textColor,
        textAlign = align
    )
}

@Composable
fun HeadingTextComponent(
    value: String,
    textColor: Color = TextColor,
    fontSize: Int = 24,
    padding: Int = 0
) {
    Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn()
            .padding(padding.dp),
        style = TextStyle(
            fontSize = fontSize.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal
        ),
        color = textColor,
        textAlign = TextAlign.Center
    )
}

@Composable
fun CustomTextField(
    labelValue: String,
    resource: Painter,
    function: (String) -> MutableState<String>,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    val textValue = remember {
        mutableStateOf("") //placeholder
    }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn()
            .clip(componentShapes.small),
        label = { Text(text = labelValue, color = Gray) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = BackgroundColor,
            unfocusedContainerColor = BackgroundColor,
            disabledContainerColor = BackgroundColor,
            cursorColor = Primary,
            focusedBorderColor = Primary,
            focusedLabelColor = Primary,
        ),
        keyboardOptions = keyboardOptions,
        value = textValue.value,
        onValueChange = {
            textValue.value = it
            val atomicReference = function.invoke(it)
            atomicReference.value = it
        },
        leadingIcon = {
            Icon(
                painter = resource,
                contentDescription = "",
                modifier = Modifier
                    .height(16.dp)
                    .width(16.dp)
            )
        }

    )
}

@Composable
fun PasswordTextField(labelValue: String, function: (String) -> MutableState<String>) {
    val password = remember {
        mutableStateOf("aaaaaaaa") // placeholder
    }

    val passwordVisible = remember {
        mutableStateOf(false)
    }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn()
            .clip(componentShapes.small),
        label = { Text(text = labelValue, color = Gray) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = BackgroundColor,
            unfocusedContainerColor = BackgroundColor,
            disabledContainerColor = BackgroundColor,
            cursorColor = Primary,
            focusedBorderColor = Primary,
            focusedLabelColor = Primary,
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        value = password.value,
        onValueChange = {
            password.value = it
            val atomicReference = function.invoke(it)
            atomicReference.value = it
        },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.key),
                contentDescription = "",
                modifier = Modifier
                    .height(16.dp)
                    .width(16.dp)
            )
        },
        trailingIcon = {
            val iconImage = if (passwordVisible.value) {
                Icons.Filled.Visibility
            } else {
                Icons.Filled.VisibilityOff
            }

            val description = if (passwordVisible.value) {
                stringResource(id = R.string.hide_password)
            } else {
                stringResource(id = R.string.show_password)
            }

            IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                Icon(imageVector = iconImage, contentDescription = description)
            }
        },

        visualTransformation = if (passwordVisible.value) VisualTransformation.None
        else PasswordVisualTransformation()
    )
}

@Composable
fun CheckBoxComponent(value: String, fontSize: TextUnit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(56.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val checkedState = remember {
            mutableStateOf(false)
        }

        Checkbox(checked = checkedState.value,
            onCheckedChange = {
                checkedState.value != checkedState.value
            })

        NormalTextComponent(value = value, align = TextAlign.Start, fontSize = fontSize, true)
    }
}

@Composable
fun ButtonComponent(value: String, onClick: () -> Unit, enabled: Boolean = true) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(48.dp),
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(Color.Transparent),
        enabled = enabled
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(48.dp)
                .background(
                    brush = Brush.horizontalGradient(listOf(Secondary, Primary)),
                    shape = RoundedCornerShape(50.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = value,
                fontSize = 18.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun CustomImageButton(onClick: () -> Unit, iconImage: Int, modifier: Modifier = Modifier) {
    IconButton(
        onClick = { onClick.invoke() },
        modifier = Modifier.clip(RectangleShape),
            //.border(width = 1.dp, color = Black, shape = RectangleShape),
        content = {
            Icon(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .clip(RectangleShape),
                painter = painterResource(id = iconImage),
                contentDescription = stringResource(id = R.string.menu),
            )
        },
    )
}


@Composable
fun TransparentButton(
    value: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    textDecoration: TextDecoration = TextDecoration.Underline,
    textColor: Color = LinkColor,
    contentAlignment: Alignment = Alignment.TopCenter
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .heightIn(48.dp),
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(Color.Transparent)
    ) {
        Box(
            modifier = modifier
                .heightIn(48.dp)
                .padding(2.dp)
                .background(
                    brush = Brush.horizontalGradient(listOf(Color.Transparent, Color.Transparent)),
                    shape = RoundedCornerShape(50.dp),
                    alpha = 0.0f
                ),
            contentAlignment = contentAlignment
        ) {
            Text(
                text = value,
                fontSize = 18.sp,
                color = textColor,
                fontWeight = FontWeight.Bold,
                textDecoration = textDecoration
            )
        }
    }
}

@Composable
fun DividerTextComponent() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    )
    {
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.0f),
            color = Gray,
            thickness = 1.dp
        )
        Text(
            modifier = Modifier.padding(8.dp),
            text = stringResource(id = R.string.or),
            fontSize = 18.sp,
            color = TextColor
        )
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.0f),
            color = Gray,
            thickness = 1.dp
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppToolbar(function: () -> Unit) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = PurpleHeader
        ),
        title = {
            Text(text = "    " + stringResource(id = R.string.rumpel), color = White)
        },
        navigationIcon = {
            IconButton(
                onClick = { function.invoke() },
                content = {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = stringResource(id = R.string.menu),
                        tint = White
                    )
                }
            )
        },


        )
}

@Composable
fun <T : Entity> EntityList(
    list: List<T>, modifier: Modifier = Modifier
        .fillMaxWidth()
        .background(color = White, shape = RectangleShape),
    onClick: (T) -> Unit?
) {
    Box(modifier) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            items(list) { pm ->
                TransparentButton(
                    value = pm.toString(),
                    onClick = { onClick.invoke(pm) },
                    modifier = Modifier.fillMaxSize(),
                    textColor = TextColor,
                    textDecoration = TextDecoration.None,
                    contentAlignment = Alignment.CenterStart,
                )
                Divider()
            }
        }
    }
}

@Composable
fun BudgetButton(
    value: Budget,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val pm = if (value.paymentMethod == null)
        stringResource(id = R.string.universal)
    else value.paymentMethod.name
    Button(
        onClick = onClick,
        modifier = modifier
            .heightIn(48.dp)
            .fillMaxWidth(),
        //  contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(Color.Transparent)
    ) {

        Box(
            modifier = modifier
                .heightIn(48.dp)
                .padding(2.dp)
                .background(
                    color = Color.Transparent,
                    shape = RectangleShape,
                ),

            ) {
            Column(
                modifier = modifier
                    .heightIn(48.dp)
                    .padding(2.dp)
                    .background(
                        color = Color.Transparent,
                        shape = RectangleShape
                    )
            ) {

                Text(
                    text = stringResource(id = R.string.interval) + ": ${value.interval}",
                    fontSize = 18.sp,
                    color = TextColor,
                    fontWeight = FontWeight.Normal,
                )
                Text(
                    text = stringResource(id = R.string.limits) + ": ${value.limit}",
                    fontSize = 18.sp,
                    color = TextColor,
                    fontWeight = FontWeight.Normal,
                )
                Text(
                    text = stringResource(id = R.string.currency) + ": ${value.limit.currency}",
                    fontSize = 18.sp,
                    color = TextColor,
                    fontWeight = FontWeight.Normal,
                )
                Text(
                    text = stringResource(id = R.string.pm) + ": $pm",
                    fontSize = 18.sp,
                    color = TextColor,
                    fontWeight = FontWeight.Normal,
                )
            }

        }
    }
}

@Composable
fun BudgetList(
    list: List<Budget>, modifier: Modifier = Modifier
        .fillMaxWidth()
        .background(color = White, shape = RectangleShape),
    onClick: (Budget) -> Unit?
) {

    Box(modifier) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            items(list) { budget ->
                BudgetButton(
                    value = budget,
                    onClick = { onClick.invoke(budget) },
                )
                Divider()
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoggedActivitiesBackbone(
    loggedUser: User?,
    composable: @Composable () -> Unit?,
    addButtonName: String,
    addButtonFunction: () -> Unit
) {
    var context = LocalContext.current
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var openDialog = remember { mutableStateOf(false) }
    val openDrawer: () -> Unit = { scope.launch { drawerState.open() } }
    val closeDrawer: () -> Unit = { scope.launch { drawerState.close() } }
    AboutDialog(openDialog)
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier
                        .background(color = PurpleHeader, shape = RectangleShape)
                        .fillMaxWidth()
                        .padding(25.dp),
                ) {
                    HeadingTextComponent(
                        value = stringResource(id = R.string.rumpel),
                        textColor = White,
                        fontSize = 30,
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    NormalTextComponent(
                        value = stringResource(id = R.string.welcome_comma) + " ${loggedUser?.username?.username}!",
                        align = TextAlign.Center,
                        fontSize = 16.sp,
                        maxWidth = true,
                        textColor = White
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }
                NavigationDrawerItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.nav_view_home),
                            contentDescription = ""
                        )
                    },
                    label = { Text(text = stringResource(id = R.string.nav_home)) },
                    selected = false,
                    onClick = {
                        if (context is HomeActivity) closeDrawer()
                        else context.startActivity(Intent(context, HomeActivity::class.java))
                    }
                )
                NavigationDrawerItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.payment_methods),
                            contentDescription = ""
                        )
                    },
                    label = { Text(text = stringResource(id = R.string.nav_payment_methods)) },
                    selected = false,
                    onClick = {
                        if (context is PaymentMethodActivity) closeDrawer()
                        else context.startActivity(
                            Intent(
                                context,
                                PaymentMethodActivity::class.java
                            )
                        )
                    }
                )
                NavigationDrawerItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.nav_menu_budgets),
                            contentDescription = ""
                        )
                    },
                    label = { Text(text = stringResource(id = R.string.nav_budgets)) },
                    selected = false,
                    onClick = {
                        if (context is BudgetActivity) closeDrawer()
                        else context.startActivity(Intent(context, BudgetActivity::class.java))
                    }
                )
                NavigationDrawerItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.nav_menu_tags),
                            contentDescription = ""
                        )
                    },
                    label = { Text(text = stringResource(id = R.string.nav_tags)) },
                    selected = false,
                    onClick = {
                        if (context is TagActivity) closeDrawer()
                        else context.startActivity(Intent(context, TagActivity::class.java))
                    }
                )
                NavigationDrawerItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.nav_view_settings),
                            contentDescription = ""
                        )
                    },
                    label = { Text(text = stringResource(id = R.string.nav_settings)) },
                    selected = false,
                    onClick = { }
                )
                NavigationDrawerItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.nav_view_about),
                            contentDescription = ""
                        )
                    },
                    label = { Text(text = stringResource(id = R.string.nav_about)) },
                    selected = false,
                    onClick = {
                        openDialog.value = true
                    }
                )
                NavigationDrawerItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.nav_logout),
                            contentDescription = ""
                        )
                    },
                    label = { Text(text = stringResource(id = R.string.nav_logout)) },
                    selected = false,
                    onClick = {
                        DAOUser.logout()
                        val intent = Intent(context, LoginActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK) // prevents coming back to app as if still logged in
                        context.startActivity(intent)
                        (context as Activity).finish()
                    }
                )
            }
        },
    ) {
        BackHandler(
            enabled = drawerState.isOpen,
            onBack = closeDrawer
        )
        Scaffold(
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    text = { Text(addButtonName) },
                    icon = { Icon(Icons.Filled.Add, contentDescription = "") },
                    onClick = {
                        scope.launch {
                            addButtonFunction.invoke()
                        }
                    }
                )
            },
            content = {
                Column {
                    AppToolbar(openDrawer)
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)
                            .padding(16.dp)
                    ) {
                        composable.invoke()
                    }
                }
            }
        )
    }
}

@Composable
fun AboutDialog(open: MutableState<Boolean>) {
    var openDialog = open
    if (openDialog.value) {
        Dialog(
            onDismissRequest = { openDialog.value = false },
            properties = DialogProperties(
                usePlatformDefaultWidth = false
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
                        painter = painterResource(id = R.drawable.money_icon),
                        contentDescription = "",
                    )
                }

                HeadingTextComponent(
                    value = stringResource(id = R.string.app_name) + " | 1.0v",
                )
                Spacer(modifier = Modifier.height(10.dp))
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
                        openDialog.value = false
                    }
                )
            }
        }
    }
}

@Composable
fun TagDialog(
    open: MutableState<Boolean>,
    tagModify: (String) -> Outcome = { Outcome.SUCCESS },
    tagDelete: () -> Outcome = { Outcome.SUCCESS },
    insert: Boolean = true
) {
    val context = LocalContext.current
    var openDialog = open
    val name = remember {
        mutableStateOf("")
    }
    val heading = if (insert) R.string.new_tag else R.string.modify_tag
    val buttonName = if (insert) R.string.insert_tag else R.string.modify
    if (openDialog.value) {
        Dialog(
            onDismissRequest = { openDialog.value = false },
            properties = DialogProperties(
                usePlatformDefaultWidth = false
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
                        painter = painterResource(id = R.drawable.nav_menu_tags),
                        contentDescription = "",
                    )
                }

                HeadingTextComponent(
                    value = stringResource(id = heading)
                )
                Spacer(modifier = Modifier.height(10.dp))
                CustomTextField(
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
                        val outcome = tagModify(name.value)
                        if (outcome == Outcome.SUCCESS)
                            openDialog.value = false
                        else if (outcome == Outcome.NOT_FOUND) toast(context, "Tag not found!")
                        else toast(context, "Something went wrong!")
                    }
                )
                if (!insert) {
                    ButtonComponent(
                        value = stringResource(id = R.string.delete),
                        onClick = {
                            val outcome = tagDelete()
                            if (outcome == Outcome.SUCCESS)
                                openDialog.value = false
                            else toast(context, "Something went wrong!")
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun BudgetDialog(
    list: List<PaymentMethod>,
    open: MutableState<Boolean>,
    budgetModify: (ZonedDateTime, ZonedDateTime, BigDecimal, BigDecimal, PaymentMethod?, Currency) -> Outcome = {_,_,_,_,_,_ -> Outcome.SUCCESS },
    budgetDelete: () -> Outcome = { Outcome.SUCCESS },
    insert: Boolean = true
) {
    val context = LocalContext.current
    var openDialog = open
    val startDate = remember {
        mutableStateOf(LocalDate.now())
    }
    val endDate = remember {
        mutableStateOf(LocalDate.now())
    }
    val softLimit = remember {
        mutableStateOf("0")
    }
    val hardLimit = remember {
        mutableStateOf("0")
    }
    val heading = if (insert) R.string.new_budget else R.string.modify_budget
    val buttonName = if (insert) R.string.insert_budget else R.string.modify
    if (openDialog.value) {
        Dialog(
            onDismissRequest = { openDialog.value = false },
            properties = DialogProperties(
                usePlatformDefaultWidth = false
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
                        painter = painterResource(id = R.drawable.nav_menu_budgets),
                        contentDescription = "",
                    )
                }

                HeadingTextComponent(
                    value = stringResource(id = heading)
                )
                Spacer(modifier = Modifier.height(10.dp))
                val methodList = remember { list }
                val selectedPm = remember { mutableStateOf(PaymentMethod(""))}
                val selectedItem = remember {
                    mutableStateOf(
                        context.getString(R.string.choose_payment_method)
                    )
                }
                Spinner(
                    text = selectedItem.value,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(46.dp)
                        .background(Color.White)
                        .align(Alignment.CenterHorizontally)
                        .border(2.dp, color = Black, shape = RectangleShape),
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
                        selectedPm.value = list[index]
                        selectedItem.value = selectedPm.value.name
                    }
                )
                val currList = remember { Money.currenciesSortedByCode() }
                val selectedCurr = remember {
                    mutableStateOf(
                        context.getString(R.string.choose_currency)
                    )
                }
                Spacer(modifier = Modifier.height(5.dp))
                Spinner(
                    text = selectedCurr.value,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(46.dp)
                        .background(Color.White)
                        .align(Alignment.CenterHorizontally)
                        .border(2.dp, color = Black, shape = RectangleShape),
                    itemList = currList.stream().map { it.currencyCode }.collect(Collectors.toList()),
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
                        selectedCurr.value = currList[index].currencyCode
                    }
                )
                Spacer(modifier = Modifier.height(5.dp))
                CustomDatePicker(context.getString(R.string.start_date), startDate)
                Spacer(modifier = Modifier.height(5.dp))
                CustomDatePicker(context.getString(R.string.end_date), endDate)
                Spacer(modifier = Modifier.height(5.dp))
                CustomTextField(
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
                            softLimit.value.toBigDecimal(),
                            hardLimit.value.toBigDecimal(),
                            selectedPm.value,
                            Currency.getInstance(selectedCurr.value)
                        )
                        if (outcome == Outcome.SUCCESS)
                            openDialog.value = false
                        else if (outcome == Outcome.NOT_FOUND) toast(context, "Budget not found!")
                        else toast(context, "Something went wrong!")
                    }
                )
                if (!insert) {
                    ButtonComponent(
                        value = stringResource(id = R.string.delete),
                        onClick = {
                            val outcome = budgetDelete()
                            if (outcome == Outcome.SUCCESS)
                                openDialog.value = false
                            else toast(context, "Something went wrong!")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun PaymentMethodDialog(
    open: MutableState<Boolean>,
    pmModify: (String) -> Outcome = { Outcome.SUCCESS },
    pmDelete: () -> Outcome = { Outcome.SUCCESS },
    insert: Boolean = true
) {
    val context = LocalContext.current
    var openDialog = open
    val name = remember {
        mutableStateOf("")
    }
    val heading = if (insert) R.string.new_pm else R.string.modify_method
    val buttonName = if (insert) R.string.insert_payment_method else R.string.modify
    if (openDialog.value) {
        Dialog(
            onDismissRequest = { openDialog.value = false },
            properties = DialogProperties(
                usePlatformDefaultWidth = false
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
                        painter = painterResource(id = R.drawable.payment_methods),
                        contentDescription = "",
                    )
                }

                HeadingTextComponent(
                    value = stringResource(id = heading)
                )
                Spacer(modifier = Modifier.height(10.dp))
                CustomTextField(
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
                        val outcome = pmModify(name.value)
                        if (outcome == Outcome.SUCCESS)
                            openDialog.value = false
                        else if (outcome == Outcome.NOT_FOUND) toast(context, "Method not found!")
                        else toast(context, "Something went wrong!")
                    }
                )
                if (!insert) {
                    ButtonComponent(
                        value = stringResource(id = R.string.delete),
                        onClick = {
                            val outcome = pmDelete()
                            if (outcome == Outcome.SUCCESS)
                                openDialog.value = false
                            else toast(context, "Something went wrong!")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun IndeterminateCircularIndicator() {
    CircularProgressIndicator(
        modifier = Modifier
            .heightIn(48.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        trackColor = MaterialTheme.colorScheme.secondary,
    )
}

@Composable
fun CustomDatePicker(placeholderText: String, mutableDate: MutableState<LocalDate>) {
    val context = LocalContext.current
    val date = remember {
        mutableDate
    }

    val init = remember {
        mutableStateOf(true)
    }

    val dialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            date.value = LocalDate.of(year, month + 1, dayOfMonth)
            init.value = false //?
        }, date.value.year, date.value.monthValue - 1, date.value.dayOfMonth
    )

    Row(
        Modifier
            .fillMaxWidth()
            .background(
                color = White,
                shape = RectangleShape
            )
            .border(1.dp, MediumGray, RectangleShape),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        Text(
            modifier = Modifier
                .weight(0.85F)
                .padding(8.dp),
            text = if (init.value) placeholderText else Dates.format(date.value),
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            color = TextColor

        )
        CustomImageButton(
            modifier = Modifier
                .weight(0.15F)
                .height(32.dp)
                .width(32.dp),
            onClick = { dialog.show() },
            iconImage = R.drawable.calendar
        )
    }


}

@Preview
@Composable
fun preview() {
//    var b1 = Budget(
//        Interval(ZonedDateTime.now(), ZonedDateTime.now().plusDays(7)),
//        Limit(BigDecimal("1000"), BigDecimal("2000"), Currency.getInstance(Locale.getDefault())),
//        PaymentMethod("Cash"),
//        User(Username("szaumoor"), null, null)
//    )
//    var b2 = Budget(
//        Interval(ZonedDateTime.now(), ZonedDateTime.now().plusDays(7)),
//        Limit(BigDecimal("1000"), BigDecimal("2000"), Currency.getInstance(Locale.getDefault())),
//        null,
//        User(Username("szaumoor"), null, null)
//    )
//    BudgetList(
//        list = listOf(b1, b2)
//    )
    BudgetDialog(open = remember {
        mutableStateOf(true)
    }, list = listOf())
}
