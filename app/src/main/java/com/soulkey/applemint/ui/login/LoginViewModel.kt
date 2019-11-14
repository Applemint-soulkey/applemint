package com.soulkey.applemint.ui.login

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.soulkey.applemint.data.ArticleRepository
import com.soulkey.applemint.data.BookmarkRepository
import com.soulkey.applemint.model.Article
import com.soulkey.applemint.model.Bookmark

class LoginViewModel(private val db: FirebaseFirestore, private val articleRepository: ArticleRepository, private val bookmarkRepository: BookmarkRepository, private val context: Context) : ViewModel() {
    var isArticleUpdated = false
    var isBookmarkUpdated = false
    val isUpdated: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val updateProcess: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun updateBookmarks() {
        isBookmarkUpdated = false
        db.collection("bookmark").get().addOnSuccessListener {snapshot->
            val bookmarkList = snapshot.map { Bookmark(it.id, it.data) }
            val fbIds = bookmarkRepository.getFbIds()
            val cloudFbIds = bookmarkList.map { it.fb_id }

            updateProcess.value = "Remove deleted bookmarks.."
            bookmarkRepository.deleteByFbIds(fbIds.filter { !cloudFbIds.contains(it) })

            updateProcess.value = "Load new Bookmarks from Server.."
            bookmarkRepository.insertAll(bookmarkList.filter { !fbIds.contains(it.fb_id) })

            isBookmarkUpdated = true
            isUpdated.value = isArticleUpdated && isBookmarkUpdated
        }.addOnFailureListener {
            isArticleUpdated = false
            Toast.makeText(context, "Failed To Update Bookmarks..", Toast.LENGTH_SHORT).show()
        }
    }

    fun updateArticles(){
        isArticleUpdated = false
        db.collection("article").get().addOnSuccessListener {snapshot->
            val articleList = snapshot.map {Article(it.id, it.data)}
            val fbIds = articleRepository.getFbIds()
            val cloudFbIds = articleList.map { it.fb_id }

            updateProcess.value = "Remove already read items.."
            articleRepository.deleteByIds(fbIds.filter { !cloudFbIds.contains(it) })

            updateProcess.value = "Load new items from Server.."
            articleRepository.insertAll(articleList.filter {!fbIds.contains(it.fb_id)})

            isArticleUpdated = true
            isUpdated.value = isArticleUpdated && isBookmarkUpdated
        }.addOnFailureListener {
            isArticleUpdated = false
            Toast.makeText(context, "Failed To Update Articles..", Toast.LENGTH_SHORT).show()
        }
    }
}