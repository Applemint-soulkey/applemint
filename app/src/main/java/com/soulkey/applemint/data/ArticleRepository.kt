package com.soulkey.applemint.data

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.soulkey.applemint.model.Article

interface ArticleRepository {
    fun removeArticle(id: String)
    fun deleteAll()
    fun insertAll(list: List<Article>)
    fun insert(article: Article)
    fun keepArticle(id: String)
    fun loadArticles(): LiveData<List<Article>>
}