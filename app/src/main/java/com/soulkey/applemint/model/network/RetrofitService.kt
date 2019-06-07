package com.soulkey.applemint.model.network

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {
    @GET("all")
    fun getAll() : Call<JsonArray>

    @GET("visit")
    fun visit(@Query("id") id: String) : Call<JsonObject>

    @GET("bookmark")
    fun bookmark(@Query("id") id: String, @Query("value") value: Boolean): Call<JsonObject>

    @GET("dapina")
    fun dapina(@Query("id") id: String): Call<JsonObject>
}