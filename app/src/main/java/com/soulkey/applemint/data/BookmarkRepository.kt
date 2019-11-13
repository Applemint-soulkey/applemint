package com.soulkey.applemint.data

import androidx.lifecycle.LiveData
import com.soulkey.applemint.model.Bookmark

interface BookmarkRepository{
    fun getCategories(): List<String>
    fun getBookmarks(): LiveData<List<Bookmark>>
    fun getFbIds(): List<String>
    fun deleteByFbIds(list: List<String>)
    fun insertAll(list: List<Bookmark>)
    fun insert(bookmark: Bookmark)
}