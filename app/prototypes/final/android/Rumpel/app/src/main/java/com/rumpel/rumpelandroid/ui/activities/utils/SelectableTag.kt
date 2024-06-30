package com.rumpel.rumpelandroid.ui.activities.utils

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.szaumoor.rumple.model.entities.Tag

/**
 * Data class to identify Tag types that can be selected using checkboxes
 */
data class SelectableTag(val tag: Tag, val selected: MutableState<Boolean>) : SelectableItem {
    constructor(tag: Tag) : this(tag, mutableStateOf(false))

    override fun toString(): String {
        return tag.name
    }

    override fun selected(): MutableState<Boolean> {
        return selected
    }

    override fun name(): String {
        return tag.name
    }
}