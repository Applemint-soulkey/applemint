package com.soulkey.applemint.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.soulkey.applemint.R
import com.soulkey.applemint.model.Article
import kotlinx.android.synthetic.main.fragment_articles.*
import kotlinx.android.synthetic.main.item_article.view.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import android.content.Intent
import android.net.Uri


class ArticleFragment : Fragment() {
    internal val articleViewModel by sharedViewModel<MainViewModel>()
    lateinit var articleAdapter: ArticleAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_articles, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        articleAdapter = ArticleAdapter(listOf())
        recycler_article.apply {
            adapter = articleAdapter
        }
        articleViewModel.getArticles().observe(this, Observer {
            articleAdapter.items = it
            articleAdapter.notifyDataSetChanged()
        })
    }

    inner class ArticleAdapter(var items: List<Article>) : RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
            return ArticleViewHolder(LayoutInflater.from(context).inflate(R.layout.item_article, parent, false))
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
            val data = items[position]
            if (data.content.isNotEmpty()) holder.itemView.tv_article_title.text = data.content
            else holder.itemView.tv_article_title.text = data.url
            holder.itemView.tv_article_desc.text = data.url
            holder.itemView.tv_article_type.text = data.type
            when (data.type) {
                "youtube"-> holder.itemView.tv_article_type.setBackgroundResource(R.drawable.backgroud_article_youtube_tag)
                "twitch"-> holder.itemView.tv_article_type.setBackgroundResource(R.drawable.backgroud_article_twtich_tag)
                "battlepage"-> holder.itemView.tv_article_type.setBackgroundResource(R.drawable.backgroud_article_battlepage_tag)
                "dogdrip"-> holder.itemView.tv_article_type.setBackgroundResource(R.drawable.backgroud_article_dogdrip_tag)
                "fmkorea"-> holder.itemView.tv_article_type.setBackgroundResource(R.drawable.backgroud_article_fmkorea_tag)
                "direct"-> holder.itemView.tv_article_type.setBackgroundResource(R.drawable.backgroud_article_direct_tag)
                "imgur"-> holder.itemView.tv_article_type.setBackgroundResource(R.drawable.backgroud_article_imgur_tag)
                else-> holder.itemView.tv_article_type.setBackgroundResource(R.drawable.backgroud_article_etc_tag)
            }
            holder.itemView.setOnClickListener {startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(data.url)))}
            holder.itemView.btn_remove_article.setOnClickListener {articleViewModel.removeArticle(data.fb_id)}
        }

        inner class ArticleViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    }
}