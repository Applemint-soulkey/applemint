package com.soulkey.applemint.ui.main.bookmark

import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.soulkey.applemint.R
import com.soulkey.applemint.config.typeTagMapper
import com.soulkey.applemint.model.Bookmark
import com.soulkey.applemint.ui.main.article.ArticleAdapter
import com.soulkey.applemint.ui.main.article.ArticleItemTouchHelper
import com.soulkey.applemint.ui.viewer.ViewerActivity
import kotlinx.android.synthetic.main.item_article_background.view.*
import kotlinx.android.synthetic.main.item_article_foreground.view.*
import kotlinx.android.synthetic.main.item_bookmark_foreground.view.*
import kotlinx.android.synthetic.main.item_bookmark_foreground.view.container_card_article_foreground

class BookmarkAdapter(var bookmarks: List<Bookmark>): RecyclerView.Adapter<BookmarkAdapter.BookmarkViewHolder>() {
    var items = bookmarks.toMutableList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {
        return BookmarkViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_bookmark, parent, false))
    }
    override fun getItemCount(): Int = items.size
    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) = holder.bind(items[position])

    fun search(keyword: String, categoryFilter: List<String>, typeFilter: List<String>) {
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

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, ViewerActivity::class.java).apply { putExtra("url", itemData.url) }
                ContextCompat.startActivity(itemView.context, intent, null)
            }
        }
    }
}

class BookmarkItemTouchHelper(dragDirs: Int, swipeDirs: Int, val listener: BookmarkItemTouchHelperListener) : ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {
    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return true
    }
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        listener.onSwiped(viewHolder, direction, viewHolder.adapterPosition)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        viewHolder?.let {
            val foregroundView = (it as BookmarkAdapter.BookmarkViewHolder).itemView.container_card_article_foreground
            ItemTouchHelper.Callback.getDefaultUIUtil().onSelected(foregroundView)
        }
    }

    override fun onChildDrawOver(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder?, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        viewHolder?.let {
            val foregroundView = viewHolder.itemView.container_card_article_foreground
            ItemTouchHelper.Callback.getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive)
        }
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        val foregroundView = viewHolder.itemView.container_card_article_foreground
        val backgroundView = viewHolder.itemView.container_card_article_background
        if (dX < 0){
            //Left Swipe
            backgroundView.setBackgroundColor(Color.parseColor("#C4302b"))
            viewHolder.itemView.iv_card_article_delete.visibility = View.VISIBLE
            viewHolder.itemView.tv_card_article_delete.visibility = View.VISIBLE
            viewHolder.itemView.iv_card_article_save.visibility = View.INVISIBLE
            viewHolder.itemView.tv_card_article_save.visibility = View.INVISIBLE
        }
        ItemTouchHelper.Callback.getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        val foregroundView = viewHolder.itemView.container_card_article_foreground
        ItemTouchHelper.Callback.getDefaultUIUtil().clearView(foregroundView)
    }

    interface BookmarkItemTouchHelperListener {
        fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int)
    }
}
