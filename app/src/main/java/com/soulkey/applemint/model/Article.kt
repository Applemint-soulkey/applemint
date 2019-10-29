package com.soulkey.applemint.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "tb_article")
data class Article(
    @PrimaryKey(autoGenerate = true)
    val id: Long?,

    @ColumnInfo(name = "fb_id")
    val fb_id: String,

    @ColumnInfo(name = "url")
    val url: String,

    @ColumnInfo(name = "type")
    val type: String,

    @ColumnInfo(name = "content")
    val content: String,

    @ColumnInfo(name = "timestamp")
    val timestamp: Date
)