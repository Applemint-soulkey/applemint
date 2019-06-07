package com.soulkey.applemint

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setPadding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.JsonObject
import com.soulkey.applemint.adapter.ContentAdapter
import com.soulkey.applemint.adapter.LinkAdapter
import com.soulkey.applemint.model.network.RetrofitClient
import com.soulkey.applemint.viewmodel.DetailViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {

    data class ExternalLink(val title: String, val url: String)
    private lateinit var detailViewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        init(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            R.id.item_upload -> {
                //TODO: Modify Database
                //TODO: Call dropbox api to save content_url
                //TODO: Save check data to Server
                val articleFbId = intent.getStringExtra("fb_id")
                detailViewModel.requestDapina(articleFbId)
            }
            R.id.item_browser -> {
                val originUrl = findViewById<TextView>(R.id.tv_detail_url).text
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(originUrl.toString())))
            }
        }
        return true
    }

    private fun init(intent: Intent){
        val textViewTitle = findViewById<TextView>(R.id.tv_detail_title)
        val textViewText = findViewById<TextView>(R.id.tv_detail_text)
        val textViewUrl = findViewById<TextView>(R.id.tv_detail_url)
        val textViewContentHeader = findViewById<TextView>(R.id.tv_content_header)
        val textViewLinkHeader = findViewById<TextView>(R.id.tv_link_header)
        val recyclerContents = findViewById<RecyclerView>(R.id.rv_contents)
        val recyclerLinks = findViewById<RecyclerView>(R.id.rv_links)
        val fabCheck = findViewById<FloatingActionButton>(R.id.fab_check)
        val appBarBottom = findViewById<BottomAppBar>(R.id.appbar_bottom)
        val pbrLink = findViewById<ProgressBar>(R.id.progress_circular_link)
        val buttonBookmark = findViewById<ImageButton>(R.id.iv_toggle_bookmark)

        val articleTitle = intent.getStringExtra("title")
        val articleText = intent.getStringExtra("text")
        val articleUrl = intent.getStringExtra("url")
        val articleContents = intent.getStringArrayListExtra("content_urls")
        val articleLinks = intent.getStringArrayListExtra("external_urls")
        val articleFbId = intent.getStringExtra("fb_id")

        //Bookmark Button
        when(intent.getBooleanExtra("bookmark", false)){
            true -> buttonBookmark.isActivated = true
            false -> buttonBookmark.isActivated = false
        }
        buttonBookmark.setOnClickListener {
            val flag = buttonBookmark.isActivated
            detailViewModel.toggleBookmark(articleFbId, !flag, buttonBookmark)
        }

        //FloatingActionButton
        fabCheck.setOnClickListener {
            val call = RetrofitClient.getInstance().buildRetrofit().visit(articleFbId)
            call.enqueue(object : Callback<JsonObject>{
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.d("sulfur", t.message)
                    Toast.makeText(this@DetailActivity, "Visit fail.. Please Check peppermint", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    detailViewModel.deleteById(articleFbId)
                    Log.d("sulfur", response.body().toString())
                }
            })
            this.onBackPressed()
        }

        //BottomAppBar
        this.setSupportActionBar(appBarBottom)
        appBarBottom.setNavigationOnClickListener{
            this.onBackPressed()
        }

        //Content RecyclerView
        val contentAdapter = ContentAdapter(applicationContext)
        contentAdapter.setContentUrls(articleContents)
        recyclerContents.layoutManager = LinearLayoutManager(this)
        recyclerContents.adapter = contentAdapter
        recyclerContents.setHasFixedSize(true)

        if(articleContents.size > 0 || articleText.isNotEmpty()){
            textViewContentHeader.visibility = View.VISIBLE
            recyclerContents.visibility = View.VISIBLE
            textViewText.visibility = View.VISIBLE
        }
        else {
            textViewText.textSize = 0F
            textViewText.setPadding(0)
            textViewContentHeader.textSize = 0F
        }

        //External Link RecyclerView
        val linkAdapter = LinkAdapter{
            if(it.url.contains("youtube")){
                startActivity(Intent(Intent.ACTION_VIEW).setData(Uri.parse(it.url)).setPackage("com.google.android.youtube"))
            }else{
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it.url)))
            }
        }
        linkAdapter.setLinkData(arrayListOf())
        recyclerLinks.layoutManager = LinearLayoutManager(this)
        recyclerLinks.adapter = linkAdapter
        recyclerLinks.setHasFixedSize(true)

        textViewTitle.text = articleTitle
        textViewText.text = textSegment(articleText)
        textViewUrl.text = articleUrl

        if(articleLinks.size > 0){
            textViewLinkHeader.visibility = View.VISIBLE
            pbrLink.visibility = View.VISIBLE
        }

        //ViewModel Apply
        detailViewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)
        detailViewModel.getLinkData(articleLinks)?.observe(this, Observer {
            if(it.size > 0){
                pbrLink.visibility = View.INVISIBLE
                linkAdapter.setLinkData(it!!)
            }else{
                textViewLinkHeader.visibility = View.INVISIBLE
                pbrLink.visibility = View.INVISIBLE
            }
        })
    }

    private fun textSegment(text: String): String{
        var segmented = ""
        for(lineText in text.split("\n")){
            if(lineText.trim().isNotEmpty()) segmented += lineText.trim()+"\n"
        }
        val regex = """\n$""".toRegex()
        return regex.replace(segmented, "")
    }

    private fun listToText(list: ArrayList<String>){
        for(item in list){
            Log.d("sulfur", item)
        }
    }
}