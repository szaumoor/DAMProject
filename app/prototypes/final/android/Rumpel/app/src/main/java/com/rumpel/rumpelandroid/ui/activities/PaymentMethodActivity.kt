package com.rumpel.rumpelandroid.ui.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.rumpel.rumpelandroid.db.DAOPaymentMethods
import com.rumpel.rumpelandroid.ui.screens.PaymentMethodScreen2

/**
 * Activity for the PaymentMethod screen.
 */
class PaymentMethodActivity : AppCompatActivity() {
    private lateinit var dao: DAOPaymentMethods

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dao = DAOPaymentMethods(this)
        setContent {
            PaymentMethodScreen2(
               // list = dao.all.sortedBy(selector = {it.name}).toMutableList(),
                addPm = {dao.insert(it)},
                modifyPm = {dao.modify(it)},
                deletePm = {dao.delete(it)}
            )
        }
    }
}