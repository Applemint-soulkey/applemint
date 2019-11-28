package com.soulkey.applemint.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.soulkey.applemint.db.BookmarkDao
import com.soulkey.applemint.model.Bookmark
import timber.log.Timber

class BookmarkRepositoryImpl(val db : FirebaseFirestore, private val bookmarkDao: BookmarkDao)  : BookmarkRepository {
    override fun syncWithServer(flag : MutableLiveData<Boolean>) {
        flag.value = false
        db.collection("bookmark").get().addOnSuccessListener { snapshot->
            val serverItems = snapshot.map { Bookmark(it.id, it.data) }
            val serverFbIds = serverItems.map { it.fb_id }
            val localFbIds = getFbIds()

            deleteByFbIds(localFbIds.filter { !serverFbIds.contains(it) })
            insertAll(serverItems.filter { !localFbIds.contains(it.fb_id) })
            flag.value = true
        }
    }

    override fun updateBookmark(bookmark: Bookmark) {
        val bookmarkArticle = hashMapOf(
            "content" to bookmark.content,
            "state" to "bookmark",
            "timestamp" to bookmark.timestamp,
            "type" to bookmark.type,
            "category" to bookmark.category,
            "url" to bookmark.url
        )
        db.collection("bookmark").apply {
            document(bookmark.fb_id).delete().addOnSuccessListener {
                document().also {docRef->
                    docRef.set(bookmarkArticle).addOnSuccessListener {
                        bookmark.fb_id = docRef.id
                        bookmarkDao.updateBookmark(bookmark)
                    }
                }
            }
        }
    }

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