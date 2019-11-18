package com.soulkey.applemint.ui.main.bookmark

import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.setActionButtonEnabled
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.afollestad.materialdialogs.list.listItems
import com.soulkey.applemint.R
import com.soulkey.applemint.config.typeTagMapper
import com.soulkey.applemint.model.Bookmark
import com.soulkey.applemint.ui.viewer.ViewerActivity
import kotlinx.android.synthetic.main.item_article_background.view.*
import kotlinx.android.synthetic.main.item_bookmark_foreground.view.*
import kotlinx.android.synthetic.main.view_dialog_edit_bookmark_form.view.*

class BookmarkAdapter(val viewModel: BookmarkViewModel) : ListAdapter<Bookmark, BookmarkAdapter.BookmarkViewHolder>(object:
    DiffUtil.ItemCallback<Bookmark>() {
    override fun areItemsTheSame(oldItem: Bookmark, newItem: Bookmark): Boolean {
        return (oldItem.fb_id == newItem.fb_id)
    }

    override fun areContentsTheSame(oldItem: Bookmark, newItem: Bookmark): Boolean {
        return oldItem == newItem
    }
}) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {
        return BookmarkViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_bookmark, parent, false))
    }

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        holder.bind(getItem(position))
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
                Handler().postDelayed( {
                    val intent = Intent(itemView.context, ViewerActivity::class.java).apply { putExtra("url", itemData.url) }
                    ContextCompat.startActivity(itemView.context, intent, null)
                }, 300)
            }
            itemView.setOnLongClickListener {
                MaterialDialog(itemView.context).show {
                    title(text = "Select a Function")
                    listItems(items = listOf("Edit Bookmark Data", "Share this Article")) { _, index, _ ->
                        when(index) {
                            0-> editBookmark(itemData)
                            1-> shareBookmark(itemData.url)
                        }
                    }
                    cornerRadius(16f)
                }
                true
            }
        }

        private fun editBookmark(item: Bookmark){
            val dialog = MaterialDialog(itemView.context).customView(R.layout.view_dialog_edit_bookmark_form)
            val view = dialog.getCustomView()
            view.et_edit_bookmark_content.setText(item.content)
            view.et_edit_bookmark_url.setText(item.url)
            view.et_edit_bookmark_type.setText(item.type)
            view.et_edit_bookmark_category.setText(item.category)
            dialog.setActionButtonEnabled(WhichButton.POSITIVE, true)
            dialog.show {
                cornerRadius(16f)
                positiveButton(text = "UPDATE") {
                    item.content = view.et_edit_bookmark_content.text.toString()
                    item.category = view.et_edit_bookmark_category.text.toString()
                    viewModel.updateBookmark(item)
                    notifyItemChanged(adapterPosition)
                }
            }
        }

        private fun shareBookmark(url: String){
            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, "Share this URL")
                putExtra(Intent.EXTRA_TEXT, url)
            }.also {
                ContextCompat.startActivity(itemView.context, Intent.createChooser(it, "Share with.."), null)
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
