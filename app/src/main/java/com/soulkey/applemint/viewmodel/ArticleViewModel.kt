package com.soulkey.applemint.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.google.gson.JsonObject
import com.soulkey.applemint.model.article.Article
import com.soulkey.applemint.model.article.ArticleRepository
import com.soulkey.applemint.model.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArticleViewModel(application: Application) : AndroidViewModel(application){
    private val repository = ArticleRepository(application)
    private val articles = repository.getAll()

    fun getAll(): LiveData<List<Article>> {
        return this.articles
    }

    fun insert(article: Article){
        repository.insert(article)
    }

    fun delete(article: Article){
        repository.delete(article)
    }

    fun toggleBookmark(fbId: String, bmkFlag:Boolean){
        val call = RetrofitClient.getInstance().buildRetrofit().bookmark(fbId, bmkFlag)
        call.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.d("sulfur", t.message)
                Toast.makeText(getApplication(), "bookmark fail.. Check peppermint", Toast.LENGTH_SHORT).show()
            }
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                repository.updateBookmark(fbId, bmkFlag)
                Toast.makeText(getApplication(), "Bookmark Success", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun clear(){
        repository.clear()
    }
}