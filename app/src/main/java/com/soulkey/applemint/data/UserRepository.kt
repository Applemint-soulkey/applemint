package com.soulkey.applemint.data

interface UserRepository {
    fun insert(email: String, dapina: String, message_token: String)
    fun setCurrentUser(email: String, dapinaKey: String)
}