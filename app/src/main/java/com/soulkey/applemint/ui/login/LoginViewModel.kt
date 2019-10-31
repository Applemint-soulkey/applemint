package com.soulkey.applemint.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.soulkey.applemint.data.ArticleRepository
import com.soulkey.applemint.model.Article
import timber.log.Timber

class LoginViewModel(private val articleRepository: ArticleRepository) : ViewModel() {
    val isArticleUpdated: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    fun updateArticles(){
        val db = FirebaseFirestore.getInstance()
        isArticleUpdated.value = false
        db.collection("article").get().addOnSuccessListener {snapshot->
            val cloudMap = mutableMapOf<String, Article>()
            val fbIds = articleRepository.getFbIds()
            snapshot.map {document->
                val data = document.data
                val article = Article(
                    null,
                    document.id,
                    data["url"].toString(),
                    data["type"].toString(),
                    data["textContent"].toString(),
                    (data["timestamp"] as Timestamp).toDate(),
                    data["state"].toString()
                )
                cloudMap[document.id] = article
            }
            Timber.v("diver:/ local size=${fbIds.size}")
            Timber.v("diver:/ cloud size=${cloudMap.keys.size}")
            fbIds.filter { id-> !cloudMap.keys.contains(id) }.map { remove_id->
                Timber.v("diver:/ $remove_id is already removed!")
                articleRepository.deleteById(remove_id)
            }
            val insertList = cloudMap.values.filter {article ->  !fbIds.contains(article.fb_id) }
            Timber.v("diver:/ ${insertList.size} articles updated")
            articleRepository.insertAll(insertList)
            Timber.v("diver:/ insertList size=${insertList.size}")
            isArticleUpdated.value = true
        }
    }
}