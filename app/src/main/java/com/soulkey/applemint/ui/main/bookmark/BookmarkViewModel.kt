package com.soulkey.applemint.ui.main.bookmark

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.soulkey.applemint.data.BookmarkRepository
import com.soulkey.applemint.model.Bookmark

class BookmarkViewModel(private val bookmarkRepo: BookmarkRepository) : ViewModel(){
    fun getBookmarks(): LiveData<List<Bookmark>> {
        return bookmarkRepo.getBookmarks()
    }

    fun getCategories(): List<String> {
        return bookmarkRepo.getCategories()
    }
}