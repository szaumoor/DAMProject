package com.rumpel.rumpelandroid.ui.activities.utils

import androidx.compose.runtime.MutableState

/**
 * Interface to identify types that can be selected using checkboxes
 */
interface SelectableItem {
    fun selected(): MutableState<Boolean>
    fun name(): String
}
