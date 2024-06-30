package com.rumpel.rumpelandroid.ui.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.rumpel.rumpelandroid.db.DAOTags
import com.rumpel.rumpelandroid.ui.screens.TagScreen

class TagActivity : AppCompatActivity() {
    private lateinit var dao: DAOTags
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dao = DAOTags(this)
        setContent { 
            TagScreen(
                list = dao.all.sortedBy(selector = {it.name}),
                addTag = { tag -> dao.insert(tag)},
                modifyTag = {tag -> dao.modify(tag)},
                deleteTag = {tag -> dao.delete(tag)}
            )
        }
    }
}