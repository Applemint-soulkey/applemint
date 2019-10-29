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

class ArticleFragment : Fragment() {
    private val articleViewModel by sharedViewModel<MainViewModel>()
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
            if (data.title.isNotEmpty()) holder.itemView.tv_article_title.text = data.title
            else holder.itemView.tv_article_title.text = data.url
            holder.itemView.tv_article_desc.text = data.url
            holder.itemView.tv_article_type.text = data.type
        }

        inner class ArticleViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    }
}