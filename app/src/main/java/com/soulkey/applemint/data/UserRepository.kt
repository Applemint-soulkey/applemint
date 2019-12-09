package com.soulkey.applemint.data

interface UserRepository {
    fun insert(email: String, dapina: String)
    fun getDapinaKey(email: String): String
}