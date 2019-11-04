package com.soulkey.applemint.ui.main.article

import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.soulkey.applemint.R
import com.soulkey.applemint.model.Article
import kotlinx.android.synthetic.main.fragment_articles.view.*
import kotlinx.android.synthetic.main.item_article.view.*

class ArticleAdapter(list: List<Article>) : RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {
    var articles = list
    var filters = listOf<String>()
    lateinit var items: MutableList<Article>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_article, parent, false))
    }

    fun filterItems() {
        items = if (filters.isNotEmpty()) articles.filter { filters.contains(it.type) }.toMutableList() else articles.toMutableList()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int)  {
        holder.bind(items[position])
    }

    inner class ArticleViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        lateinit var itemData: Article
        fun bind(data: Article) {
            itemData = data
            if (data.content.isNotEmpty()) itemView.tv_article_title.text = data.content
            else itemView.tv_article_title.text = data.url
            itemView.tv_article_desc.text = data.url
            itemView.tv_article_type.text = data.type
            when (data.type) {
                "youtube"-> itemView.tv_article_type.setBackgroundResource(R.drawable.backgroud_article_youtube_tag)
                "twitch"-> itemView.tv_article_type.setBackgroundResource(R.drawable.backgroud_article_twtich_tag)
                "battlepage"-> itemView.tv_article_type.setBackgroundResource(R.drawable.backgroud_article_battlepage_tag)
                "dogdrip"-> itemView.tv_article_type.setBackgroundResource(R.drawable.backgroud_article_dogdrip_tag)
                "fmkorea"-> itemView.tv_article_type.setBackgroundResource(R.drawable.backgroud_article_fmkorea_tag)
                "direct"-> itemView.tv_article_type.setBackgroundResource(R.drawable.backgroud_article_direct_tag)
                "imgur"-> itemView.tv_article_type.setBackgroundResource(R.drawable.backgroud_article_imgur_tag)
                else-> itemView.tv_article_type.setBackgroundResource(R.drawable.backgroud_article_etc_tag)
            }
            itemView.setOnClickListener {
                ContextCompat.startActivity(
                    itemView.context,
                    Intent(Intent.ACTION_VIEW, Uri.parse(data.url)),
                    null
                )
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

fun getCheckedFilter(view: View?): List<String>{
    val filterList = mutableListOf<String>()
    view?.let {
        if (it.chip_filter_battlepage.isChecked) filterList.add("battlepage")
        if (it.chip_filter_dogdrip.isChecked) filterList.add("dogdrip")
        if (it.chip_filter_fmkorea.isChecked) filterList.add("fmkorea")
        if (it.chip_filter_direct.isChecked) filterList.add("direct")
        if (it.chip_filter_etc.isChecked) filterList.add("etc")
        if (it.chip_filter_imgur.isChecked) filterList.add("imgur")
        if (it.chip_filter_twtich.isChecked) filterList.add("twitch")
        if (it.chip_filter_youtube.isChecked) filterList.add("youtube")
    }
    return filterList
}
