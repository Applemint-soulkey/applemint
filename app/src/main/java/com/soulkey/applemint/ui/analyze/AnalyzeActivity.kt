package com.soulkey.applemint.ui.analyze

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.soulkey.applemint.R
import kotlinx.android.synthetic.main.analyze_activity.*
import kotlinx.android.synthetic.main.item_analyze_media.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class AnalyzeActivity : AppCompatActivity(){
    private val viewModel: AnalyzeViewModel by viewModel()
    private var mediaAdapter: MediaAdapter = MediaAdapter(listOf())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.analyze_activity)
        viewModel.callAnalyze(intent.getStringExtra("id"))
        viewModel.targetTitle.observe(this, Observer {text->
            tv_content_analyzed_title.text = text
        })
        recycler_analyzed_media.adapter = mediaAdapter
        viewModel.mediaContents.observe(this, Observer { urlList->
            Timber.v("diver: on Activity/ $urlList")
            mediaAdapter.urls = urlList
            mediaAdapter.notifyDataSetChanged()
        })

    }

    inner class MediaAdapter(var urls: List<String>) : RecyclerView.Adapter<MediaAdapter.MediaViewHolder>() {
        inner class MediaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            fun bind(url: String){
                itemView.tv_test.text = url
                Glide
                    .with(itemView.context)
                    .load(url)
                    //.placeholder(R.drawable.ic_add_circle_24px)
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
}