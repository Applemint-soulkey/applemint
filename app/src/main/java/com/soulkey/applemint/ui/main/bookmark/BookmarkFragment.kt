package com.soulkey.applemint.ui.main.bookmark

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.soulkey.applemint.R
import com.soulkey.applemint.config.typeTagMapper
import com.soulkey.applemint.model.Bookmark
import com.soulkey.applemint.ui.main.MainViewModel
import kotlinx.android.synthetic.main.fragment_bookmark.*
import kotlinx.android.synthetic.main.item_bookmark_foreground.view.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class BookmarkFragment : Fragment() {
    internal val mainViewModel by sharedViewModel<MainViewModel> ()
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


        recycler_bookmark.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                container_bookmark_filter.collapse()
            }
        })
        adapter = BookmarkAdapter(listOf()).also {recycler_bookmark.adapter = it}
        viewModel.getBookmarks().observe(this, Observer {
            adapter.bookmarks = it
            adapter.notifyDataSetChanged()
        })

    }

    inner class BookmarkAdapter(var bookmarks: List<Bookmark>): RecyclerView.Adapter<BookmarkAdapter.BookmarkViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {
            return BookmarkViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_bookmark, parent, false))
        }

        override fun getItemCount(): Int = bookmarks.size
        override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) = holder.bind(bookmarks[position])

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