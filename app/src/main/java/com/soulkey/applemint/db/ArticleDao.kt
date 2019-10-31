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

    @Query("SELECT * FROM tb_article ORDER BY timestamp DESC")
    fun getArticleData(): List<Article>

    @Query("SELECT * FROM tb_article WHERE state='new' ORDER BY timestamp DESC")
    fun getNewArticles(): LiveData<List<Article>>

    @Query("UPDATE tb_article SET state='keep' WHERE fb_id=:id")
    fun setKeepStateArticle(id: String)

    @Query("SELECT fb_id FROM tb_article")
    fun getFbIds(): List<String>
}