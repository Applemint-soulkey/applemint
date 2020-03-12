package com.soulkey.applemint.common.raindrop

import com.google.gson.JsonObject
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class RaindropClient(private val api: RaindropAPI) {
    fun getCollections() : Single<Response<GetCollectionsResponse>>{
        return api.getCollections()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun createRaindrop(
        title: String,
        link: String,
        tags: List<String>,
        collectionId: String
    ): Single<Response<CreateRaindropResponse>> {
        val collectionObject = JsonObject().apply {
            this.addProperty("\$id", collectionId)
        }
        Raindrop(title, link, tags, collectionObject).also {
            return api.createRaindrop(it)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }
}