package com.soulkey.applemint.ui.main.article

import androidx.lifecycle.*
import com.soulkey.applemint.config.typeMapper
import com.soulkey.applemint.data.ArticleRepository
import com.soulkey.applemint.data.BookmarkRepository
import com.soulkey.applemint.model.Article
import com.soulkey.applemint.model.Bookmark

class ArticleViewModel(private val articleRepo: ArticleRepository, private val bookmarkRepo:BookmarkRepository): ViewModel(){
    var typeFilter: MutableLiveData<List<String>> = MutableLiveData()
    var articles: LiveData<List<Article>>
    var filterArticle: MediatorLiveData<List<Article>> = MediatorLiveData()
    var newArticles: MediatorLiveData<List<Article>> = MediatorLiveData()
    var readLaters: MediatorLiveData<List<Article>> = MediatorLiveData()

    init {
        typeFilter.value = listOf()
        articles = articleRepo.loadArticles()

        newArticles.addSource(typeFilter){
            articles.value?.let {
                newArticles.value = it
                    .filter { article-> article.state == "new" }
                    .filter { article->
                    typeFilter.value!!.contains(article.type) || typeFilter.value!!.isEmpty()
                }
            }
        }
        newArticles.addSource(articles) {
            articles.value?.let {
                newArticles.value = it
                    .filter { article-> article.state == "new" }
                    .filter { article->
                    typeFilter.value!!.contains(article.type) || typeFilter.value!!.isEmpty()
                }
            }
        }
        readLaters.addSource(typeFilter){
            articles.value?.let {
                readLaters.value = it
                    .filter { article-> article.state == "keep" }
                    .filter { article->
                    typeFilter.value!!.contains(article.type) || typeFilter.value!!.isEmpty()
                }
            }
        }
        readLaters.addSource(articles) {
            articles.value?.let {
                readLaters.value = it
                    .filter { article-> article.state == "keep" }
                    .filter { article->
                    typeFilter.value!!.contains(article.type) || typeFilter.value!!.isEmpty()
                }
            }
        }
    }

    fun bookmarkArticle(category: String, item: Article){
        articleRepo.removeArticle(item.fb_id)
        bookmarkRepo.insert(Bookmark(item, category))
    }

    fun getCategories(): List<String> {
        return bookmarkRepo.getCategories()
    }

    fun removeArticle(fb_id: String) {
        articleRepo.removeArticle(fb_id)
    }

    fun restoreArticle(item: Article) {
        articleRepo.insert(item)
    }

    fun keepArticle(fb_id: String) {
        articleRepo.keepArticle(fb_id)
    }
}