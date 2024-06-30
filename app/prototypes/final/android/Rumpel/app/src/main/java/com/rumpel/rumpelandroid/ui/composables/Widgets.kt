package com.rumpel.rumpelandroid.ui.composables

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rumpel.rumpelandroid.R
import com.rumpel.rumpelandroid.ui.elements.MediumGray
import com.rumpel.rumpelandroid.ui.elements.PurpleHeader
import com.rumpel.rumpelandroid.ui.elements.TextColor
import com.rumpel.rumpelandroid.ui.elements.White
import com.szaumoor.rumple.utils.Dates
import java.time.LocalDate

/**
 * Composable for an indeterminate progress indicator
 */
@Composable
fun IndeterminateCircularIndicator() {
    CircularProgressIndicator(
        modifier = Modifier
            .heightIn(48.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        trackColor = MaterialTheme.colorScheme.secondary,
    )
}

/**
 * Composable for a dropdown
 */
@Composable
fun Dropdown(itemActionPairs:List<Pair<String, () -> Unit>>, expanded: MutableState<Boolean>) {
    val mutableExpanded = remember { expanded }
    DropdownMenu(
        expanded = mutableExpanded.value,
        onDismissRequest = { mutableExpanded.value = false }
    ) {
        var index = 0
        for (item in itemActionPairs) {
            DropdownMenuItem(
                text = { Text(item.first) },
                onClick = { item.second.invoke() }
            )
            index++
            if (index < itemActionPairs.size) Divider()
        }
    }
}

/**
 * Composable for the toolbar of this app
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppToolbar(toolBarText: String = "Rumpel", function: () -> Unit) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = PurpleHeader
        ),
        title = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Serif,
                letterSpacing = 3.0.sp,
                text = toolBarText,
                color = White
            )
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

/**
 * Composable for a date picker
 */
@Composable
fun CustomDatePicker(mutableDate: MutableState<LocalDate>) {
    val context = LocalContext.current
    val date = remember {
        mutableDate
    }
    val dialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            date.value = LocalDate.of(year, month + 1, dayOfMonth)
            //  init.value = false //?
        }, date.value.year, date.value.monthValue - 1, date.value.dayOfMonth
    )
    Row(Modifier
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
            text = Dates.format(date.value),
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
