package com.soulkey.applemint.data

import androidx.lifecycle.LiveData
import com.soulkey.applemint.db.BookmarkDao
import com.soulkey.applemint.model.Bookmark

class BookmarkRepositoryImpl(private val bookmarkDao: BookmarkDao)  : BookmarkRepository {
    override fun getCategories(): List<String> {
        return bookmarkDao.getCategories()
    }

    override fun getBookmarks(): LiveData<List<Bookmark>> {
        return bookmarkDao.getBookmarks()
    }

    override fun insert(bookmark: Bookmark) {
        bookmarkDao.insert(bookmark)
    }

    override fun insertAll(list: List<Bookmark>) {
        bookmarkDao.insertAll(list)
    }

    override fun deleteByFbIds(list: List<String>) {
        bookmarkDao.deleteByFbIds(list)
    }

    override fun getFbIds(): List<String> {
        return bookmarkDao.getFbIds()
    }
}