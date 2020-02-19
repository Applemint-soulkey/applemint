package com.soulkey.applemint.data

interface UserRepository {
    fun setCurrentUser(email: String, dapina: String, message_token: String, raindrop_key: String)
}