package com.soulkey.applemint.ui.main.article

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.soulkey.applemint.data.ArticleRepository
import com.soulkey.applemint.model.Article
import timber.log.Timber

class ArticleViewModel(private val db: FirebaseFirestore, private val context: Context, private val articleRepo: ArticleRepository): ViewModel(){
    var articles: MutableLiveData<List<Article>> = MutableLiveData(listOf())
    var typeFilter: MutableLiveData<List<String>> = MutableLiveData(listOf())
    var newArticles: MediatorLiveData<List<Article>> = MediatorLiveData()
    var readLaters: MediatorLiveData<List<Article>> = MediatorLiveData()
    val isDataLoading: MutableLiveData<Boolean> = MutableLiveData(true)

    init {
        fetchArticles()
        newArticles.addSource(typeFilter){ filterArticle(newArticles,"new") }
        newArticles.addSource(articles) { filterArticle(newArticles, "new") }
        readLaters.addSource(typeFilter){ filterArticle(readLaters, "keep") }
        readLaters.addSource(articles) { filterArticle(readLaters, "keep") }
    }

    fun fetchArticles() {
        isDataLoading.value = true
        db.collection("article").orderBy("timestamp", Query.Direction.DESCENDING).get()
            .addOnSuccessListener { snapshot ->
                articles.value = snapshot.map { Article(it.id, it.data) }
                isDataLoading.value = false
            }.addOnFailureListener {
                Timber.v(it)
                Toast.makeText(context, "Can't fetch Articles From Server..", Toast.LENGTH_SHORT).show()
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

    fun removeArticle(fb_id: String) {
        articleRepo.removeArticle(fb_id).addOnSuccessListener {
            Timber.v("diver:/ $fb_id delete success")
            articles.value?.let {list->
                articles.value = list.filter { it.fb_id != fb_id }
            }
        }.addOnFailureListener {
            Toast.makeText(context, "Error occurred during Remove article.. :-<", Toast.LENGTH_SHORT).show()
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
        }.addOnFailureListener {
            Toast.makeText(context, "Error occurred during Restore article.. :-<", Toast.LENGTH_SHORT).show()
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
        }.addOnFailureListener {
            Toast.makeText(context, "Error occurred during Keep article.. :-<", Toast.LENGTH_SHORT).show()
        }
    }
}