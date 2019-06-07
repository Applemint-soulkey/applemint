package com.soulkey.applemint.utils

import android.util.Log
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.soulkey.applemint.model.article.Article
import com.soulkey.applemint.model.network.RetrofitClient
import com.soulkey.applemint.viewmodel.ArticleViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArticleUtil {
    private fun convertToList(jsonArray: JsonArray): ArrayList<String>{
        val listData = ArrayList<String>()
        for (i in 0 until jsonArray.size()) {
            listData.add(jsonArray.get(i).toString())
        }
        return listData
    }

    fun convertArticle(data: JsonObject): Article {
        val fireBaseId = data.get("id").asString
        val tags = convertToList(data.get("data").asJsonObject.get("tag").asJsonArray)
        val isBookmarked = data.get("data").asJsonObject.get("isBookmark").asBoolean
        val linkData = data.get("data").asJsonObject.get("article_link")
        val url = linkData.asJsonObject.get("url").asString
        val domain = linkData.asJsonObject.get("domain").asString
        val articleData = data.get("data").asJsonObject.get("article_content").asJsonObject
        val title = articleData.get("title").asString
        val text = articleData.get("content_text").asString
        val contentUrls = convertToList(articleData.get("content_urls").asJsonArray)
        val externalUrls = convertToList(articleData.get("external_urls").asJsonArray)
        return Article(
            null,
            fireBaseId,
            title,
            text,
            url,
            domain,
            isBookmarked,
            contentUrls,
            externalUrls,
            tags
        )
    }

    fun getAllData(articleViewModel: ArticleViewModel) {
        val result : ArrayList<String> = ArrayList()
        val call = RetrofitClient.getInstance().buildRetrofit().getAll()
        call.enqueue(object : Callback<JsonArray> {
            override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                Log.d("retrofit", t.message)
            }
            override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
                articleViewModel.clear()
                val iterData = response.body()
                iterData?.forEach {
                    val article = convertArticle(it.asJsonObject)
                    articleViewModel.insert(article)
                }
                Log.d("retrofit", result.toString())
            }
        })
    }
}
