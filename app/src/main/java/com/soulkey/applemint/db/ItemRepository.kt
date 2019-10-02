package com.soulkey.applemint.db

import androidx.lifecycle.LiveData
import com.soulkey.applemint.model.Item

class ItemRepository(private val itemDao: ItemDao) {
    val itemList: LiveData<List<Item>> = itemDao.getAllItems()
}