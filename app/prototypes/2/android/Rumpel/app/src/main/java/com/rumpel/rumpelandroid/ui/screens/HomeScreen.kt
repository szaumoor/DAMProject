package com.rumpel.rumpelandroid.ui.screens

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.rumpel.rumpelandroid.R
import com.rumpel.rumpelandroid.ui.activities.AddBillActivity
import com.rumpel.rumpelandroid.ui.composables.LoggedActivitiesBackbone
import com.szaumoor.rumple.model.entities.User
import com.szaumoor.rumple.model.entities.types.Username


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(loggedUser: User?) {
    val context = LocalContext.current
   LoggedActivitiesBackbone(
       loggedUser = loggedUser,
       composable = { },
       addButtonName = stringResource(id = R.string.add_bill)
   ) {
       context.startActivity(Intent(context, AddBillActivity::class.java))
   }
}

@Preview
@Composable
fun Preview() {
    HomeScreen(User(Username("szaumoor"), null, null))
}