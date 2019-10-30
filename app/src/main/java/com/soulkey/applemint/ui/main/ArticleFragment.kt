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
import androidx.core.view.children

class ArticleFragment : Fragment() {
    internal val articleViewModel by sharedViewModel<MainViewModel>()
    lateinit var articleAdapter: ArticleAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        articleViewModel.initialize()
        return inflater.inflate(R.layout.fragment_articles, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        articleAdapter = ArticleAdapter(listOf())
        recycler_article.apply {
            adapter = articleAdapter
        }

        articleViewModel.getArticles().observe(this, Observer {
            articleAdapter.articles = it
            articleAdapter.filterItems()
        })
        articleViewModel.filters.observe(this, Observer {
            articleAdapter.filters = it
            articleAdapter.filterItems()
        })
        articleViewModel.isFilterOpen.observe(this, Observer {
            if (it) {

            }else {

            }
        })

        articleViewModel.filters.value = getCheckedFilter()
        for (chip in chip_group_filter_article.children){
            chip.setOnClickListener(ChipStateChangedListener())
        }
    }

    inner class ChipStateChangedListener : View.OnClickListener{
        override fun onClick(v: View?) {
            articleViewModel.filters.value = getCheckedFilter()
        }
    }

    internal fun getCheckedFilter(): List<String>{
        val filterList = mutableListOf<String>()
        if (chip_filter_battlepage.isChecked) filterList.add("battlepage")
        if (chip_filter_dogdrip.isChecked) filterList.add("dogdrip")
        if (chip_filter_fmkorea.isChecked) filterList.add("fmkorea")
        if (chip_filter_direct.isChecked) filterList.add("direct")
        if (chip_filter_etc.isChecked) filterList.add("etc")
        if (chip_filter_imgur.isChecked) filterList.add("imgur")
        if (chip_filter_twtich.isChecked) filterList.add("twitch")
        if (chip_filter_youtube.isChecked) filterList.add("youtube")
        return filterList
    }

    inner class ArticleAdapter(list: List<Article>) : RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {
        var articles = list
        var filters = listOf<String>()
        lateinit var items: List<Article>

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
            return ArticleViewHolder(LayoutInflater.from(context).inflate(R.layout.item_article, parent, false))
        }

        fun filterItems() {
            items = if (filters.isNotEmpty()) articles.filter { filters.contains(it.type) } else articles
            notifyDataSetChanged()
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