package com.soulkey.applemint.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.soulkey.applemint.data.ArticleRepository
import com.soulkey.applemint.model.Article

class MainViewModel(private val articleRepo: ArticleRepository) : ViewModel() {
    fun initialize(){
        articleRepo.loadArticles()
    }

    fun getArticles(): LiveData<List<Article>>{
        return articleRepo.getArticles()
    }
}
