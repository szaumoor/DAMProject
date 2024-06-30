package com.rumpel.rumpelandroid.ui.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.rumpel.rumpelandroid.db.DAOUser
import com.rumpel.rumpelandroid.ui.screens.HomeScreen

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HomeScreen(DAOUser.getLoggedUser())
        }
    }
}