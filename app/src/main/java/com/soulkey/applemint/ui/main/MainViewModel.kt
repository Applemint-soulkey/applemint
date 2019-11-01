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

    fun getInitialData(): List<Article> {
        return articleRepo.getArticlesSingle()
    }

    fun getNewArticles(): LiveData<List<Article>>{
        return articleRepo.getNewArticles()
    }

    fun getReadLaters(): LiveData<List<Article>> {
        return articleRepo.getReadLater()
    }

    fun removeArticle(fb_id: String) {
        articleRepo.removeArticle(fb_id)
    }

    fun restoreArticle(item: Article) {
        articleRepo.restoreArticle(item)
    }

    fun keepArticle(fb_id: String) {
        articleRepo.keepArticle(fb_id)
    }
}
