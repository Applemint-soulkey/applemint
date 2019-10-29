package com.soulkey.applemint.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.soulkey.applemint.model.Article

@Dao
interface ArticleDao {
    @Insert
    fun insert(article: Article)

    @Insert
    fun insertAll(routes : List<Article>)

    @Query("DELETE FROM tb_article")
    fun deleteAllArticles()

    @Query("DELETE FROM tb_article WHERE fb_id=:id")
    fun deleteByFbId(id: String)

    @Query("SELECT * FROM tb_article ORDER BY type DESC")
    fun getAllArticles(): LiveData<List<Article>>

    @Query("SELECT fb_id FROM tb_article")
    fun getFbIds(): List<String>
}