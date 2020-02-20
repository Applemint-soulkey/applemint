package com.soulkey.applemint.ui.analyze

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.soulkey.applemint.R
import kotlinx.android.synthetic.main.analyze_activity.*
import kotlinx.android.synthetic.main.item_analyze_media.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class AnalyzeActivity : AppCompatActivity() {
    private val viewModel: AnalyzeViewModel by viewModel()
    private var mediaAdapter: MediaAdapter = MediaAdapter(listOf())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.analyze_activity)
        viewModel.callAnalyze(intent.getStringExtra("id")!!)

        viewModel.targetTitle.observe(this, Observer { text ->
            et_content_analyze_title.setText(text)
            btn_edit_title.isEnabled = true
        })

        recycler_analyzed_media.adapter = mediaAdapter
        recycler_analyzed_media.layoutManager = GridLayoutManager(this, 2)
        recycler_analyzed_media.addItemDecoration(GridSpacingItemDecoration(2, resources.getDimensionPixelOffset(R.dimen.outline_margin), true, 0))
        viewModel.mediaContents.observe(this, Observer { urlList ->
            urlList?.let {
                mediaAdapter.urls = urlList
                mediaAdapter.notifyDataSetChanged()
                tv_count_analyzed_media.text = "(${urlList.size})"
                btn_save_analyzed_media.isEnabled = true
                recycler_analyzed_media.visibility = View.VISIBLE
                animationView_loading.visibility = View.GONE
            }
        })

        btn_edit_title.setOnClickListener {
            MaterialDialog(this).show {
                title(text = "Edit Title")
                input(prefill = viewModel.targetTitle.value) { _, charSequence ->
                    viewModel.targetTitle.value = charSequence.toString()
                }
                positiveButton()
                negativeButton()
            }
        }

        btn_save_analyzed_media.setOnClickListener {
            viewModel.dapina()
            it.isEnabled = false
            Toast.makeText(it.context,"Dapina is Requested! Check your Dropbox!",Toast.LENGTH_SHORT).show()
        }

        iv_back_to_main.setOnClickListener {
            onBackPressed()
        }
    }

    inner class GridSpacingItemDecoration(
        private val spanCount: Int,
        private val spacing: Int,
        private val includeEdge: Boolean,
        private val headerNum: Int
    ) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val position = parent.getChildAdapterPosition(view) - headerNum
            if (position >= 0) {
                val column = position % spanCount // item column
                if (includeEdge) {
                    outRect.left =
                        spacing - column * spacing / spanCount // spacing - column * ((1f / spanCount) * spacing)
                    outRect.right =
                        (column + 1) * spacing / spanCount // (column + 1) * ((1f / spanCount) * spacing)

                    if (position < spanCount) { // top edge
                        outRect.top = spacing
                    }
                    outRect.bottom = spacing // item bottom
                } else {
                    outRect.left =
                        column * spacing / spanCount // column * ((1f / spanCount) * spacing)
                    outRect.right =
                        spacing - (column + 1) * spacing / spanCount // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                    if (position >= spanCount) {
                        outRect.top = spacing // item top
                    }
                }
            } else {
                outRect.left = 0
                outRect.right = 0
                outRect.top = 0
                outRect.bottom = 0
            }
        }
    }

    inner class MediaAdapter(var urls: List<String>) :
        RecyclerView.Adapter<MediaAdapter.MediaViewHolder>() {
        inner class MediaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(url: String) {
                Glide
                    .with(itemView.context)
                    .load(url)
                    .transform(CenterCrop(), RoundedCorners(14))
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .thumbnail(0.25f)
                    .into(itemView.iv_analyzed_media)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
            return MediaViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_analyze_media,
                    parent,
                    false
                )
            )
        }

        override fun getItemCount(): Int {
            return urls.size
        }

        override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
            holder.bind(urls[position])
        }
    }
}