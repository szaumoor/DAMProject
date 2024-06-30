package com.rumpel.rumpelandroid.ui.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.rumpel.rumpelandroid.db.DAOUser
import com.rumpel.rumpelandroid.ui.screens.HomeScreen
import java.time.LocalDate
import java.time.Month
import java.time.Year

/**
 * Activity for the home screen
 */
class HomeActivity : AppCompatActivity() {
    private var year: Year = Year.now()
    private var month: Month = LocalDate.now().month

    private fun unpackBundle(bundle: Bundle?) {
        if (bundle != null) {
            year = Year.of(bundle.getInt("YEAR"))
            month = Month.of(bundle.getInt("MONTH"))
        } else Log.e(javaClass.simpleName, "Bundle is null")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        unpackBundle(intent.extras)
        setContent {
            HomeScreen(DAOUser.getLoggedUser(), year, month)
        }
    }
}