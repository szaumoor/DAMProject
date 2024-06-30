package com.rumpel.rumpelandroid.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.rumpel.rumpelandroid.db.DAOUser
import com.rumpel.rumpelandroid.ui.screens.LoginScreen
import com.szaumoor.rumple.model.entities.types.UserEmail
import com.szaumoor.rumple.model.entities.types.Username

/**
 * Activity for the login screen
 */
class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        val dao = DAOUser(this)
        setContent {
            LoginScreen(onLogin = {username, pass ->
                if (UserEmail.validate(username)){
                    dao.authenticate(UserEmail(username), pass)
                } else dao.authenticate(Username(username), pass)
            }, switchToSignUp = {
                startActivity(Intent(this, SignUpActivity::class.java))
                finish()
            })
        }
    }
}