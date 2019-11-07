package com.soulkey.applemint.ui.login

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.soulkey.applemint.data.ArticleRepository
import com.soulkey.applemint.model.Article

class LoginViewModel(private val articleRepository: ArticleRepository, private val context: Context) : ViewModel() {
    val isArticleUpdated: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val updateProcess: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun updateArticles(){
        val db = FirebaseFirestore.getInstance()
        isArticleUpdated.value = false
        db.collection("article").get().addOnSuccessListener {snapshot->
            val articleList = snapshot.map {Article(it.id, it.data)}
            val fbIds = articleRepository.getFbIds()
            val cloudFbIds = articleList.map { it.fb_id }

            updateProcess.value = "Remove already read items.."
            articleRepository.deleteByIds(fbIds.filter { !cloudFbIds.contains(it) })

            updateProcess.value = "Load new items from Server.."
            articleRepository.insertAll(articleList.filter {!fbIds.contains(it.fb_id)})

            isArticleUpdated.value = true
        }.addOnFailureListener {
            isArticleUpdated.value = false
            Toast.makeText(context, "Failed To Update Articles..", Toast.LENGTH_SHORT).show()
        }
    }
}