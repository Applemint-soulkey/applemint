package com.soulkey.applemint.ui.main.article

import androidx.lifecycle.*
import com.soulkey.applemint.data.ArticleRepository
import com.soulkey.applemint.data.BookmarkRepository
import com.soulkey.applemint.model.Article
import com.soulkey.applemint.model.Bookmark

class ArticleViewModel(private val articleRepo: ArticleRepository, private val bookmarkRepo:BookmarkRepository): ViewModel(){
    var typeFilter: MutableLiveData<List<String>> = MutableLiveData(listOf())
    var isArticleUpdated: MutableLiveData<Boolean> = MutableLiveData()
    private var articles: LiveData<List<Article>> = articleRepo.loadArticles()
    var newArticles: MediatorLiveData<List<Article>> = MediatorLiveData()
    var readLaters: MediatorLiveData<List<Article>> = MediatorLiveData()

    init {
        newArticles.addSource(typeFilter){ filterArticle(newArticles,"new") }
        newArticles.addSource(articles) { filterArticle(newArticles, "new") }
        readLaters.addSource(typeFilter){ filterArticle(readLaters, "keep") }
        readLaters.addSource(articles) { filterArticle(readLaters, "keep") }
    }

    fun triggerUpdate() {
        articleRepo.syncWithServer(isArticleUpdated)
    }

    private fun filterArticle(target: MediatorLiveData<List<Article>>, state: String){
        articles.value?.let {
            target.value = it
                .filter { article-> article.state == state }
                .filter { article->
                    typeFilter.value!!.contains(article.type) || typeFilter.value!!.isEmpty()
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