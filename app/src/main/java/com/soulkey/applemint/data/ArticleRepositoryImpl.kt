package com.soulkey.applemint.data

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.soulkey.applemint.db.ArticleDao
import com.soulkey.applemint.model.Article
import timber.log.Timber

class ArticleRepositoryImpl(private val articleDao: ArticleDao, private val context: Context) : ArticleRepository {
    private val db = FirebaseFirestore.getInstance()

    override fun getNewArticles(): LiveData<List<Article>> {
        return articleDao.getNewArticles()
    }

    override fun getReadLater(): LiveData<List<Article>> {
        return articleDao.getReadLaters()
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

    override fun restoreArticle(item: Article) {
        db.collection("article").document(item.fb_id).set(item)
            .addOnSuccessListener {
                Timber.v("diver:/ restore success")
                articleDao.insert(item)
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed To Restore..", Toast.LENGTH_SHORT).show()
            }
    }

    override fun bookmarkArticle(category: String, item: Article) {
        val bookmarkArticle = hashMapOf(
            "content" to item.content,
            "state" to "bookmark",
            "timestamp" to Timestamp.now(),
            "type" to item.type,
            "category" to category,
            "url" to item.url
        )
        db.collection("bookmark").document(item.fb_id).set(bookmarkArticle).addOnCompleteListener {saveTask->
            if (saveTask.isSuccessful){
                db.collection("article").document(item.fb_id).delete().addOnCompleteListener {deleteTask->
                    if (deleteTask.isSuccessful) {
                        articleDao.deleteByFbId(item.fb_id)
                        Toast.makeText(context, "Bookmark Success!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Failed to Bookmark..", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(context, "Failed to Bookmark..", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun bookmarkCategories(): List<String>? {
        return db.collection("bookmark").get().result?.documents?.map { it["category"].toString() }?.distinct()
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

    override fun deleteByIds(list: List<String>) {
        articleDao.deleteByFbIds(list)
    }

    override fun insertAll(list: List<Article>) {
        articleDao.insertAll(list)
    }
}