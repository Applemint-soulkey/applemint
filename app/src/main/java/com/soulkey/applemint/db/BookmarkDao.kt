package com.soulkey.applemint.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.soulkey.applemint.model.Bookmark

@Dao
interface BookmarkDao {

    @Query("SELECT * FROM tb_bookmark ORDER BY timestamp DESC")
    fun getBookmarks(): LiveData<List<Bookmark>>

    @Query("SELECT category FROM tb_bookmark GROUP BY category")
    fun getCategories(): List<String>

    @Query("SELECT fb_id FROM tb_bookmark")
    fun getFbIds() : List<String>

    @Query("DELETE FROM tb_bookmark WHERE fb_id IN (:list)")
    fun deleteByFbIds(list: List<String>)

    @Insert
    fun insert(bookmark: Bookmark)

    @Insert
    fun insertAll(list: List<Bookmark>)

    @Update
    fun updateBookmark(bookmark: Bookmark)
}