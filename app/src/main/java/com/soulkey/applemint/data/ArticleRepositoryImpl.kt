package com.soulkey.applemint.data

import androidx.lifecycle.LiveData
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.soulkey.applemint.db.ArticleDao
import com.soulkey.applemint.model.Article
import timber.log.Timber

class ArticleRepositoryImpl(private val articleDao: ArticleDao) : ArticleRepository {
    private val db = FirebaseFirestore.getInstance()

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
        val insertList = mutableListOf<Article>()
        db.collection("article").get().addOnSuccessListener {
            val fbIds = articleDao.getFbIds()
            for (document in it){
                val data = document.data
                val article = Article(
                    null,
                    document.id,
                    data["url"].toString(),
                    data["type"].toString(),
                    data["textContent"].toString(),
                    (data["timestamp"] as Timestamp).toDate()
                )
                if (!fbIds.contains(document.id)) insertList.add(article)
            }
            articleDao.insertAll(insertList)
            Timber.v("diver:/ "+insertList.size.toString())
        }
    }
}