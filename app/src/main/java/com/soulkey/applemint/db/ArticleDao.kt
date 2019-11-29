package com.soulkey.applemint.db

import androidx.room.Dao
import androidx.room.Insert
import com.soulkey.applemint.model.Article

@Dao
interface ArticleDao {
    @Insert
    fun insert(article: Article)
}