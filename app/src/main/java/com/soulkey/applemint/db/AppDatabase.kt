package com.soulkey.applemint.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.soulkey.applemint.model.Article
import com.soulkey.applemint.model.Bookmark
import java.util.Date

@Database(entities = [Article::class, Bookmark::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun articleDao(): ArticleDao
    abstract fun bookmarkDao(): BookmarkDao
}

class DateConverter {
    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return if (timestamp == null) null else Date(timestamp)
    }

    @TypeConverter
    fun toTimestamp(date: Date?): Long? {
        return date?.time
    }
}
