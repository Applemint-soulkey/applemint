package com.soulkey.applemint.ui.bookmark

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.soulkey.applemint.R
import kotlinx.android.synthetic.main.analyze_activity.iv_back_to_main
import kotlinx.android.synthetic.main.bookmark_activity.*
import kotlinx.android.synthetic.main.bookmark_activity.chip_group_bookmark_tags
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class BookmarkActivity : AppCompatActivity() {
    private val viewModel: BookmarkViewModel by viewModel()

    private fun addChip(text: String, chipGroup: ChipGroup){
        Chip(this).apply {
            this.text = "# "+ text
            this.isCloseIconVisible = true
            this.setOnCloseIconClickListener { chipGroup.removeView(this as View) }
            chipGroup.addView(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bookmark_activity)

        et_bookmark_title.setText(intent.getStringExtra("title"))
        et_bookmark_url.setText(intent.getStringExtra("url"))

        et_bookmark_add_tag.setOnEditorActionListener { textView, actionId, event ->
            textView?.let {
                Timber.v("diver:/ ${it.text}")
                if (it.text.isNotEmpty()) addChip(it.text.toString(), chip_group_bookmark_tags)
                it.text = ""
            }
            true
        }

        viewModel.loadCollections().observe(this, Observer { collections ->
            collections.map { it.collectionName }.also { collectionNames ->
                ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, collectionNames).also { adapter->
                    spinner_collection.adapter = adapter
                }
                intent.getStringExtra("collection")?.let {type->
                    spinner_collection.setSelection(collectionNames.indexOf(type))
                }
            }
        })

        iv_back_to_main.setOnClickListener {
            onBackPressed()
        }
    }
}