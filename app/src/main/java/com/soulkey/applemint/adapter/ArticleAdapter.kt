package com.soulkey.applemint.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.soulkey.applemint.ArticleFragment
import com.soulkey.applemint.R
import com.soulkey.applemint.model.article.Article

class ArticleAdapter(context: ArticleFragment, val articleItemClick: (Article)->Unit, val bookmarkClick:(Article)->Unit) : RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>(){
    private var articles: List<Article> = listOf()
    private var context = context

    inner class ArticleViewHolder(view: View) : RecyclerView.ViewHolder(view){
        private val headingText = view.findViewById<TextView>(R.id.tv_heading_text)
        private val urlText = view.findViewById<TextView>(R.id.tv_article_url)
        private val thumbnailImage = view.findViewById<ImageView>(R.id.iv_thumb)
        private val bookmarkButton = view.findViewById<ImageButton>(R.id.btn_bookmark)
        fun bind(article: Article){
            headingText.text = article.title
            urlText.text = article.url
            bookmarkButton.isActivated = article.bookmark
            if(article.content_urls.size > 0){
                val thumbnailUrl = article.content_urls[0].replace("\"", "")
                Glide.with(context)
                    .load(thumbnailUrl)
                    .thumbnail(0.25F)
                    .centerCrop()
                    .into(thumbnailImage)
            }
            bookmarkButton.setOnClickListener {
                bookmarkClick(article)
            }
            itemView.setOnClickListener {
                articleItemClick(article)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_article, parent, false)
        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(articles[position])
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    fun setArticles(articles: List<Article>){
        this.articles = articles
        notifyDataSetChanged()
    }
}