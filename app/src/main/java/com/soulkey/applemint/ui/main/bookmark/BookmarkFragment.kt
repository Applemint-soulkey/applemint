package com.soulkey.applemint.ui.main.bookmark

import android.content.Context
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
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.soulkey.applemint.R
import com.soulkey.applemint.config.getFilters
import com.soulkey.applemint.config.typeTagMapper
import com.soulkey.applemint.model.Bookmark
import com.soulkey.applemint.ui.main.MainViewModel
import kotlinx.android.synthetic.main.fragment_bookmark.*
import kotlinx.android.synthetic.main.item_bookmark_foreground.view.*
import kotlinx.android.synthetic.main.view_chip_group_type.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class BookmarkFragment : Fragment() {
    private val mainViewModel by sharedViewModel<MainViewModel> ()
    private val viewModel by sharedViewModel<BookmarkViewModel>()
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
        adapter = BookmarkAdapter(listOf()).also {recycler_bookmark.adapter = it}
        viewModel.getBookmarks().observe(this, Observer {
            adapter.bookmarks = it
            adapter.items = it.toMutableList()
            adapter.notifyDataSetChanged()
        })

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
        mainViewModel.isFilterOpen.value = false
        mainViewModel.isFilterOpen.observe(this, Observer {
            if (it) container_bookmark_filter.expand()
            else container_bookmark_filter.collapse()
            (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).also {imm->
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        })

        et_bookmark_search.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                adapter.search(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int){}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        for (chip in chip_group_bookmark_filter_category.children) {
            chip.setOnClickListener {
                adapter.search(et_bookmark_search.text.toString())
            }
        }

        for (chip in chip_group_filter_article.children){
            chip.setOnClickListener {
                adapter.search(et_bookmark_search.text.toString())
            }
        }
    }

    inner class BookmarkAdapter(var bookmarks: List<Bookmark>): RecyclerView.Adapter<BookmarkAdapter.BookmarkViewHolder>() {
        var items = bookmarks.toMutableList()
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {
            return BookmarkViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_bookmark, parent, false))
        }
        override fun getItemCount(): Int = items.size
        override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) = holder.bind(items[position])

        fun search(keyword: String) {
            val categoryFilter = getFilters(chip_group_bookmark_filter_category)
            val typeFilter = getFilters(chip_group_filter_article)
            items = bookmarks
                .filter {it.content.contains(keyword) || keyword.isEmpty() }
                .filter {categoryFilter.contains(it.category) || categoryFilter.isEmpty() }
                .filter {typeFilter.contains(it.type) || typeFilter.isEmpty() }
                .toMutableList()
            notifyDataSetChanged()
        }

        inner class BookmarkViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
            lateinit var itemData: Bookmark
            fun bind(data: Bookmark) {
                itemData = data
                itemView.tv_bookmark_type.text = itemData.type
                itemView.tv_bookmark_type.setBackgroundResource(typeTagMapper(itemData.type))
                itemView.tv_bookmark_category.text = itemData.category
                itemView.tv_bookmark_title.text = if (itemData.content.isNotEmpty()) itemData.content else itemData.url
                itemView.tv_bookmark_url.text = itemData.url
            }
        }
    }
}