package com.soulkey.applemint.data

import androidx.lifecycle.LiveData
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.soulkey.applemint.db.BookmarkDao
import com.soulkey.applemint.model.Bookmark

class BookmarkRepositoryImpl(val db : FirebaseFirestore, private val bookmarkDao: BookmarkDao)  : BookmarkRepository {
    override fun getCategories(): List<String> {
        return bookmarkDao.getCategories()
    }

    override fun getBookmarks(): LiveData<List<Bookmark>> {
        return bookmarkDao.getBookmarks()
    }

    override fun insert(bookmark: Bookmark) {
        val bookmarkArticle = hashMapOf(
            "content" to bookmark.content,
            "state" to "bookmark",
            "timestamp" to Timestamp.now(),
            "type" to bookmark.type,
            "category" to bookmark.category,
            "url" to bookmark.url
        )
        db.collection("bookmark").document(bookmark.fb_id)
            .set(bookmarkArticle)
            .addOnSuccessListener {
                bookmarkDao.insert(bookmark)
            }
    }

    override fun insertAll(list: List<Bookmark>) {
        bookmarkDao.insertAll(list)
    }

    override fun removeBookmark(id: String) {
        db.collection("bookmark").document(id).delete()
            .addOnSuccessListener {
                bookmarkDao.deleteByFbIds(listOf(id))
            }
    }

    override fun deleteByFbIds(list: List<String>) {
        bookmarkDao.deleteByFbIds(list)
    }

    override fun getFbIds(): List<String> {
        return bookmarkDao.getFbIds()
    }
}