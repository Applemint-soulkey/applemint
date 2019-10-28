package com.soulkey.applemint.data

import androidx.lifecycle.LiveData
import com.soulkey.applemint.model.Article

interface ArticleRepository {
    fun loadArticles()
    fun getArticles(): LiveData<List<Article>>
}