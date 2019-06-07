package com.soulkey.applemint.model.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {
    private val localServer = "http://10.0.2.2:3000"
    private val cloudServer = "https://peppermint-soulkey.appspot.com/"

    companion object {
        private val retrofitClient: RetrofitClient = RetrofitClient()

        fun getInstance(): RetrofitClient {
            return retrofitClient
        }
    }

    fun buildRetrofit(): RetrofitService {
        val retrofit: Retrofit? = Retrofit.Builder()
            .baseUrl(localServer)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit!!.create(RetrofitService::class.java)
    }
}