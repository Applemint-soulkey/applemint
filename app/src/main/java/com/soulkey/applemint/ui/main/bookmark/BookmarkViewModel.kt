package com.soulkey.applemint.ui.main.bookmark

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.soulkey.applemint.data.BookmarkRepository
import com.soulkey.applemint.model.Bookmark

class BookmarkViewModel(private val bookmarkRepo: BookmarkRepository) : ViewModel(){
    val categoryFilter: MutableLiveData<List<String>> by lazy {
        MutableLiveData<List<String>>()
    }
    val typeFilter: MutableLiveData<List<String>> by lazy {
        MutableLiveData<List<String>>()
    }
    var isFilterApply: MediatorLiveData<Boolean> = MediatorLiveData()

    init {
        categoryFilter.value = listOf()
        typeFilter.value = listOf()
        isFilterApply.addSource(categoryFilter) {
            isFilterApply.value = categoryFilter.value!!.isNotEmpty() || typeFilter.value!!.isNotEmpty()
        }
        isFilterApply.addSource(typeFilter) {
            isFilterApply.value = categoryFilter.value!!.isNotEmpty() || typeFilter.value!!.isNotEmpty()
        }
    }

    fun getBookmarks(): LiveData<List<Bookmark>> {
        return bookmarkRepo.getBookmarks()
    }

    fun getCategories(): List<String> {
        return bookmarkRepo.getCategories()
    }

    fun removeBookmark(fb_id: String) {
        bookmarkRepo.removeBookmark(fb_id)
    }

    fun restoreBookmark(item: Bookmark){
        bookmarkRepo.insert(item)
    }

    fun updateBookmark(bookmark: Bookmark){
        bookmarkRepo.updateBookmark(bookmark)
    }
}