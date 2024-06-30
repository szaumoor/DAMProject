package com.rumpel.rumpelandroid.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.rumpel.rumpelandroid.db.DAOUser
import com.rumpel.rumpelandroid.ui.screens.SignUpScreen

/**
 * Activity for the sign up screen
 */
class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dao = DAOUser(this)
        setContent {
            SignUpScreen(onSignUp = { user ->
                dao.insert(user)
            }, switchToLogin = {
                startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
                finish()
            })
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
        finish()
        super.onBackPressed()
    }
}