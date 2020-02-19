package com.soulkey.applemint.config

import com.soulkey.applemint.model.User

data class CurrentUser(var user: User? = null){
    fun getCurrentUser(): User? = user
    fun getRaindropToken(): String? {
        return user?.raindrop_token
    }
}