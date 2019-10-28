package com.soulkey.applemint.ui.main

import androidx.lifecycle.ViewModel
import com.soulkey.applemint.data.ArticleRepository

class MainViewModel(private val articleRepo: ArticleRepository) : ViewModel() {
    fun initialize(){
        articleRepo.loadArticles()
    }
}
