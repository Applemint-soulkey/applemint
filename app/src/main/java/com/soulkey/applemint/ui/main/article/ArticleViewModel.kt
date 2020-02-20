package com.soulkey.applemint.ui.main.article

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.soulkey.applemint.common.raindrop.RaindropClient
import com.soulkey.applemint.common.raindrop.CreateRaindropResponse
import com.soulkey.applemint.data.ArticleRepository
import com.soulkey.applemint.data.RaindropCollectionRepository
import com.soulkey.applemint.model.Article
import io.reactivex.disposables.Disposable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class ArticleViewModel(
    private val context: Context,
    private val db: FirebaseFirestore,
    private val articleRepo: ArticleRepository,
    private val raindropCollectionRepository: RaindropCollectionRepository,
    private val raindropClient: RaindropClient
) : ViewModel() {
    var articles: MutableLiveData<List<Article>> = MutableLiveData(listOf())
    var typeFilter: MutableLiveData<List<String>> = MutableLiveData(listOf())
    var newArticles: MediatorLiveData<List<Article>> = MediatorLiveData()
    var readLaters: MediatorLiveData<List<Article>> = MediatorLiveData()
    val isDataLoading: MutableLiveData<Boolean> = MutableLiveData(true)

    init {
        fetchArticles()
        newArticles.addSource(typeFilter){ filterArticle(newArticles,"new") }
        newArticles.addSource(articles) { filterArticle(newArticles, "new") }
        readLaters.addSource(typeFilter){ filterArticle(readLaters, "keep") }
        readLaters.addSource(articles) { filterArticle(readLaters, "keep") }
    }

    fun testRainDrop(): Disposable {
        val collectionId = raindropCollectionRepository.getIdByCollectionName("battlepage")
        return raindropClient.createRaindrop(
            "Raindrop TEST",
            "http://v12.battlepage.com/??=Board.Etc.View&no=117232",
            listOf("test"),
            collectionId
        )
            .subscribe({ response->
                if (response.isSuccessful){
                    Timber.v("diver:/ ${response.body()?.itemDetail}")
                } else {
                    Timber.v("diver:/ ${response.errorBody().toString()}")
                }
            }, {
                Timber.v("diver:/ retrofit fail:: ${it.localizedMessage}")
            })
    }

    fun fetchArticles() {
        isDataLoading.value = true
        db.collection("article").orderBy("timestamp", Query.Direction.DESCENDING).get()
            .addOnSuccessListener { snapshot ->
                articles.value = snapshot.map { Article(it.id, it.data) }
                isDataLoading.value = false
            }.addOnFailureListener {
                Timber.v(it)
                Toast.makeText(context, "Can't fetch Articles From Server..", Toast.LENGTH_SHORT).show()
            }
    }

    private fun filterArticle(target: MediatorLiveData<List<Article>>, state: String){
        articles.value?.let {
            target.value = it
                .filter { article-> article.state == state }
                .filter { article->
                    typeFilter.value!!.contains(article.type) || typeFilter.value!!.isEmpty()
                }
        }
    }

    fun removeArticle(fb_id: String) {
        articleRepo.removeArticle(fb_id).addOnSuccessListener {
            Timber.v("diver:/ $fb_id delete success")
            articles.value?.let {list->
                articles.value = list.filter { it.fb_id != fb_id }
            }
        }.addOnFailureListener {
            Toast.makeText(context, "Error occurred during Remove article.. :-<", Toast.LENGTH_SHORT).show()
        }
    }

    fun restoreArticle(item: Article, position: Int) {
        articleRepo.restoreArticle(item).addOnSuccessListener {
            Timber.v("diver:/ ${item.fb_id} delete restored")
            articles.value?.let {list->
                list.toMutableList().also {
                    it.add(position, item)
                    articles.value = it
                }
            }
        }.addOnFailureListener {
            Toast.makeText(context, "Error occurred during Restore article.. :-<", Toast.LENGTH_SHORT).show()
        }
    }

    fun keepArticle(fb_id: String) {
        articleRepo.keepArticle(fb_id).addOnSuccessListener {
            articles.value?.let { list->
                list.indexOf(list.find { it.fb_id == fb_id }).also { keepIndex->
                    list[keepIndex].state = "keep"
                    articles.value = list
                }
            }
        }.addOnFailureListener {
            Toast.makeText(context, "Error occurred during Keep article.. :-<", Toast.LENGTH_SHORT).show()
        }
    }
}