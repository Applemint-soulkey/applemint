package com.soulkey.applemint.ui.main.article

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItems
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.soulkey.applemint.R
import com.soulkey.applemint.config.typeTagMapper
import com.soulkey.applemint.model.Article
import com.soulkey.applemint.ui.analyze.AnalyzeActivity
import com.soulkey.applemint.ui.viewer.ViewerActivity
import kotlinx.android.synthetic.main.item_article_background.view.*
import kotlinx.android.synthetic.main.item_article_foreground.view.*
import timber.log.Timber

class ArticleAdapter: ListAdapter<Article, ArticleAdapter.ArticleViewHolder>(object :
    DiffUtil.ItemCallback<Article>(){
    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
        return (oldItem.fb_id == newItem.fb_id)
    }

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem == newItem
    }
}) {
    val analyzableType = listOf("battlepage", "dogdrip", "imgur", "youtube", "twitch", "direct")
    lateinit var availableAction: List<String>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_article, parent, false))
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ArticleViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        lateinit var itemData: Article
        fun bind(data: Article) {
            itemData = data
            itemView.tv_article_title.text = if (data.content.isNotEmpty()) data.content else data.url
            itemView.tv_article_url.text = data.url
            itemView.tv_article_type.text = data.type
            itemView.tv_article_type.setBackgroundResource(typeTagMapper(data.type))

            itemView.setOnClickListener {
                Handler().postDelayed( {
                    val intent: Intent = if (itemData.type == "youtube" || itemData.type == "twitch"){
                        Intent(Intent.ACTION_VIEW).setData(Uri.parse(itemData.url))
                    }else{
                        Intent(itemView.context, ViewerActivity::class.java).apply { putExtra("url", itemData.url) }
                    }
                    ContextCompat.startActivity(itemView.context, intent, null)
                }, 300)
                //ContextCompat.startActivity(itemView.context, Intent(Intent.ACTION_VIEW, Uri.parse(data.url)), null)
            }

            itemView.setOnLongClickListener {view->
                MaterialDialog(view.context).show {
                    title(text="Select an Article Action")
                    cornerRadius(16f)

                    availableAction = if (itemData.type in analyzableType){
                        listOf("Analyze", "Share", "Copy URL")
                    } else {
                        listOf("Share", "Copy URL")
                    }

                    listItems(items = availableAction) {_, _, text ->
                        Timber.v("diver:/ Select $text")
                        when(text) {
                            "Analyze"-> {
                                Intent(view.context, AnalyzeActivity::class.java).also {
                                    it.putExtra("id", itemData.fb_id)
                                    ContextCompat.startActivity(view.context, it, null)
                                }
                            }
                            "Share"-> {
                                Intent().apply {
                                    action= Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, itemData.url)
                                    type = "text/plain"
                                }.also {
                                    ContextCompat.startActivity(view.context, Intent.createChooser(it, null), null)
                                }
                            }
                            "Copy URL"-> {
                                (view.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).also {
                                    it.primaryClip = ClipData.newPlainText("auto_copy_text", itemData.url)
                                    Toast.makeText(view.context, "URL is copied to Clipboard!", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }
                true
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
        backgroundView.isClickable = false
        //backgroundView.isEnabled = false
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