package com.soulkey.applemint.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.soulkey.applemint.data.ArticleRepository
import com.soulkey.applemint.model.Article
import timber.log.Timber

class MainViewModel(private val articleRepo: ArticleRepository) : ViewModel() {
    val filters: MutableLiveData<List<String>> by lazy {
        MutableLiveData<List<String>>()
    }
    val isFilterOpen: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    fun initialize(){
        Timber.v("diver:/ call on initialize")
        articleRepo.loadArticles()
    }

    fun getArticles(): LiveData<List<Article>>{
        return articleRepo.getArticles()
    }

    fun removeArticle(fb_id: String) {
        articleRepo.removeArticle(fb_id)
    }


}
