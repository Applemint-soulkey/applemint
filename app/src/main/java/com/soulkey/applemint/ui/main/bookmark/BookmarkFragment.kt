package com.soulkey.applemint.ui.main.bookmark

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.soulkey.applemint.R
import com.soulkey.applemint.config.getFilters
import com.soulkey.applemint.ui.main.MainViewModel
import kotlinx.android.synthetic.main.fragment_bookmark.*
import kotlinx.android.synthetic.main.item_bookmark_foreground.view.*
import kotlinx.android.synthetic.main.view_chip_group_type.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class BookmarkFragment : Fragment() {
    private val mainViewModel by sharedViewModel<MainViewModel> ()
    internal val viewModel by sharedViewModel<BookmarkViewModel>()
    lateinit var adapter: BookmarkAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bookmark, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Bookmark List
        adapter = BookmarkAdapter(listOf()).also {
            recycler_bookmark.apply {adapter = it}.addOnScrollListener(object: RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).also {imm->
                        imm.hideSoftInputFromWindow(view.windowToken, 0)
                    }
                }
            })
        }
        viewModel.getBookmarks().observe(this, Observer {
            adapter.bookmarks = it
            adapter.items = it.toMutableList()
            adapter.notifyDataSetChanged()
        })

        // Add Category Chips on Filter
        viewModel.getCategories().let {categories ->
            categories.map { category ->
                val categoryChip = Chip(context)
                categoryChip.text = category
                categoryChip.setTextColor(Color.BLACK)
                categoryChip.isCheckable = true
                categoryChip.isChipIconVisible = false
                categoryChip.isCloseIconVisible = false
                categoryChip.checkedIcon = ContextCompat.getDrawable(view.context, R.drawable.ic_check_circle_black_24px)
                chip_group_bookmark_filter_category.addView(categoryChip)
            }
        }

        // Expand & Collapse Animation on Filter
        mainViewModel.isFilterOpen.value = false
        mainViewModel.isFilterOpen.observe(this, Observer {
            if (it) container_bookmark_filter.expand()
            else container_bookmark_filter.collapse()
            (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).also {imm->
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        })

        // Add Filter Action on Search EditText
        et_bookmark_search.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                val categoryFilter = getFilters(chip_group_bookmark_filter_category)
                val typeFilter = getFilters(chip_group_filter_article)
                adapter.search(s.toString(), categoryFilter, typeFilter)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int){}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Add Filter Action on Category Chips
        for (chip in chip_group_bookmark_filter_category.children) {
            chip.setOnClickListener {
                val categoryFilter = getFilters(chip_group_bookmark_filter_category)
                val typeFilter = getFilters(chip_group_filter_article)
                adapter.search(et_bookmark_search.text.toString(), categoryFilter, typeFilter)
            }
        }

        // Add Filter Action on Type Chips
        for (chip in chip_group_filter_article.children){
            chip.setOnClickListener {
                val categoryFilter = getFilters(chip_group_bookmark_filter_category)
                val typeFilter = getFilters(chip_group_filter_article)
                adapter.search(et_bookmark_search.text.toString(), categoryFilter, typeFilter)
            }
        }

        // Swipe Function
        val leftSwipeCallback = BookmarkItemTouchHelper(0, ItemTouchHelper.LEFT,
            object : BookmarkItemTouchHelper.BookmarkItemTouchHelperListener{
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int) {
                    adapter.items.removeAt(position)
                    adapter.notifyItemRemoved(position)
                    val removeItem = (viewHolder as BookmarkAdapter.BookmarkViewHolder).itemData
                    val removeItemTitle = viewHolder.itemView.tv_bookmark_title.text
                    Snackbar.make(layout_fragment_bookmark, "$removeItemTitle is Deleted", Snackbar.LENGTH_LONG).also {
                        it.setAction("UNDO") {
                            viewModel.restoreBookmark(removeItem)
                            adapter.items.add(position, removeItem)
                            adapter.notifyItemInserted(position)
                        }
                    }.show()
                    viewModel.removeBookmark(removeItem.fb_id)
                }
            })
        ItemTouchHelper(leftSwipeCallback).attachToRecyclerView(recycler_bookmark)
    }

}