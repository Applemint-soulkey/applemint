package com.soulkey.applemint.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.soulkey.applemint.model.Item

@Dao
interface ItemDao {
    @Query("SELECT * FROM tb_item")
    fun getAllItems(): LiveData<List<Item>>
}