package com.rumpel.rumpelandroid.ui.screens

import android.app.Activity
import android.util.Log
import android.view.View
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
import com.szaumoor.rumple.db.utils.Outcome
import com.szaumoor.rumple.model.entities.User
import com.szaumoor.rumple.model.entities.types.UserEmail
import com.szaumoor.rumple.model.entities.types.UserPass
import com.szaumoor.rumple.model.entities.types.Username
import com.szaumoor.utils.Optionals.allPresent
import com.szaumoor.utils.Strings.hasContent
import java.util.concurrent.atomic.AtomicReference

/**
 * Composable function defining the view in the SignUpActivity
 */
@Composable
fun SignUpScreen(onSignUp: (User) -> Outcome, switchToLogin: ()->Unit) {
    val context = LocalContext.current
    Surface(
        color = Color.White,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(28.dp)
    ) {
        val mutableUsername = remember { mutableStateOf("") }
        val mutableEmail = remember { mutableStateOf("") }
        val mutablePass = remember { mutableStateOf("") }
        val mutablePassConfirm = remember { mutableStateOf("") }
        val loading = remember { mutableStateOf(false) }

        Column(modifier = Modifier.fillMaxSize()) {
            NormalTextComponent(
                value = stringResource(id = R.string.hello),
                align = TextAlign.Center,
                fontSize = 18.sp, maxWidth = true
            )
            HeadingTextComponent(
                value = stringResource(id = R.string.create_an_account), textColor = TextColor
            )
            Spacer(modifier = Modifier.height(20.dp))
            CustomTextField(
                labelValue = stringResource(id = R.string.username),
                resource = painterResource(id = R.drawable.user),
                function = {
                    return@CustomTextField mutableUsername
                },
                keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                autoCorrect = false,
                capitalization = KeyboardCapitalization.None
                )
            )
            CustomTextField(
                labelValue = stringResource(id = R.string.prompt_email),
                resource = painterResource(id = R.drawable.arroba),
                function = {
                    return@CustomTextField mutableEmail
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                    autoCorrect = false
                )
            )
            PasswordTextField(
                labelValue = stringResource(id = R.string.prompt_password),
                function = {
                    return@PasswordTextField mutablePass
                }
            )
            PasswordTextField(
                labelValue = stringResource(id = R.string.confirm_password),
                function = {
                    return@PasswordTextField mutablePassConfirm
                }
            )
            Spacer(modifier = Modifier.height(60.dp))
            if (!loading.value) {
                ButtonComponent(
                    value = stringResource(id = R.string.sign_up),
                    onClick = {
                        if (hasContent(mutableUsername.value, mutableEmail. value,mutablePass.value,mutablePassConfirm.value)) {
                            if (mutablePass.value != mutablePassConfirm.value) {
                                toast(context, R.string.passwords_dont_match)
                                return@ButtonComponent
                            }
                            loading.value = true
                            val root = (context as Activity).findViewById<View>(android.R.id.content)
                            val username = Username.of(mutableUsername.value)
                            val email = UserEmail.of(mutableEmail.value)
                            val pass = UserPass.of(mutablePass.value.toCharArray())
                            if (allPresent(username, email, pass)) {
                                val user = User.of(username.get(), email.get(), pass.get())
                                if (user.isPresent) {
                                    val ref = AtomicReference<Outcome>()
                                    TaskRunner.executeAsync(
                                        BackgroundTask({ref.set(onSignUp.invoke(user.get()))},
                                        {
                                            when (ref.get()) {
                                                Outcome.SUCCESS -> toast(context,context.getString(R.string.user_registered))
                                                Outcome.UNIQUE_EXISTS -> Popups.snackbar(root,R.string.unique_exists_error)
                                                Outcome.TIMEOUT -> toast(context,context.getString(R.string.timeout_error))
                                                else -> Log.v("SIGN UP", "Failed to insert user")
                                            }
                                            loading.value = false
                                        }
                                    ))
                                } else{
                                    toast(context, context.getString(R.string.invalid_parameters))
                                    loading.value = false
                                }
                            } else toast(context, context.getString(R.string.invalid_parameters))
                        } else toast(context, R.string.empty_fields_error)
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
                    value = stringResource(id = R.string.already_an_user),
                    align = TextAlign.Center,
                    fontSize = 18.sp, maxWidth = false
                )
                Spacer(modifier = Modifier
                    .height(80.dp)
                    .width(15.dp)
                )
                TransparentButton(
                    value = stringResource(id = R.string.login),
                    onClick = {switchToLogin.invoke()}
                )
            }
        }
    }
}