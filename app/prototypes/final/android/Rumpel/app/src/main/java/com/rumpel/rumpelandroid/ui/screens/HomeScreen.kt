package com.rumpel.rumpelandroid.ui.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rumpel.rumpelandroid.R
import com.rumpel.rumpelandroid.db.DAOBill
import com.rumpel.rumpelandroid.ui.activities.AddBillActivity
import com.rumpel.rumpelandroid.ui.activities.HomeActivity
import com.rumpel.rumpelandroid.ui.composables.BillList
import com.rumpel.rumpelandroid.ui.composables.HeadingTextComponent
import com.rumpel.rumpelandroid.ui.composables.LoggedActivitiesBackbone
import com.rumpel.rumpelandroid.ui.composables.SelectMonthYearDialog
import com.rumpel.rumpelandroid.ui.elements.Gray
import com.rumpel.rumpelandroid.utils.threads.BackgroundTask
import com.rumpel.rumpelandroid.utils.threads.TaskRunner
import com.szaumoor.rumple.model.entities.Bill
import com.szaumoor.rumple.model.entities.User
import com.szaumoor.rumple.model.entities.types.Username
import com.szaumoor.utils.Strings.capitalize
import org.bson.BsonObjectId
import java.time.LocalDate
import java.time.Month
import java.time.Year
import java.time.format.TextStyle
import java.util.Locale

/**
 * Composable defining the view of the home activity
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(loggedUser: User?,
               year: Year = Year.now(),
               month: Month = LocalDate.now().month
) {
    val context = LocalContext.current
    val open = remember {
        mutableStateOf(false)
    }
    val loaded = remember {
        mutableStateOf(false)
    }
    val list = mutableListOf<Bill>()
    TaskRunner.executeAsync(
        BackgroundTask(
            {
                list.addAll(DAOBill(context).getAllFiltered(year, month))
            }, {
                loaded.value = true
            })
    )
    SelectMonthYearDialog(open = open,)
    LoggedActivitiesBackbone(
        loggedUser = loggedUser,
        withFloatingButton = true,
        composable = {
            HeadingTextComponent(
                modifier = Modifier.clickable(onClick = {
                    open.value = true
                }),
                value = capitalize(month.getDisplayName(TextStyle.FULL, Locale.getDefault())) + ", " + year.value
            )
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(4.dp))
            Divider(Modifier.fillMaxWidth(), thickness = 1.dp, color = Gray)
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(8.dp))
            BillList(
                year = year,
                month = month,
                loaded = loaded,
                list = list,
                onClick = {
                val intent = Intent(context, AddBillActivity::class.java)
                val theId = (it.id as BsonObjectId).value.toString()
                intent.putExtra("BILL_ID", theId)
                context.startActivity(intent)
                if (context is HomeActivity) {
                    context.finish()
                }
            })
        },
        addButtonName = stringResource(id = R.string.add_bill),
        toolBarTitle = stringResource(id = R.string.your_bills),
    ) {
        context.startActivity(Intent(context, AddBillActivity::class.java))
        (context as Activity).finish()
    }
}

@Composable
@Preview
fun HomeScreenPreview() {
    HomeScreen(loggedUser = User(Username("szaumoor"), null, null))
}
