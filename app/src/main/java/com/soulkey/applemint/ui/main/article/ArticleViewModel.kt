package com.soulkey.applemint.ui.main.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.soulkey.applemint.data.ArticleRepository
import com.soulkey.applemint.data.BookmarkRepository
import com.soulkey.applemint.model.Article
import com.soulkey.applemint.model.Bookmark

class ArticleViewModel(private val articleRepo: ArticleRepository, private val bookmarkRepo:BookmarkRepository): ViewModel(){
    fun bookmarkArticle(category: String, item: Article){
        articleRepo.removeArticle(item.fb_id)
        bookmarkRepo.insert(Bookmark(item, category))
    }

    fun getNewArticles(): LiveData<List<Article>> {
        return articleRepo.getNewArticles()
    }

    fun getReadLaters(): LiveData<List<Article>> {
        return articleRepo.getReadLater()
    }

    fun getCategories(): List<String> {
        return bookmarkRepo.getCategories()
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