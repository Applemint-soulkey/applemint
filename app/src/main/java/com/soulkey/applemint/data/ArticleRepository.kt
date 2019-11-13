package com.soulkey.applemint.data

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.soulkey.applemint.model.Article

interface ArticleRepository {
    fun getArticlesSingle(): List<Article>
    fun getNewArticles(): LiveData<List<Article>>
    fun getReadLater(): LiveData<List<Article>>
    fun removeArticle(id: String)
    fun restoreArticle(item: Article)
    fun keepArticle(id: String)
    fun bookmarkArticle(category: String, item: Article)
    fun bookmarkCategories(): List<String>?
    fun getFbIds(): List<String>
    fun deleteById(id: String)
    fun deleteByIds(list: List<String>)
    fun insertAll(list: List<Article>)
}