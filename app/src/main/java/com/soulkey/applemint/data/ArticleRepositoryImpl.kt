package com.soulkey.applemint.data

import androidx.lifecycle.LiveData
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.soulkey.applemint.db.ArticleDao
import com.soulkey.applemint.model.Article
import timber.log.Timber

class ArticleRepositoryImpl(private val articleDao: ArticleDao) : ArticleRepository {
    private val db = FirebaseFirestore.getInstance()

    override fun getNewArticles(): LiveData<List<Article>> {
        return articleDao.getNewArticles()
    }

    override fun removeArticle(id: String) {
        db.collection("article").document(id).delete().addOnSuccessListener {
            articleDao.deleteByFbId(id)
            Timber.v("diver:/ $id delete success")
        }
    }

    override fun keepArticle(id: String) {
        db.collection("article").document(id).update("state", "keep").addOnSuccessListener {
            articleDao.setKeepStateArticle(id)
            Timber.v("diver:/ $id keeping success")
        }
    }

    override fun getFbIds(): List<String> {
        return articleDao.getFbIds()
    }

    override fun getArticlesSingle(): List<Article> {
        return articleDao.getArticleData()
    }

    override fun deleteById(id: String) {
        articleDao.deleteByFbId(id)
    }

    override fun insertAll(list: List<Article>) {
        articleDao.insertAll(list)
    }
}