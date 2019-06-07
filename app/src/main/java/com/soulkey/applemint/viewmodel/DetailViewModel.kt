package com.soulkey.applemint.viewmodel

import android.app.Application
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.soulkey.applemint.DetailActivity.ExternalLink
import com.soulkey.applemint.model.article.ArticleRepository
import com.soulkey.applemint.model.network.RetrofitClient
import org.jsoup.Jsoup
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class DetailViewModel(application: Application) : AndroidViewModel(application){
    private val repository = ArticleRepository(application)

    fun deleteById(fbId:String){
        repository.deleteById(fbId)
    }

    fun toggleBookmark(id: String, value:Boolean, view: View){
        val call = RetrofitClient.getInstance().buildRetrofit().bookmark(id, value)
        call.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.d("sulfur", t.message)
                Toast.makeText(getApplication(), "bookmark fail.. Check peppermint", Toast.LENGTH_SHORT).show()
            }
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                repository.updateBookmark(id, value)
                view.isActivated = value
                Toast.makeText(getApplication(), "Bookmark Success", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun getLinkData(urlList: ArrayList<String>): LiveData<ArrayList<ExternalLink>>? {
        var result: MutableLiveData<ArrayList<ExternalLink>> = MutableLiveData()
        val data: ArrayList<ExternalLink> = arrayListOf()

        try {
            Thread(Runnable {
                try{
                    for(urlText in urlList){
                        val doc = Jsoup.connect(urlText.replace("\"","")).get()
                        val title = doc.title()
                        val linkData = ExternalLink(title, urlText.replace("\"",""))
                        data.add(linkData)
                    }
                }catch (e: Exception){
                    Log.d("sulfur", e.message)
                }
                result.postValue(data)
            }).start()
        }catch (e: Exception){
            Log.d("sulfur", e.message)
        }
        return result
    }
}