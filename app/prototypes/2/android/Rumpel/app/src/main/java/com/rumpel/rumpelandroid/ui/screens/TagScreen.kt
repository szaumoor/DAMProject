package com.rumpel.rumpelandroid.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.res.stringResource
import com.rumpel.rumpelandroid.R
import com.rumpel.rumpelandroid.db.DAOUser
import com.rumpel.rumpelandroid.ui.composables.EntityList
import com.rumpel.rumpelandroid.ui.composables.LoggedActivitiesBackbone
import com.rumpel.rumpelandroid.ui.composables.TagDialog
import com.szaumoor.rumple.db.utils.Outcome
import com.szaumoor.rumple.model.entities.Tag

@Composable
fun TagScreen(list: List<Tag> = listOf(), addTag: (Tag) -> Outcome = {Outcome.SUCCESS},
              modifyTag: (Tag) -> Outcome = {Outcome.SUCCESS},
              deleteTag: (Tag) -> Outcome = {Outcome.SUCCESS}) {
    val mutable = remember {
        mutableStateOf(false)
    }
    val listOfTags = remember {
        list.toMutableStateList()
    }
    val mode = remember {
        mutableStateOf(true)
    }
    val selectedTag: MutableState<Tag> = remember {
        mutableStateOf(Tag(""))
    }
    LoggedActivitiesBackbone(
        loggedUser = DAOUser.getLoggedUser(),
        composable = {
            if (mode.value) {
                TagDialog(
                    open = mutable,
                    insert = true,
                    tagModify = { name ->
                        val opTag = Tag.of(name)
                        if (opTag.isPresent) {
                            val tag = opTag.get()
                            tag.user = DAOUser.getLoggedUser()
                            listOfTags.add(tag)
                            val invoke = addTag.invoke(tag)
                            return@TagDialog invoke
                        } else Outcome.ERROR
                    })
            } else {
                TagDialog(
                    open = mutable,
                    insert = false,
                    tagModify = { name ->
                        val tag = selectedTag.value
                        if (Tag("").setName(name)) {
                            listOfTags.remove(tag)
                            tag.name = name
                            listOfTags.add(tag)
                            return@TagDialog modifyTag.invoke(tag)
                        } else Outcome.ERROR

                    },
                    tagDelete = {
                        val deletion = deleteTag(selectedTag.value)
                        if (deletion == Outcome.SUCCESS) listOfTags.remove(selectedTag.value)
                        return@TagDialog deletion
                    }
                )
            }
            EntityList(
                list = listOfTags.sortedBy(selector = { it.name }),
                onClick = {
                    flipToModify(mode, selectedTag, it)
                    flipMutator(mutable)
                }
            )
        },
        addButtonName = stringResource(id = R.string.add_tag),

    ) {
        if (!mode.value) mode.value = !mode.value
        flipMutator(mutable)
    }

}

private fun flipMutator(mutable: MutableState<Boolean>){
    mutable.value = !mutable.value
}

private fun flipToModify(mode: MutableState<Boolean>, selectedTag: MutableState<Tag>, newTag: Tag) {
    if (mode.value) mode.value = !mode.value
    selectedTag.value = newTag
    println("Selected tag: $selectedTag")
}