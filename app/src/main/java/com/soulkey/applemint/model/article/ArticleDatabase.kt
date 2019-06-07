package com.soulkey.applemint.model.article

import android.content.Context
import androidx.room.*
import com.soulkey.applemint.utils.JsonConverter

@Database(entities = [Article::class], version = 2, exportSchema = false)
@TypeConverters(JsonConverter::class)
abstract class ArticleDatabase: RoomDatabase(){
    abstract fun articleDao() : ArticleDAO

    companion object {
        private var INSTANCE: ArticleDatabase? = null

        fun getInstance(context: Context): ArticleDatabase? {
            if(INSTANCE == null){
                synchronized(ArticleDatabase::class){
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext, ArticleDatabase::class.java, "article")
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE
        }
    }
}