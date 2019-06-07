package com.soulkey.applemint.model.article

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import java.lang.Exception

class ArticleRepository (application: Application){
    private val articleDatabase = ArticleDatabase.getInstance(application)
    private val articleDao: ArticleDAO = articleDatabase!!.articleDao()
    private val articles: LiveData<List<Article>> = articleDao.getAll()

    fun deleteById(fbId: String){
        try {
            Thread(Runnable {
                articleDao.deleteById(fbId)
            }).start()
        }catch (e:Exception){
            Log.d("sulfur", e.message)
        }
    }

    fun getAll(): LiveData<List<Article>> {
        return articles
    }

    fun insert(article: Article){
        try {
            Thread(Runnable {
                articleDao.insert(article)
            }).start()
        } catch (e: Exception) {
            Log.d("sulfur", e.message)
        }
    }

    fun delete(article: Article){
        try {
            Thread(Runnable {
                articleDao.delete(article)
            }).start()
        }catch (e: Exception){
            Log.d("sulfur", e.message)
        }
    }

    fun updateBookmark(firebase_id: String, bmkFlag:Boolean){
        try {
            Thread(Runnable {
                articleDao.setBookmarkState(firebase_id, bmkFlag)
            }).start()
        }catch (e: Exception){
            Log.d("sulfur", e.message)
        }
    }

    fun clear(){
        try{
            Thread(Runnable {
                articleDao.clear()
            }).start()
        }catch (e: Exception){
            Log.d("sulfur", e.message)
        }
    }
}