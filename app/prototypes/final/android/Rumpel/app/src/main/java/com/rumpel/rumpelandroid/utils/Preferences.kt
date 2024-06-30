package com.rumpel.rumpelandroid.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Currency
import java.util.Locale

/**
 * Kotlin class that handles preferences using DataStore
 */
class Preferences(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
        val CURRENCY_KEY = stringPreferencesKey("currency")
    }


    val getCurrency: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[CURRENCY_KEY] ?: Currency.getInstance(Locale.getDefault()).currencyCode
        }

    suspend fun savePreferredCurrency(currency: String) {
        context.dataStore.edit { settings ->
            settings[CURRENCY_KEY] = currency
        }
    }
}
