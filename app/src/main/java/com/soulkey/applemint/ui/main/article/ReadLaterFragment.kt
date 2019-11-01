package com.soulkey.applemint.ui.main.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.soulkey.applemint.R
import com.soulkey.applemint.ui.main.MainViewModel
import kotlinx.android.synthetic.main.fragment_articles.*
import kotlinx.android.synthetic.main.item_article.view.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ReadLaterFragment : Fragment() {
    internal val articleViewModel by sharedViewModel<MainViewModel>()
    lateinit var articleAdapter: ArticleAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        articleAdapter = ArticleAdapter(articleViewModel.getInitialData().filter { it.state == "keep" })
        return inflater.inflate(R.layout.fragment_articles, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

        articleViewModel.getReadLaters().observe(this, Observer {
            articleAdapter.articles = it
        })
        articleViewModel.filters.observe(this, Observer {
            articleAdapter.filters = it
            articleAdapter.filterItems()
        })
        articleViewModel.isFilterOpen.observe(this, Observer {
            if (it) container_el_chip_filter.expand()
            else container_el_chip_filter.collapse()
        })
        articleViewModel.filters.value = getCheckedFilter(view)
        for (chip in chip_group_filter_article.children){
            chip.setOnClickListener(ChipStateChangedListener())
        }

        val leftSwipeCallback = ArticleItemTouchHelper(0, ItemTouchHelper.LEFT,
            object : ArticleItemTouchHelper.ArticleItemTouchHelperListener {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int) {
                    articleAdapter.items.removeAt(position)
                    articleAdapter.notifyItemRemoved(position)
                    val removeItem = (viewHolder as ArticleAdapter.ArticleViewHolder).itemData
                    val removeItemTitle = viewHolder.itemView.tv_article_title.text
                    Snackbar.make(layout_fragment_article, "$removeItemTitle is Deleted", Snackbar.LENGTH_LONG).also {
                        it.setAction("UNDO") {
                            articleViewModel.restoreArticle(removeItem)
                            articleAdapter.items.add(position, removeItem)
                            articleAdapter.notifyItemInserted(position)
                        }
                    }.show()
                    val removeId = removeItem.fb_id
                    articleViewModel.removeArticle(removeId)
                }
            })
        ItemTouchHelper(leftSwipeCallback).attachToRecyclerView(recycler_article)

    }

    inner class ChipStateChangedListener : View.OnClickListener{
        override fun onClick(v: View?) {
            articleViewModel.filters.value = getCheckedFilter(view)
        }
    }
}