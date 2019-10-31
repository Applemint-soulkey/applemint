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
        articleViewModel.isFilterOpen.value = false
        recycler_article.apply {
            adapter = articleAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (articleViewModel.isFilterOpen.value == true)
                        articleViewModel.isFilterOpen.value = false
                }
            })
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
                container_el_chip_filter.expand()
            }
            else {
                container_el_chip_filter.collapse()
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

        override fun onBindViewHolder(holder: ArticleViewHolder, position: Int)  {
            holder.bind(items[position])
        }

        inner class ArticleViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
            fun bind(data: Article) {
                if (data.content.isNotEmpty()) itemView.tv_article_title.text = data.content
                else itemView.tv_article_title.text = data.url
                itemView.tv_article_desc.text = data.url
                itemView.tv_article_type.text = data.type
                when (data.type) {
                    "youtube"-> itemView.tv_article_type.setBackgroundResource(R.drawable.backgroud_article_youtube_tag)
                    "twitch"-> itemView.tv_article_type.setBackgroundResource(R.drawable.backgroud_article_twtich_tag)
                    "battlepage"-> itemView.tv_article_type.setBackgroundResource(R.drawable.backgroud_article_battlepage_tag)
                    "dogdrip"-> itemView.tv_article_type.setBackgroundResource(R.drawable.backgroud_article_dogdrip_tag)
                    "fmkorea"-> itemView.tv_article_type.setBackgroundResource(R.drawable.backgroud_article_fmkorea_tag)
                    "direct"-> itemView.tv_article_type.setBackgroundResource(R.drawable.backgroud_article_direct_tag)
                    "imgur"-> itemView.tv_article_type.setBackgroundResource(R.drawable.backgroud_article_imgur_tag)
                    else-> itemView.tv_article_type.setBackgroundResource(R.drawable.backgroud_article_etc_tag)
                }
                itemView.setOnClickListener {startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(data.url)))}
                itemView.btn_remove_article.setOnClickListener {articleViewModel.removeArticle(data.fb_id)}
            }
        }
    }
}