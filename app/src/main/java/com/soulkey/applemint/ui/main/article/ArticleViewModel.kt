package com.soulkey.applemint.ui.main.article

import androidx.lifecycle.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.soulkey.applemint.data.ArticleRepository
import com.soulkey.applemint.data.BookmarkRepository
import com.soulkey.applemint.model.Article
import com.soulkey.applemint.model.Bookmark
import timber.log.Timber

class ArticleViewModel(private val db: FirebaseFirestore, private val articleRepo: ArticleRepository, private val bookmarkRepo:BookmarkRepository): ViewModel(){
    var articles: MutableLiveData<List<Article>> = MutableLiveData(listOf())
    var typeFilter: MutableLiveData<List<String>> = MutableLiveData(listOf())
    var newArticles: MediatorLiveData<List<Article>> = MediatorLiveData()
    var readLaters: MediatorLiveData<List<Article>> = MediatorLiveData()

    init {
        fetchArticles()
        newArticles.addSource(typeFilter){ filterArticle(newArticles,"new") }
        newArticles.addSource(articles) { filterArticle(newArticles, "new") }
        readLaters.addSource(typeFilter){ filterArticle(readLaters, "keep") }
        readLaters.addSource(articles) { filterArticle(readLaters, "keep") }
    }

    fun fetchArticles(){
        db.collection("article").orderBy("timestamp", Query.Direction.DESCENDING).get().addOnSuccessListener { snapshot ->
            articles.value = snapshot.map { Article(it.id, it.data) }
        }
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
        articleRepo.removeArticle(item.fb_id).addOnSuccessListener {
            Timber.v("diver:/ ${item.fb_id} delete success")
            articles.value?.let {list->
                articles.value = list.filter { it.fb_id != item.fb_id }
            }
        }
        bookmarkRepo.insert(Bookmark(item, category))
    }

    fun getCategories(): List<String> {
        return bookmarkRepo.getCategories()
    }

    fun removeArticle(fb_id: String) {
        articleRepo.removeArticle(fb_id).addOnSuccessListener {
            Timber.v("diver:/ $fb_id delete success")
            articles.value?.let {list->
                articles.value = list.filter { it.fb_id != fb_id }
            }
        }
    }

    fun restoreArticle(item: Article, position: Int) {
        articleRepo.restoreArticle(item).addOnSuccessListener {
            Timber.v("diver:/ ${item.fb_id} delete restored")
            articles.value?.let {list->
                list.toMutableList().also {
                    it.add(position, item)
                    articles.value = it
                }
            }
        }
    }

    fun keepArticle(fb_id: String) {
        articleRepo.keepArticle(fb_id).addOnSuccessListener {
            articles.value?.let { list->
                list.indexOf(list.find { it.fb_id == fb_id }).also { keepIndex->
                    list[keepIndex].state = "keep"
                    articles.value = list
                }
            }
        }
    }
}