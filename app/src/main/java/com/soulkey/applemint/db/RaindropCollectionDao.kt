package com.soulkey.applemint.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.soulkey.applemint.model.RaindropCollection

@Dao
interface RaindropCollectionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCollections(collections: List<RaindropCollection>)

    @Query("SELECT * FROM tb_raindrop_collections")
    fun getCollections(): LiveData<List<RaindropCollection>>

    @Query("SELECT id FROM tb_raindrop_collections WHERE collectionName=:name")
    fun getIdByCollectionName(name: String): String
}