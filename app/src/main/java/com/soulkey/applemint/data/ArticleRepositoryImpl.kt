package com.soulkey.applemint.data

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.soulkey.applemint.model.Article

class ArticleRepositoryImpl(private val db: FirebaseFirestore) : ArticleRepository {

    override fun restoreArticle(item: Article): Task<Void> {
        val articleData = hashMapOf(
            "url" to item.url,
            "type" to item.type,
            "timestamp" to item.timestamp,
            "textContent" to item.content,
            "state" to item.state,
            "crawlSource" to item.source
        )
        return db.collection("article").document(item.fb_id).set(articleData)
    }

    override fun removeArticle(id: String) : Task<Void> {
        return db.collection("article").document(id).delete()
    }

    override fun keepArticle(id: String) :Task<Void> {
        return db.collection("article").document(id).update("state", "keep")
    }
}