package com.soulkey.applemint.ui.main.bookmark

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
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
import kotlinx.android.synthetic.main.fragment_articles.*
import kotlinx.android.synthetic.main.fragment_bookmark.*
import kotlinx.android.synthetic.main.item_bookmark_foreground.view.*
import kotlinx.android.synthetic.main.view_chip_group_type.*
import kotlinx.android.synthetic.main.view_empty.*
import kotlinx.android.synthetic.main.view_loading.*
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

        layout_view_empty.visibility = View.INVISIBLE
        layout_view_loading.visibility = View.INVISIBLE
        viewModel.categoryFilter.value = listOf()
        viewModel.typeFilter.value = listOf()

        viewModel.isBookmarkUpdated.observe(this, Observer {
            layout_view_loading.visibility = if (it) View.INVISIBLE else View.VISIBLE
        })

        adapter = BookmarkAdapter(viewModel)
        viewModel.filterBookmarks.observe(this, Observer {
            layout_view_empty.visibility = if (it.isEmpty()) View.VISIBLE else View.INVISIBLE
            adapter.submitList(it)
        })
        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver(){
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (itemCount > 1) recycler_bookmark.scrollToPosition(0)
            }
        })

        recycler_bookmark.adapter = adapter
        recycler_bookmark.setOnTouchListener { _, _ ->
            mainViewModel.isFilterOpen.value = false
            (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).also {imm->
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
            false
        }

        viewModel.isFilterApply.observe(this, Observer {
            mainViewModel.isFilterApply.value = it
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

        iv_clear_bookmark_search.setOnClickListener {
            et_bookmark_search.text?.clear()
            et_bookmark_search.clearFocus()
            (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).also {imm->
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }

        // Add Filter Action on Search EditText
        et_bookmark_search.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    iv_clear_bookmark_search.visibility = if (s.isEmpty()) View.INVISIBLE else View.VISIBLE
                    viewModel.searchKeyword.value = s.toString()
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int){}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Add Filter Action on Category Chips
        for (chip in chip_group_bookmark_filter_category.children) {
            chip.setOnClickListener {
                viewModel.categoryFilter.value = getFilters(chip_group_bookmark_filter_category)
            }
        }

        // Add Filter Action on Type Chips
        for (chip in chip_group_filter_article.children){
            chip.setOnClickListener {
                viewModel.typeFilter.value = getFilters(chip_group_filter_article)
            }
        }

        // Swipe Function
        val leftSwipeCallback = BookmarkItemTouchHelper(0, ItemTouchHelper.LEFT,
            object : BookmarkItemTouchHelper.BookmarkItemTouchHelperListener{
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int) {
                    val removeItem = (viewHolder as BookmarkAdapter.BookmarkViewHolder).itemData
                    val removeItemTitle = viewHolder.itemView.tv_bookmark_title.text
                    Snackbar.make(layout_fragment_bookmark, "$removeItemTitle is Deleted", Snackbar.LENGTH_LONG).apply {
                        setAction("UNDO") { viewModel.restoreBookmark(removeItem) }
                    }.show()
                    viewModel.removeBookmark(removeItem.fb_id)
                }
            })
        ItemTouchHelper(leftSwipeCallback).attachToRecyclerView(recycler_bookmark)
    }
}