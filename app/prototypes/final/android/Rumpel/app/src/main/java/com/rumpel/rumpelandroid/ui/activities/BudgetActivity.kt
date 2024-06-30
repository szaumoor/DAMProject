package com.rumpel.rumpelandroid.ui.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.rumpel.rumpelandroid.db.DAOBudgets
import com.rumpel.rumpelandroid.db.DAOPaymentMethods
import com.rumpel.rumpelandroid.ui.screens.BudgetScreen2

/**
 * Activity for the Budget screen.
 */
class BudgetActivity : AppCompatActivity() {
    private lateinit var dao: DAOBudgets
    private lateinit var daoPm: DAOPaymentMethods

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dao = DAOBudgets(this)
        daoPm = DAOPaymentMethods(this)
        setContent {
            BudgetScreen2(
               addBudget = {
                   dao.insert(it)
               },
               modifyBudget = {
                   dao.modify(it)
               },
               deleteBudget = {
                   dao.delete(it)
               }
           )
        }
    }
}