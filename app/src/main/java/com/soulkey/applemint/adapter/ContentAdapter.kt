package com.soulkey.applemint.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.target.Target

import com.soulkey.applemint.R
import com.bumptech.glide.request.RequestListener as RequestListener

class ContentAdapter(context: Context) : RecyclerView.Adapter<ContentAdapter.ContentViewHolder>(){
    private var contentUrls : ArrayList<String> = ArrayList()
    private var context = context

    inner class ContentViewHolder(view : View) : RecyclerView.ViewHolder(view){
        private val imgView = view.findViewById<ImageView>(R.id.iv_content)
        fun getImgView(): ImageView{
            return this.imgView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder {
        val view  = LayoutInflater.from(parent.context).inflate(R.layout.item_content, parent, false)
        return ContentViewHolder(view)
    }

    override fun getItemCount(): Int {
        return contentUrls.size
    }

    override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
        Glide.with(this.context)
            .load(contentUrls[position])
            .error(R.drawable.ic_baseline_broken_image_24px)
            //.override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
            .fitCenter()
            .listener(object : RequestListener<Drawable>{
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.getImgView().setOnClickListener {
                        Glide.with(context)
                            .load(contentUrls[position])
                            .error(R.drawable.ic_baseline_broken_image_24px)
                            .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                            .fitCenter()
                            .into(holder.getImgView())
                    }
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

            })
            .into(holder.getImgView())
    }

    fun setContentUrls(urls: ArrayList<String>){
        for(url in urls){
            contentUrls.add(url.replace("\"", ""))
        }
    }
}