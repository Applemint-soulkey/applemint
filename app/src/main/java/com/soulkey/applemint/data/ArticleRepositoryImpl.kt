package com.soulkey.applemint.data

import androidx.lifecycle.LiveData
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.soulkey.applemint.db.ArticleDao
import com.soulkey.applemint.model.Article
import timber.log.Timber

class ArticleRepositoryImpl(private val articleDao: ArticleDao) : ArticleRepository {
    private val db = FirebaseFirestore.getInstance()

    override fun getArticlesSingle(): List<Article> {
        return articleDao.getAritlceSingle()
    }

    override fun getArticles(): LiveData<List<Article>> {
        return articleDao.getAllArticles()
    }

    override fun removeArticle(id: String) {
        db.collection("article").document(id).delete().addOnSuccessListener {
            articleDao.deleteByFbId(id)
            Timber.v("diver:/ $id delete success")
        }
    }

    override fun loadArticles() {
        db.collection("article").get().addOnSuccessListener {snapshot->
            val fbIds = articleDao.getFbIds()
            val insertList: List<Article>
            val cloudMap = mutableMapOf<String, Article>()
            snapshot.map {document->
                val data = document.data
                val article = Article(
                    null,
                    document.id,
                    data["url"].toString(),
                    data["type"].toString(),
                    data["textContent"].toString(),
                    (data["timestamp"] as Timestamp).toDate()
                )
                cloudMap[document.id] = article
            }
            Timber.v("diver:/ local size=${fbIds.size}")
            Timber.v("diver:/ cloud size=${cloudMap.keys.size}")
            fbIds.filter { id-> !cloudMap.keys.contains(id) }.map { remove_id->
                Timber.v("diver:/ $remove_id is already removed!")
                articleDao.deleteByFbId(remove_id)
            }
            insertList = cloudMap.values.filter {article ->  !fbIds.contains(article.fb_id) }
            Timber.v("diver:/ ${insertList.size} articles updated")

            articleDao.insertAll(insertList)
            Timber.v("diver:/ insertList size=${insertList.size}")
        }

    }
}