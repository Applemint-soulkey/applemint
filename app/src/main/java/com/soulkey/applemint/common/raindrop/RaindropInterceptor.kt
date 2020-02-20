package com.soulkey.applemint.common.raindrop

import com.soulkey.applemint.config.CurrentUser
import com.soulkey.applemint.data.UserRepository
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

open class RaindropInterceptor(private val currentUser: CurrentUser) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        return if (currentUser.getCurrentUser() != null){
            chain.proceed(request(chain.request()))
        } else {
            chain.proceed(chain.request())
        }
    }

    private fun request(request: Request): Request {
        return request.newBuilder()
            .addHeader("Authorization", "Bearer ${currentUser.getRaindropToken()}")
            .addHeader("Content-Type", "application/json")
            .url(request.url)
            .build()
    }
}