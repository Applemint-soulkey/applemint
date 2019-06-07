package com.soulkey.applemint

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.soulkey.applemint.adapter.ArticleAdapter
import com.soulkey.applemint.model.article.Article
import com.soulkey.applemint.utils.ArticleUtil
import com.soulkey.applemint.viewmodel.ArticleViewModel

class ArticleFragment : Fragment() {
    private lateinit var articleViewModel: ArticleViewModel
    private lateinit var recyclerViewArticle : RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_article, container, false)

        val articleItemClick: (Article)->Unit = {
            val intent = Intent(activity, DetailActivity::class.java)
            intent.putExtra("fb_id", it.firebase_id)
            intent.putExtra("title", it.title)
            intent.putExtra("text", it.text)
            intent.putExtra("url", it.url)
            intent.putExtra("domain", it.domain)
            intent.putExtra("bookmark", it.bookmark)
            intent.putExtra("content_urls", it.content_urls)
            intent.putExtra("external_urls", it.external_urls)
            intent.putExtra("tags", it.tags)
            startActivity(intent)
        }

        val bookmarkClick: (Article)->Unit = {
            articleViewModel.toggleBookmark(it.firebase_id, !it.bookmark)
        }

        val adapter = ArticleAdapter(this, articleItemClick, bookmarkClick)
        recyclerViewArticle = view.findViewById(R.id.rv_article)
        recyclerViewArticle.layoutManager = LinearLayoutManager(this.context)
        recyclerViewArticle.adapter = adapter
        recyclerViewArticle.setHasFixedSize(true)

        articleViewModel = ViewModelProviders.of(this).get(ArticleViewModel::class.java)
        articleViewModel.getAll().observe(this, Observer<List<Article>>{
            adapter.setArticles(it!!)
        })

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        ArticleUtil().getAllData(articleViewModel)
    }
}