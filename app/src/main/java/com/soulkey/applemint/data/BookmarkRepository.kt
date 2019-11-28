package com.soulkey.applemint.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.soulkey.applemint.model.Bookmark

interface BookmarkRepository{
    fun getCategories(): List<String>
    fun getBookmarks(): LiveData<List<Bookmark>>
    fun getFbIds(): List<String>
    fun deleteByFbIds(list: List<String>)
    fun syncWithServer(flag : MutableLiveData<Boolean>)
    fun insert(bookmark: Bookmark)
    fun insertAll(list: List<Bookmark>)
    fun removeBookmark(id: String)
    fun updateBookmark(bookmark: Bookmark)
}