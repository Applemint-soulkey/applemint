package com.soulkey.applemint.ui.main.article

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.setActionButtonEnabled
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.google.android.material.radiobutton.MaterialRadioButton
import com.soulkey.applemint.R
import com.soulkey.applemint.config.typeTagMapper
import com.soulkey.applemint.model.Article
import com.soulkey.applemint.ui.main.MainViewModel
import kotlinx.android.synthetic.main.item_article_background.view.*
import kotlinx.android.synthetic.main.item_article_foreground.view.*
import kotlinx.android.synthetic.main.view_bottomsheet_bookmark.view.*

class ArticleAdapter(list: List<Article>, val viewModel: MainViewModel) : RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {
    var articles = list
    var filters = listOf<String>()

    lateinit var items: MutableList<Article>
    fun filterItems() {
        items = if (filters.isNotEmpty()) articles.filter { filters.contains(it.type) }.toMutableList() else articles.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ArticleViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_article, parent, false))
    override fun getItemCount() = items.size
    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ArticleViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        lateinit var itemData: Article
        fun bind(data: Article) {
            itemData = data
            itemView.tv_article_title.text = if (data.content.isNotEmpty()) data.content else data.url
            itemView.tv_article_url.text = data.url
            itemView.tv_article_type.text = data.type
            itemView.tv_article_type.setBackgroundResource(typeTagMapper(data.type))

            itemView.container_card_article_detail.setExpanded(false, false)
            itemView.btn_expand_card.rotation = 0f
            itemView.btn_expand_card.setOnClickListener {
                if (itemView.container_card_article_detail.isExpanded) {
                    itemView.btn_expand_card.animate().rotation(0f).start()
                    itemView.btn_expand_card.rotation = 0f
                    itemView.container_card_article_detail.isExpanded = false
                } else  {
                    itemView.btn_expand_card.animate().rotation(180f).start()
                    itemView.btn_expand_card.rotation = 180f
                    itemView.container_card_article_detail.isExpanded = true
                }
            }

            itemView.btn_card_article_bookmark.setOnClickListener {
                openBookmarkDialog(it.context, adapterPosition)
            }
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, ViewerActivity::class.java).apply { putExtra("url", itemData.url) }
                ContextCompat.startActivity(itemView.context, intent, null)
                //ContextCompat.startActivity(itemView.context, Intent(Intent.ACTION_VIEW, Uri.parse(data.url)), null)
            }
        }

        private fun openBookmarkDialog(context: Context, position: Int) {
            var selectedCategory = ""
            val dialog = MaterialDialog(context, BottomSheet()).customView(R.layout.view_bottomsheet_bookmark, scrollable = true)
            val view = dialog.getCustomView()
            dialog.setActionButtonEnabled(WhichButton.POSITIVE, false)
            viewModel.getCategories().map {
                val categoryButton = MaterialRadioButton(context)
                categoryButton.text = it
                categoryButton.typeface = context.resources.getFont(R.font.opensans_bold)
                view.group_bookmark.addView(categoryButton)
            }
            view.group_bookmark.setOnCheckedChangeListener { group, checkedId ->
                view.btn_add_bookmark_category.visibility = View.VISIBLE
                view.et_add_bookmark_category.visibility = View.INVISIBLE
                view.et_add_bookmark_category.text?.clear()
                if (checkedId == -1) {
                    selectedCategory = ""
                    dialog.setActionButtonEnabled(WhichButton.POSITIVE, false)
                } else {
                    selectedCategory = group.findViewById<RadioButton>(checkedId).text.toString()
                    dialog.setActionButtonEnabled(WhichButton.POSITIVE, true)
                }
            }
            view.btn_add_bookmark_category.setOnClickListener {
                view.group_bookmark.clearCheck()
                view.btn_add_bookmark_category.visibility = View.INVISIBLE
                view.et_add_bookmark_category.visibility = View.VISIBLE
            }
            view.et_add_bookmark_category.addTextChangedListener(object : TextWatcher{
                override fun afterTextChanged(s: Editable?) {
                    selectedCategory = s.toString()
                    dialog.setActionButtonEnabled(WhichButton.POSITIVE, selectedCategory.isNotEmpty())
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })

            dialog.show {
                cornerRadius(16f)
                positiveButton(text = "Save") {
                    viewModel.bookmarkArticle(selectedCategory, itemData)
                    items.removeAt(position)
                    notifyItemRemoved(position)
                }
            }
        }
    }
}

class ArticleItemTouchHelper(dragDirs: Int, swipeDirs: Int, private val listener: ArticleItemTouchHelperListener) : ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        listener.onSwiped(viewHolder, direction, viewHolder.adapterPosition)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        viewHolder?.let {
            val foregroundView = (it as ArticleAdapter.ArticleViewHolder).itemView.container_card_article_foreground
            ItemTouchHelper.Callback.getDefaultUIUtil().onSelected(foregroundView)
        }
    }

    override fun onChildDrawOver(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder?,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        viewHolder?.let {
            val foregroundView = viewHolder.itemView.container_card_article_foreground
            ItemTouchHelper.Callback.getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive)
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val foregroundView = viewHolder.itemView.container_card_article_foreground
        val backgroundView = viewHolder.itemView.container_card_article_background
        if (dX > 0) {
            //Right Swipe
            backgroundView.setBackgroundColor(Color.parseColor("#1d1d1d"))
            viewHolder.itemView.iv_card_article_save.visibility = View.VISIBLE
            viewHolder.itemView.tv_card_article_save.visibility = View.VISIBLE
            viewHolder.itemView.iv_card_article_delete.visibility = View.INVISIBLE
            viewHolder.itemView.tv_card_article_delete.visibility = View.INVISIBLE
        }
        else if (dX < 0){
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

    interface ArticleItemTouchHelperListener {
        fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int)
    }
}