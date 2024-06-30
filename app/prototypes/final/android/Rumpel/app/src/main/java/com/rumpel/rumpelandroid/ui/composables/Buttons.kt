package com.rumpel.rumpelandroid.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rumpel.rumpelandroid.R
import com.rumpel.rumpelandroid.ui.elements.LinkColor
import com.rumpel.rumpelandroid.ui.elements.Primary
import com.rumpel.rumpelandroid.ui.elements.Secondary
import com.rumpel.rumpelandroid.ui.elements.TextColor
import com.rumpel.rumpelandroid.ui.elements.White
import com.szaumoor.rumple.model.entities.Bill
import com.szaumoor.rumple.model.entities.Budget
import com.szaumoor.rumple.model.entities.ItemBill
import com.szaumoor.rumple.utils.Dates
import com.szaumoor.rumple.utils.Money
import java.util.Currency

/**
 * Button that creates a dropdown menu on clicking
 */
@Composable
fun ButtonDropdown(
    text: String,
    expanded: MutableState<Boolean>,
    itemActionPairs: List<Pair<String, () -> Unit>>
) {
    Button(
        onClick = { expanded.value = !expanded.value },
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(48.dp),
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(Color.Transparent),
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
                text = text,
                fontSize = 18.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Dropdown(itemActionPairs = itemActionPairs, expanded = expanded)
        }
    }
}

/**
 * Basic button
 */
@Composable
fun ButtonComponent(modifier: Modifier? = Modifier, value: String, onClick: () -> Unit, enabled: Boolean = true) {
    val theModifier = modifier ?: Modifier.fillMaxWidth()

    Button(
        onClick = onClick,
        modifier = theModifier
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

/**
 * Button with an icon and no text
 */
@Composable
fun CustomImageButton(onClick: () -> Unit, iconImage: Int, modifier: Modifier = Modifier) {
    IconButton(
        onClick = { onClick.invoke() },
        modifier = Modifier.clip(RectangleShape),
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

/**
 * Transparent button, used for things like links.
 */
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
        contentPadding = PaddingValues(5.dp, 5.dp),
        colors = ButtonDefaults.buttonColors(Color.Transparent)
    ) {
        Box(
            modifier = modifier
                .heightIn(48.dp)
                .padding(2.dp)
                .background(
                    brush = Brush.horizontalGradient(listOf(Color.Transparent, Color.Transparent)),
                    shape = RectangleShape,
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

/**
 * Button with a checkbox. Designed so that the checkbox is ticked on and off
 * no matter where you tap in the whole area
 */
@Composable
fun TransparentCheckBoxButton(checked: MutableState<Boolean>, str: String) {

    Button(
        onClick = { checked.value = !checked.value },
        colors = ButtonDefaults.buttonColors(Color.White),
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterStart,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(White, RectangleShape),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Bottom
            ) {
                Checkbox(
                    modifier = Modifier
                        //.fillMaxWidth()
                        .background(color = White, shape = RectangleShape),
                    checked = checked.value,
                    onCheckedChange = {
                        println("Changing from ${checked.value} to $it")
                        checked.value = it
                    })
                NormalTextComponent(
                    modifier = Modifier.padding(top = 5.dp),
                    value = str,
                    align = TextAlign.Start, fontSize = 18.sp, maxWidth = false
                )
            }
        }
    }
}

/**
 * Button specifically designed for budgets, each containing the various bits of relevant data about them
 */
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

/**
 * Button specifically designed for items inside a bill
 */
@Composable
fun ItemButton(
    value: ItemBill,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .heightIn(32.dp)
            .fillMaxWidth(),
        //  contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(Color.Transparent)
    ) {
        Box(
            modifier = modifier
                .heightIn(32.dp)
                .padding(2.dp)
                .background(
                    color = Color.Transparent,
                    shape = RectangleShape,
                ),
        ) {
            Column(
                modifier = modifier
                    .heightIn(20.dp)
                    .padding(2.dp)
                    .background(
                        color = Color.Transparent,
                        shape = RectangleShape
                    )
            ) {
                Text(
                    text = stringResource(id = R.string.name) + ": ${value.name}  " +
                            stringResource(id = R.string.price) + ": ${
                        Money.formatCurrency(
                            value.price,
                            Currency.getInstance("EUR")
                        )
                    }",
                    fontSize = 18.sp,
                    color = TextColor,
                    fontWeight = FontWeight.Normal,
                )
            }

        }
    }
}

/**
 * Button specifically designed for a whole bill
 */
@Composable
fun BillButton(
    modifier: Modifier = Modifier,
    value: Bill,
    onClick: () -> Unit,
    onDelete: (Bill) -> Unit,
    expanded: MutableState<Boolean> = mutableStateOf(false)
) {
    val context = LocalContext.current
    Button(
        onClick = { expanded.value = !expanded.value },
        modifier = modifier
            .heightIn(32.dp)
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(Color.Transparent)
    ) {
        Box(
            modifier = modifier
                .heightIn(32.dp)
                .padding(2.dp)
                .background(
                    color = Color.Transparent,
                    shape = RectangleShape,
                ),
        ) {
            Column(
                modifier = modifier
                    .heightIn(20.dp)
                    .padding(2.dp)
                    .background(
                        color = Color.Transparent,
                        shape = RectangleShape
                    )
            ) {
                Text(
                    text = stringResource(id = R.string.date) + ": ${Dates.format(value.date)}",
                    fontSize = 18.sp,
                    color = TextColor,
                    fontWeight = FontWeight.Normal,
                )
                Text(
                    text = stringResource(id = R.string.total) + Money.formatCurrency(
                        value.total,
                        value.currency
                    ),
                    fontSize = 18.sp,
                    color = TextColor,
                    fontWeight = FontWeight.Normal,
                )
                Text(
                    text = stringResource(id = R.string.elements) + ": ${value.size()}",
                    fontSize = 18.sp,
                    color = TextColor,
                    fontWeight = FontWeight.Normal,
                )
            }
            Dropdown(itemActionPairs = listOf(
                Pair(context.getString(R.string.edit_bill)) { onClick.invoke() },
                Pair(context.getString(R.string.delete)) { onDelete.invoke(value) }),
                expanded = expanded)
        }
    }
}
