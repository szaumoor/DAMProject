package com.rumpel.rumpelandroid.ui.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rumpel.rumpelandroid.R
import com.rumpel.rumpelandroid.db.DAOTags
import com.rumpel.rumpelandroid.db.DAOUser
import com.rumpel.rumpelandroid.ui.composables.EntityList
import com.rumpel.rumpelandroid.ui.composables.HeadingTextComponent
import com.rumpel.rumpelandroid.ui.composables.LoggedActivitiesBackbone
import com.rumpel.rumpelandroid.ui.composables.TagDialog
import com.rumpel.rumpelandroid.utils.threads.BackgroundTask
import com.rumpel.rumpelandroid.utils.threads.TaskRunner
import com.szaumoor.rumple.db.utils.Outcome
import com.szaumoor.rumple.model.entities.Tag
import java.util.Optional

/**
 * Composable function defining the view of the TagActivity
 */
@Composable
fun TagScreen2(
    addTag: (Tag) -> Outcome = {Outcome.SUCCESS},
    modifyTag: (Tag) -> Outcome = {Outcome.SUCCESS},
    deleteTag: (Tag) -> Outcome = {Outcome.SUCCESS}) {
    val mutable = remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    val listOfTags = remember {
        mutableStateListOf<Tag>()
    }
    val thread = remember {
        mutableStateOf(true)
    }
    val mode = remember {
        mutableStateOf(true)
    }
    val selectedTag: MutableState<Optional<Tag>> = remember {
        mutableStateOf(Optional.empty())
    }
    val selectedTagName: MutableState<String> = remember {
        mutableStateOf(selectedTag.value.map { it.name }.orElse(""))
    }
    if (thread.value) {
        TaskRunner.executeAsync(
            BackgroundTask(
            {
                listOfTags.addAll(DAOTags(context).all)
            },{
                thread.value = false
            })
        )
    }
    LoggedActivitiesBackbone(
        loggedUser = DAOUser.getLoggedUser(),
        withFloatingButton = true,
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
                            return@TagDialog addTag.invoke(tag)
                        } else Outcome.ERROR
                    })
            } else {
                TagDialog(
                    name = selectedTagName,
                    open = mutable,
                    insert = false,
                    tagModify = { name ->
                        val tag = selectedTag.value
                        if (tag.isEmpty) return@TagDialog Outcome.ERROR
                        if (Tag("").setName(name)) {
                            tag.get().name = name
                            val invoke = modifyTag.invoke(tag.get())
                            if (invoke == Outcome.SUCCESS) {
                                listOfTags.remove(tag.get())
                                listOfTags.add(tag.get())
                            }
                            return@TagDialog invoke
                        } else Outcome.ERROR
                    },
                    tagDelete = {
                        if (selectedTag.value.isEmpty) return@TagDialog Outcome.ERROR
                        val deletion = deleteTag(selectedTag.value.get())
                        if (deletion == Outcome.SUCCESS) listOfTags.remove(selectedTag.value.get())
                        return@TagDialog deletion
                    }
                )
            }
            if (listOfTags.isNotEmpty()) {
                Divider()
                EntityList(
                    list = listOfTags.sortedBy(selector = { it.name }).toMutableList(),
                    onClick = {
                        if (mode.value) mode.value = !mode.value
                        selectedTag.value = Optional.of(it)
                        selectedTagName.value = it.name
                        flipMutator(mutable)
                    }
                )
            } else {
                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp))
                HeadingTextComponent(value = context.getString(R.string.no_elements_present))
            }
        },
        addButtonName = stringResource(id = R.string.add_tag),
        toolBarTitle = stringResource(id = R.string.your_tags)

    ) {
        if (!mode.value) mode.value = !mode.value
        flipMutator(mutable)
    }
}

/**
 * Composable function defining the view of the TagActivity
 */
@Composable
fun TagScreen(
              list: List<Tag> = listOf(),
              addTag: (Tag) -> Outcome = {Outcome.SUCCESS},
              modifyTag: (Tag) -> Outcome = {Outcome.SUCCESS},
              deleteTag: (Tag) -> Outcome = {Outcome.SUCCESS}) {
    val mutable = remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    val listOfTags = remember {
        list.toMutableStateList()
    }
    val mode = remember {
        mutableStateOf(true)
    }
    val selectedTag: MutableState<Tag> = remember {
        mutableStateOf(Tag(""))
    }

    val selectedTagName: MutableState<String> = remember {
        mutableStateOf(selectedTag.value.name)
    }
    LoggedActivitiesBackbone(
        loggedUser = DAOUser.getLoggedUser(),
        withFloatingButton = true,
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
                            return@TagDialog addTag.invoke(tag)
                        } else Outcome.ERROR
                    })
            } else {
                TagDialog(
                    name = selectedTagName,
                    open = mutable,
                    insert = false,
                    tagModify = { name ->
                        val tag = selectedTag.value
                        if (Tag("").setName(name)) {
                            tag.name = name
                            val invoke = modifyTag.invoke(tag)
                            if (invoke == Outcome.SUCCESS) {
                                listOfTags.remove(tag)
                                listOfTags.add(tag)
                            }
                            return@TagDialog invoke
                        } else Outcome.ERROR
                    },
                    tagDelete = {
                        val deletion = deleteTag(selectedTag.value)
                        if (deletion == Outcome.SUCCESS) listOfTags.remove(selectedTag.value)
                        return@TagDialog deletion
                    }
                )
            }
            if (listOfTags.isNotEmpty()) {
                Divider()
                EntityList(
                    list = listOfTags.sortedBy(selector = { it.name }).toMutableList(),
                    onClick = {
                        if (mode.value) mode.value = !mode.value
                        selectedTag.value = it
                        selectedTagName.value = it.name
                        flipMutator(mutable)
                    }
                )
            } else {
                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp))
                HeadingTextComponent(value = context.getString(R.string.no_elements_present))
            }
        },
        addButtonName = stringResource(id = R.string.add_tag),
        toolBarTitle = stringResource(id = R.string.your_tags)

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
}