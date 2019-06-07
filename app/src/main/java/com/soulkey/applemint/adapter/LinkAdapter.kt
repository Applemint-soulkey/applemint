package com.soulkey.applemint.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.soulkey.applemint.DetailActivity.ExternalLink
import com.soulkey.applemint.R
import java.util.logging.Handler as Handler1

class LinkAdapter(val linkItemClick: (ExternalLink)->Unit) : RecyclerView.Adapter<LinkAdapter.LinkViewHolder>() {
    private var externalLinks: ArrayList<ExternalLink> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LinkViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_link, parent, false)
        return LinkViewHolder(view)
    }

    override fun getItemCount(): Int {
        return externalLinks.size
    }

    override fun onBindViewHolder(holder: LinkViewHolder, position: Int) {
        holder.bind(externalLinks[position])
    }

    inner class LinkViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val linkTitle = view.findViewById<TextView>(R.id.tv_link_title)
        private val linkText = view.findViewById<TextView>(R.id.tv_link_url)

        fun bind(externalLink: ExternalLink) {
            linkTitle.text = externalLink.title
            linkText.text = externalLink.url
            itemView.setOnClickListener{
                linkItemClick(externalLink)
            }
        }
    }

    fun setLinkData(linkData: ArrayList<ExternalLink>){
        externalLinks = linkData
        notifyDataSetChanged()
    }
}