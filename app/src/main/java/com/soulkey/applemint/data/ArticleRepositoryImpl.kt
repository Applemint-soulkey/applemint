package com.soulkey.applemint.data

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.soulkey.applemint.db.ArticleDao
import com.soulkey.applemint.db.BookmarkDao
import com.soulkey.applemint.model.Article
import com.soulkey.applemint.model.Bookmark
import timber.log.Timber

class ArticleRepositoryImpl(private val db: FirebaseFirestore, private val articleDao: ArticleDao, private val context: Context) : ArticleRepository {
    override fun syncWithServer(flag: MutableLiveData<Boolean>) {
        db.collection("article").get().addOnSuccessListener { snapshot ->
            deleteAll()
            snapshot.map { Article(it.id, it.data) }.also {insertAll(it)}
            flag.value = true
        }
    }

    override fun loadArticles(): LiveData<List<Article>> {
        return articleDao.loadAllArticles()
    }

    override fun deleteAll() {
        articleDao.deleteAllArticles()
    }

    override fun removeArticle(id: String) {
        db.collection("article").document(id).delete()
            .addOnSuccessListener {
                articleDao.deleteByFbId(id)
                Timber.v("diver:/ $id delete success")
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed To Remove..", Toast.LENGTH_SHORT).show()
            }
    }

    override fun keepArticle(id: String) {
        db.collection("article").document(id).update("state", "keep")
            .addOnSuccessListener {
                articleDao.setKeepStateArticle(id)
                Timber.v("diver:/ $id keeping success")
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed To Keep..", Toast.LENGTH_SHORT).show()
            }
    }

    override fun insert(article: Article) {
        val articleData = hashMapOf(
            "url" to article.url,
            "type" to article.type,
            "timestamp" to article.timestamp,
            "textContent" to article.content,
            "state" to article.state,
            "crawlSource" to article.source
        )
        db.collection("article").document(article.fb_id)
            .set(articleData)
            .addOnSuccessListener {
                articleDao.insert(article)
            }
    }

    override fun insertAll(list: List<Article>) {
        articleDao.insertAll(list)
    }
}