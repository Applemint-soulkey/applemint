package com.soulkey.applemint.db

import androidx.lifecycle.LiveData
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

    @Query("UPDATE tb_article SET state='keep' WHERE fb_id=:id")
    fun setKeepStateArticle(id: String)

    @Query("SELECT * FROM tb_article ORDER BY timestamp DESC")
    fun loadAllArticles(): LiveData<List<Article>>
}