package com.soulkey.applemint.model.article

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ArticleDAO {
    @Query("SELECT * FROM article")
    fun getAll(): LiveData<List<Article>>

    @Query("DELETE FROM article WHERE firebase_id==:fbId")
    fun deleteById(fbId: String)

    @Query("UPDATE article SET isBookmarked=:value WHERE firebase_id==:fbId")
    fun setBookmarkState(fbId: String, value:Boolean)

    @Query("DELETE FROM article")
    fun clear()

    @Update
    fun update(article: Article)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(article: Article)

    @Delete
    fun delete(article: Article)
}