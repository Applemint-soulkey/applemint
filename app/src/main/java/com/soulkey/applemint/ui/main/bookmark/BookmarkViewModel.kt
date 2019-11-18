package com.soulkey.applemint.ui.main.bookmark

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.soulkey.applemint.data.BookmarkRepository
import com.soulkey.applemint.model.Bookmark

class BookmarkViewModel(private val bookmarkRepo: BookmarkRepository) : ViewModel(){
    val categoryFilter: MutableLiveData<List<String>> = MutableLiveData()
    val typeFilter: MutableLiveData<List<String>> = MutableLiveData()
    val searchKeyword: MutableLiveData<String> = MutableLiveData()

    private var bookmarks: LiveData<List<Bookmark>>
    var isFilterApply: MediatorLiveData<Boolean> = MediatorLiveData()
    var filterBookmarks: MediatorLiveData<List<Bookmark>> = MediatorLiveData()

    init {
        categoryFilter.value = listOf()
        typeFilter.value = listOf()
        searchKeyword.value = ""
        bookmarks = bookmarkRepo.getBookmarks()

        isFilterApply.addSource(categoryFilter) { checkFilterApply() }
        isFilterApply.addSource(typeFilter) { checkFilterApply() }

        filterBookmarks.addSource(bookmarks) { filterItem() }
        filterBookmarks.addSource(searchKeyword) { filterItem() }
        filterBookmarks.addSource(typeFilter) { filterItem() }
        filterBookmarks.addSource(categoryFilter) { filterItem() }
    }

    private fun checkFilterApply() {
        isFilterApply.value = categoryFilter.value!!.isNotEmpty() || typeFilter.value!!.isNotEmpty()
    }

    private fun filterItem() {
        bookmarks.value?.let {
            filterBookmarks.value = it
                .filter { bookmark-> bookmark.content.contains(searchKeyword.value!!) || searchKeyword.value!!.isEmpty()}
                .filter { bookmark-> typeFilter.value!!.contains(bookmark.type) || typeFilter.value!!.isEmpty()}
                .filter { bookmark-> categoryFilter.value!!.contains(bookmark.category) || categoryFilter.value!!.isEmpty()}
        }
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