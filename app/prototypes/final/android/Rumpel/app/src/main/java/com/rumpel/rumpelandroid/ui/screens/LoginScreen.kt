package com.rumpel.rumpelandroid.ui.screens

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rumpel.rumpelandroid.R
import com.rumpel.rumpelandroid.ui.activities.HomeActivity
import com.rumpel.rumpelandroid.ui.composables.ButtonComponent
import com.rumpel.rumpelandroid.ui.composables.CustomTextField
import com.rumpel.rumpelandroid.ui.composables.DividerTextComponent
import com.rumpel.rumpelandroid.ui.composables.HeadingTextComponent
import com.rumpel.rumpelandroid.ui.composables.IndeterminateCircularIndicator
import com.rumpel.rumpelandroid.ui.composables.NormalTextComponent
import com.rumpel.rumpelandroid.ui.composables.PasswordTextField
import com.rumpel.rumpelandroid.ui.composables.TransparentButton
import com.rumpel.rumpelandroid.ui.elements.TextColor
import com.rumpel.rumpelandroid.utils.Popups
import com.rumpel.rumpelandroid.utils.Popups.toast
import com.rumpel.rumpelandroid.utils.threads.BackgroundTask
import com.rumpel.rumpelandroid.utils.threads.TaskRunner
import com.szaumoor.utils.Strings.hasContent
import java.util.concurrent.atomic.AtomicReference

/**
 * Composable function defining the view in the LoginActivity
 */
@Composable
fun LoginScreen(onLogin: (String, String) -> Boolean, switchToSignUp: () -> Unit) {
    val context = LocalContext.current
    Surface(
        color = Color.White,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(28.dp)
    ) {
        val username = remember { mutableStateOf("") }
        val pass = remember { mutableStateOf("") }
        val loading = remember { mutableStateOf(false) }

        Column(modifier = Modifier.fillMaxSize()) {
            NormalTextComponent(
                value = stringResource(id = R.string.welcome),
                align = TextAlign.Center,
                fontSize = 18.sp,
                maxWidth = true
            )
            HeadingTextComponent(
                value = stringResource(id = R.string.login), textColor = TextColor
            )
            Spacer(modifier = Modifier.height(20.dp))
            CustomTextField(
                labelValue = stringResource(id = R.string.username_or_email),
                resource = painterResource(id = R.drawable.user),
                function = {
                    return@CustomTextField username
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    autoCorrect = false,
                    capitalization = KeyboardCapitalization.None
                )
            )
            PasswordTextField(
                labelValue = stringResource(id = R.string.prompt_password),
                function = {
                    return@PasswordTextField pass
                }
            )

            Spacer(modifier = Modifier.height(60.dp))

            if (!loading.value) {
                ButtonComponent(
                    value = stringResource(id = R.string.login),
                    onClick = {
                            if (hasContent(username.value, pass.value)) { //
                                loading.value = true
                                val ref = AtomicReference(false)
                                TaskRunner.executeAsync(
                                    BackgroundTask({ref.set(onLogin.invoke(username.value, pass.value))},
                                    {
                                        if (ref.get()) {
                                            Log.v("MongoDB", "Authenticated!")
                                            context.startActivity(Intent(context, HomeActivity::class.java))
                                            (context as Activity).finish()
                                        } else {
                                            loading.value = false
                                            Popups.snackbarLg(
                                                (context as Activity).findViewById(android.R.id.content),
                                                context.getString(R.string.failed_to_authenticate)
                                            )
                                        }
                                    })
                                )
                        } else toast(context, context.getString(R.string.empty_credentials))
                    },
                    enabled = !loading.value
                )
            } else {
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    IndeterminateCircularIndicator()
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
            DividerTextComponent()

            Spacer(modifier = Modifier.height(30.dp))

            Row (modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center){
                NormalTextComponent(
                    value = stringResource(id = R.string.not_an_user_yet),
                    align = TextAlign.Center,
                    fontSize = 18.sp, maxWidth = false
                )
                Spacer(modifier = Modifier
                    .height(80.dp)
                    .width(20.dp)
                )
                TransparentButton(
                    value = stringResource(id = R.string.sign_up),
                    onClick = {switchToSignUp.invoke()}
                )
            }
        }
    }
}