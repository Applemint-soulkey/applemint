package com.soulkey.applemint.ui.main.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.soulkey.applemint.R
import kotlinx.android.synthetic.main.fragment_articles.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import androidx.core.view.children
import androidx.recyclerview.widget.ItemTouchHelper
import com.google.android.material.snackbar.Snackbar
import com.soulkey.applemint.config.getFilters
import com.soulkey.applemint.ui.main.*
import kotlinx.android.synthetic.main.item_article_foreground.view.*
import kotlinx.android.synthetic.main.view_chip_group_type.*
import kotlinx.android.synthetic.main.view_empty.*
import kotlinx.android.synthetic.main.view_loading.*

class NewArticleFragment : Fragment() {
    internal val articleViewModel by sharedViewModel<ArticleViewModel>()
    private val mainViewModel by sharedViewModel<MainViewModel>()
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
        layout_view_empty.visibility = View.INVISIBLE

        // Loading View 설정
        articleViewModel.isDataLoading.observe(this, Observer {
            layout_view_loading.visibility = if(it) View.VISIBLE else View.INVISIBLE
        })

        // Article Adapter 설정
        articleAdapter = ArticleAdapter(articleViewModel)
        articleViewModel.newArticles.observe(this, Observer {
            layout_view_empty.visibility = if (it.isEmpty()) View.VISIBLE else View.INVISIBLE
            articleAdapter.submitList(it)
            tv_article_remain_count.text = it.size.toString()
        })

        // Filter 적용시 자동으로 Top Scroll
        articleAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver(){
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (itemCount > 1) recycler_article.scrollToPosition(0)
            }
        })
        // Adapter 적용
        recycler_article.apply {
            adapter = articleAdapter
            setOnTouchListener { _, _ ->
                mainViewModel.isFilterOpen.value = false
                false
            }
        }

        //Filter 초기화
        articleViewModel.typeFilter.value = listOf()

        //Filter 적용 여부 표시 설정
        articleViewModel.typeFilter.observe(this, Observer {filter->
            mainViewModel.isFilterApply.value = !filter.isNullOrEmpty()
        })

        //Filter 창 확장여부 적용
        mainViewModel.isFilterOpen.value = false
        mainViewModel.isFilterOpen.observe(this, Observer {
            if (it) container_el_chip_filter.expand()
            else container_el_chip_filter.collapse()
        })

        //Filter 변경 리스너 설정
        for (chip in chip_group_filter_article.children){
            chip.setOnClickListener {
                articleViewModel.typeFilter.value = getFilters(chip_group_filter_article)
            }
        }

        //Swipe Function
        val leftSwipeCallback = ArticleItemTouchHelper(0, ItemTouchHelper.LEFT,
            object : ArticleItemTouchHelper.ArticleItemTouchHelperListener {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int) {
                    val removeItem = (viewHolder as ArticleAdapter.ArticleViewHolder).itemData
                    val removeItemTitle = viewHolder.itemView.tv_article_title.text
                    val removeIndex = articleViewModel.articles.value!!.indexOf(removeItem)
                    Snackbar.make(layout_fragment_article, "'$removeItemTitle' is Deleted", Snackbar.LENGTH_LONG).apply {
                        setAction("UNDO") {articleViewModel.restoreArticle(removeItem, removeIndex)}
                    }.show()
                    articleViewModel.removeArticle(removeItem.fb_id)
                }
            })
        val rightSwipeCallback = ArticleItemTouchHelper(0, ItemTouchHelper.RIGHT,
            object : ArticleItemTouchHelper.ArticleItemTouchHelperListener {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int) {
                    val keepId = (viewHolder as ArticleAdapter.ArticleViewHolder).itemData.fb_id
                    articleViewModel.keepArticle(keepId)
                }
            })
        ItemTouchHelper(leftSwipeCallback).attachToRecyclerView(recycler_article)
        ItemTouchHelper(rightSwipeCallback).attachToRecyclerView(recycler_article)
    }
}

