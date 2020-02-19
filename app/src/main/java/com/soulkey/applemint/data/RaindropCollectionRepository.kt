package com.soulkey.applemint.data

import androidx.lifecycle.LiveData
import com.google.gson.JsonArray
import com.soulkey.applemint.model.RaindropCollection

interface RaindropCollectionRepository {
    fun insertCollections(collections: JsonArray)
    fun getCollections(): LiveData<List<RaindropCollection>>
    fun getIdByCollectionName(name: String): String
}