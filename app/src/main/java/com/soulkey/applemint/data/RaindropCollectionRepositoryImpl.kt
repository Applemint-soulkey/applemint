package com.soulkey.applemint.data

import androidx.lifecycle.LiveData
import com.google.gson.JsonArray
import com.soulkey.applemint.db.RaindropCollectionDao
import com.soulkey.applemint.model.RaindropCollection

class RaindropCollectionRepositoryImpl(private val raindropCollectionDao: RaindropCollectionDao) : RaindropCollectionRepository{
    override fun insertCollections(collections: JsonArray){
        collections.map {
            val collection = it.asJsonObject
            RaindropCollection(collection["_id"].asString, collection["title"].asString)
        }.also { raindropCollectionDao.insertCollections(it) }
    }

    override fun getCollections(): LiveData<List<RaindropCollection>> = raindropCollectionDao.getCollections()
    override fun getIdByCollectionName(name: String): String =raindropCollectionDao.getIdByCollectionName(name)
}