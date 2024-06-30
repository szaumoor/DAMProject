package com.rumpel.rumpelandroid.ui.composables


import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.rumpel.rumpelandroid.R
import com.rumpel.rumpelandroid.db.DAOBill
import com.rumpel.rumpelandroid.ui.activities.HomeActivity
import com.rumpel.rumpelandroid.ui.activities.utils.HomeViewModel
import com.rumpel.rumpelandroid.ui.activities.utils.SelectableItem
import com.rumpel.rumpelandroid.ui.elements.TextColor
import com.rumpel.rumpelandroid.ui.elements.White
import com.rumpel.rumpelandroid.utils.Popups.toast
import com.szaumoor.rumple.db.utils.Outcome
import com.szaumoor.rumple.model.entities.Bill
import com.szaumoor.rumple.model.entities.Budget
import com.szaumoor.rumple.model.entities.ItemBill
import com.szaumoor.rumple.model.interfaces.Entity
import java.time.LocalDate
import java.time.Month
import java.time.Year


/**
 * Composable function that creates a list of entities with a particular function attached when clicked.
 * The name associated with each item will depend on the entity's toString() function.
 */
@Composable
fun <T : Entity> EntityList(
    list: MutableList<T>, modifier: Modifier = Modifier,
    onClick: (T) -> Unit?,
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

/**
 * Composable function to create specifically a list of budgets, which all the relevant information
 * that should be read on a glance.
 */
@Composable
fun BudgetList(
    list: List<Budget>, modifier: Modifier = Modifier,
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

/**
 * Composable function to create specifically a list of items, showing the name of the item and the price.
 */
@Composable
fun ItemList(
    list: SnapshotStateList<ItemBill>,
    modifier: Modifier = Modifier,
    onClick: (ItemBill) -> Unit?
) {
    Box(modifier) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            items(list) { item ->
                ItemButton(
                    value = item,
                    onClick = { onClick.invoke(item) },
                )
                Divider()
            }
        }
    }
}

/**
 * Composable function to create specifically a list of bills from a year and month, which some relevant information for each.
 *
 */
@Composable
fun BillList(
    modifier: Modifier = Modifier,
    list: MutableList<Bill>,
    year: Year = Year.now(),
    month: Month = LocalDate.now().month,
    onClick: (Bill) -> Unit?,
    loaded: MutableState<Boolean>
) {
    val rememberedList = remember { list }
    val context = LocalContext.current
    val viewModel = viewModel<HomeViewModel>()
    val isLoading by viewModel.isLoading.collectAsState()
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isLoading)

    Box(modifier) {
        if (!loaded.value) IndeterminateCircularIndicator()
        else {
            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = {
                    viewModel.loadStuff {
                        rememberedList.clear()
                        rememberedList.addAll(DAOBill(context).getAllFiltered(year, month))
                    }
                },
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(rememberedList) { bill ->
                        BillButton(
                            value = bill,
                            onClick = { onClick.invoke(bill)  },
                            onDelete = {
                                val theBill = it
                                val delete = DAOBill(context).delete(theBill)
                                if (delete == Outcome.SUCCESS) {
                                    toast(context, R.string.bill_deleted)
                                    context.startActivity(Intent(context, HomeActivity::class.java))
                                    if (context is HomeActivity) {
                                        context.finish()
                                    }
                                }
                            }
                                )
                        Divider()
                    }
                }
            }
        }

    }
}

/**
 * Composable function to create a list of checkboxes, with selectable items associated with them
 */
@Composable
fun <T : SelectableItem> CheckBoxList(
    list: MutableList<T>,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = White, shape = RectangleShape)
    ) {
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            var start = true
            items(list) { pm ->
                if (start) {
                    Divider()
                    start = false
                }
                TransparentCheckBoxButton(str = pm.name(), checked = pm.selected())
                Divider()
            }
        }
    }
}