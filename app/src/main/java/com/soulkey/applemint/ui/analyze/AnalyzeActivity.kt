package com.soulkey.applemint.ui.analyze

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.bumptech.glide.Glide
import com.soulkey.applemint.R
import kotlinx.android.synthetic.main.analyze_activity.*
import kotlinx.android.synthetic.main.item_analyze_external.view.*
import kotlinx.android.synthetic.main.item_analyze_media.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class AnalyzeActivity : AppCompatActivity(){
    private val viewModel: AnalyzeViewModel by viewModel()
    private var mediaAdapter: MediaAdapter = MediaAdapter(listOf())
    private var externalAdapter: ExternalAdapter = ExternalAdapter(listOf())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.analyze_activity)
        viewModel.callAnalyze(intent.getStringExtra("id"))
        viewModel.targetTitle.observe(this, Observer {text->
            tv_content_analyzed_title.text = text
        })
        recycler_analyzed_media.adapter = mediaAdapter
        viewModel.mediaContents.observe(this, Observer { urlList->
            urlList?.let {
                mediaAdapter.urls = urlList
                mediaAdapter.notifyDataSetChanged()
                tv_count_analyzed_media.text = "(${urlList.size})"
            }
        })
        viewModel.externalLinks.observe(this, Observer { urlList->
            urlList?.let {
                externalAdapter.urls = urlList
                externalAdapter.notifyDataSetChanged()
                tv_count_analyzed_media.text = "(${urlList.size})"
            }
        })

        btn_edit_title.setOnClickListener {
            MaterialDialog(this).show {
                title(text="Edit Title")
                input(prefill = viewModel.targetTitle.value) { _, charSequence ->
                    viewModel.targetTitle.value = charSequence.toString()
                }
                positiveButton()
                negativeButton()
            }
        }

        btn_save_analyzed_media.setOnClickListener {
            viewModel.dapinaTest()
            Toast.makeText(it.context, "Dapina is Requested! Check your Dropbox!", Toast.LENGTH_SHORT).show()
        }
    }

    inner class MediaAdapter(var urls: List<String>) : RecyclerView.Adapter<MediaAdapter.MediaViewHolder>() {
        inner class MediaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            fun bind(url: String){
                url.split('/').also { urlBlock->
                    itemView.tv_filename_analyzed_media.text = urlBlock[urlBlock.size-1]
                }
                Glide
                    .with(itemView.context)
                    .load(url)
                    .into(itemView.iv_analyzed_media)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
            return MediaViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_analyze_media, parent, false))
        }

        override fun getItemCount(): Int {
            return urls.size
        }

        override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
            holder.bind(urls[position])
        }
    }

    inner class ExternalAdapter(var urls: List<String>) : RecyclerView.Adapter<ExternalAdapter.ExternalViewHolder>(){
        inner class ExternalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            fun bind(position: Int){
                itemView.tv_analyze_external_link.text = urls[position]
                itemView.setOnClickListener {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(urls[position])))
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExternalViewHolder {
            return ExternalViewHolder((LayoutInflater.from(parent.context).inflate(R.layout.item_analyze_external, parent, false)))
        }

        override fun getItemCount(): Int {
            return urls.size
        }

        override fun onBindViewHolder(holder: ExternalViewHolder, position: Int) {
            holder.bind(position)
        }
    }
}