package com.rumpel.rumpelandroid.ui.composables

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rumpel.rumpelandroid.R
import com.rumpel.rumpelandroid.db.DAOUser
import com.rumpel.rumpelandroid.ui.activities.BudgetActivity
import com.rumpel.rumpelandroid.ui.activities.HomeActivity
import com.rumpel.rumpelandroid.ui.activities.LoginActivity
import com.rumpel.rumpelandroid.ui.activities.PaymentMethodActivity
import com.rumpel.rumpelandroid.ui.activities.TagActivity
import com.rumpel.rumpelandroid.ui.elements.PurpleHeader
import com.rumpel.rumpelandroid.ui.elements.White
import com.rumpel.rumpelandroid.utils.Preferences
import com.szaumoor.rumple.model.entities.User
import kotlinx.coroutines.launch

/**
 * Composable function that generates the backbone of the logged activities.
 * It has a menu side bar, a floating action button, and a toolbar.
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoggedActivitiesBackbone(
    loggedUser: User?,
    composable: @Composable () -> Unit?,
    withFloatingButton: Boolean,
    addButtonName: String? = "",
    toolBarTitle: String? = "",
    addButtonFunction: () -> Unit? = {}
) {
    val context = LocalContext.current
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val openDialog = remember { mutableStateOf(false) }
    val openReportDialog = remember { mutableStateOf(false) }
    val openSettingsDialog = remember { mutableStateOf(false) }
    val openAccountDialog = remember { mutableStateOf(false) }
    val openDrawer: () -> Unit = { scope.launch { drawerState.open() } }
    val closeDrawer: () -> Unit = { scope.launch { drawerState.close() } }

    val dataStore = Preferences(context)
    val savedCurrency = dataStore.getCurrency.collectAsState(initial = "")

    AboutDialog(openDialog)
    ReportChooserDialog(openReportDialog, closeDrawer, savedCurrency)
    SettingsDialog(openSettingsDialog, savedCurrency)
    AccountDialog(open = openAccountDialog)
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
                        value = context.getString(R.string.rumpel),
                        textColor = White,
                        fontSize = 30,
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    NormalTextComponent(
                        value = stringResource(id = R.string.welcome_comma) + " ${loggedUser?.username?.value}!",
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
                        if (context !is HomeActivity) {
                            (context as Activity).finish()
                            context.startActivity(Intent(context, HomeActivity::class.java))
                        }
                        closeDrawer()
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
                        if (context !is PaymentMethodActivity)  {
                            context.startActivity(Intent(context, PaymentMethodActivity::class.java))
                            if (context !is HomeActivity) {
                                (context as Activity).finish()
                            }
                        }
                        closeDrawer()
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
                        if (context !is BudgetActivity) {
                            context.startActivity(Intent(context, BudgetActivity::class.java))
                            if (context !is HomeActivity) {
                                (context as Activity).finish()
                            }
                        }
                        closeDrawer()
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
                        if (context !is TagActivity) {
                            context.startActivity(Intent(context, TagActivity::class.java))
                            if (context !is HomeActivity) {
                                (context as Activity).finish()
                            }
                        }
                        closeDrawer()
                    }
                )
                NavigationDrawerItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.report),
                            contentDescription = ""
                        )
                    },
                    label = { Text(text = stringResource(id = R.string.reports)) },
                    selected = false,
                    onClick = {
                        openReportDialog.value = true
                    }
                )
                Divider(color = Color.LightGray, thickness = 1.dp)
                NavigationDrawerItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.nav_view_settings),
                            contentDescription = ""
                        )
                    },
                    label = { Text(text = stringResource(id = R.string.nav_settings)) },
                    selected = false,
                    onClick = {
                        openSettingsDialog.value = true
                    }
                )
                NavigationDrawerItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.user_bigger),
                            contentDescription = ""
                        )
                    },
                    label = { Text(text = stringResource(id = R.string.account)) },
                    selected = false,
                    onClick = {
                        openAccountDialog.value = true
                    }
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
            onBack = {
                if (drawerState.isClosed && context !is HomeActivity) {
                    (context as Activity).finish()
                    context.startActivityIfNeeded(Intent(context, HomeActivity::class.java), 1)
                }
                closeDrawer.invoke()
            }
        )
        Scaffold(
            floatingActionButton = {
                if (withFloatingButton) {
                    ExtendedFloatingActionButton(
                        text = {
                            if (addButtonName != null) {
                                Text(addButtonName)
                            }
                        },
                        icon = { Icon(Icons.Filled.Add, contentDescription = "") },
                        onClick = {
                            scope.launch {
                                addButtonFunction.invoke()
                            }
                        }
                    )
                }
            },
            content = {
                Column {
                    if (toolBarTitle == null) AppToolbar(function = openDrawer)
                    else AppToolbar(toolBarTitle ,function = openDrawer)
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