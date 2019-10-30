package com.soulkey.applemint.ui.main

import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.soulkey.applemint.data.ArticleRepository
import com.soulkey.applemint.model.Article

class MainViewModel(private val articleRepo: ArticleRepository) : ViewModel() {
    val filters: MutableLiveData<List<String>> by lazy {
        MutableLiveData<List<String>>()
    }
    val isFilterOpen: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    fun initialize(){
        articleRepo.loadArticles()
    }

    fun getArticles(): LiveData<List<Article>>{
        return articleRepo.getArticles()
    }

    fun removeArticle(fb_id: String) {
        articleRepo.removeArticle(fb_id)
    }


}
