package com.soulkey.applemint.common.raindrop

import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RaindropAPI {
    object EndPoint {
        const val baseURL = "https://api.raindrop.io/"
    }

    @GET("rest/v1/collections")
    fun getCollections() : Single<Response<GetCollectionsResponse>>

    @POST("rest/v1/raindrop")
    fun createRaindrop(@Body body: Raindrop): Single<Response<CreateRaindropResponse>>
}