package com.rumpel.rumpelandroid.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.rumpel.rumpelandroid.db.DAOUser
import com.rumpel.rumpelandroid.ui.screens.LoginScreen

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        val dao = DAOUser(this)
        setContent {
            LoginScreen(onLogin = {username, pass -> dao.authenticate("username", "1martson1") // placeholder to save time
            }, switchToSignUp = {
                startActivity(Intent(this, SignUpActivity::class.java))
                finish()
            })
        }

    }
}