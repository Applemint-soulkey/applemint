package com.soulkey.applemint.ui.login

import android.content.Context
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.soulkey.applemint.data.ArticleRepository
import com.soulkey.applemint.data.BookmarkRepository

class LoginViewModel(private val db: FirebaseFirestore, private val articleRepository: ArticleRepository, private val bookmarkRepository: BookmarkRepository, private val context: Context) : ViewModel() {
    private val isArticleUpdated : MutableLiveData<Boolean> = MutableLiveData(true)
    private val isBookmarkUpdated : MutableLiveData<Boolean> = MutableLiveData(false)
    val isUpdateComplete : MediatorLiveData<Boolean> = MediatorLiveData()
    val updateProcess: MutableLiveData<String> = MutableLiveData("Connect to Server..")

    init {
        isUpdateComplete.addSource(isArticleUpdated) {
            isUpdateComplete.value = isArticleUpdated.value!! && isBookmarkUpdated.value!!
        }
        isUpdateComplete.addSource(isBookmarkUpdated) {
            isUpdateComplete.value = isArticleUpdated.value!! && isBookmarkUpdated.value!!
        }
    }

    fun updateBookmarks() {
        isBookmarkUpdated.value = false
        bookmarkRepository.syncWithServer(isBookmarkUpdated)
    }
}