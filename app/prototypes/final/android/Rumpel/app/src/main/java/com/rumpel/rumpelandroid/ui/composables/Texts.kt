package com.rumpel.rumpelandroid.ui.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rumpel.rumpelandroid.R
import com.rumpel.rumpelandroid.ui.elements.BackgroundColor
import com.rumpel.rumpelandroid.ui.elements.Gray
import com.rumpel.rumpelandroid.ui.elements.Primary
import com.rumpel.rumpelandroid.ui.elements.TextColor
import com.rumpel.rumpelandroid.ui.elements.componentShapes

/**
 * Composable for a normal text
 */
@Composable
fun NormalTextComponent(
    modifier: Modifier = Modifier,
    value: String, align: TextAlign,
    fontSize: TextUnit, maxWidth: Boolean,
    textColor: Color = TextColor
) {
    val newModifier = if (maxWidth) {
        modifier.fillMaxWidth()
    } else {
        modifier.heightIn(min = 40.dp)
    }

    Text(
        text = value,
        modifier = newModifier,
        style = TextStyle(
            fontSize = fontSize,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal
        ),
        color = textColor,
        textAlign = align
    )
}

/**
 * Composable for a heading
 */
@Composable
fun HeadingTextComponent(
    modifier: Modifier = Modifier,
    value: String,
    textColor: Color = TextColor,
    fontSize: Int = 24,
    padding: Int = 0
) {
    Text(
        text = value,
        modifier = modifier
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

/**
 * Composable for a text field
 */
@Composable
fun CustomTextField(
    labelValue: String,
    textValue: String = "",
    resource: Painter,
    function: (String) -> MutableState<String>,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    val mutableTextValue = remember {
        mutableStateOf(textValue)
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
        value = mutableTextValue.value,
        onValueChange = {
            mutableTextValue.value = it
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

/**
 * Composable for a text field designed for passwords
 */
@Composable
fun PasswordTextField(labelValue: String, function: (String) -> MutableState<String>) {
    val password = remember {
        mutableStateOf("")
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

/**
 * A composable of a horizontal divider where there is a text in the center
 */
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
