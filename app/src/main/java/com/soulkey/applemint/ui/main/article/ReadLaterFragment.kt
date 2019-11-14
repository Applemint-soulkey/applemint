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
import kotlinx.android.synthetic.main.item_article_foreground.view.*
import kotlinx.android.synthetic.main.view_chip_group_type.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ReadLaterFragment : Fragment() {
    private val mainViewModel by sharedViewModel<MainViewModel>()
    internal val articleViewModel by sharedViewModel<ArticleViewModel>()
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
        articleAdapter = ArticleAdapter(listOf(), articleViewModel).also { recycler_article.adapter = it }
        articleViewModel.getReadLaters().observe(this, Observer {
            articleAdapter.articles = it
            articleAdapter.items = it.toMutableList()
            articleAdapter.notifyDataSetChanged()
        })
        mainViewModel.isFilterOpen.value = false
        mainViewModel.isFilterOpen.observe(this, Observer {
            if (it) container_el_chip_filter.expand()
            else container_el_chip_filter.collapse()
        })
        for (chip in chip_group_filter_article.children){
            chip.setOnClickListener { articleAdapter.filter(chip_group_filter_article) }
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
                    articleViewModel.removeArticle(removeItem.fb_id)
                }
            })
        ItemTouchHelper(leftSwipeCallback).attachToRecyclerView(recycler_article)
    }
}