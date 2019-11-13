package com.soulkey.applemint.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp
import java.util.Date

@Entity(tableName = "tb_bookmark")
data class Bookmark(
    @PrimaryKey(autoGenerate = true)
    val id: Long?,

    @ColumnInfo(name = "fb_id")
    val fb_id: String,

    @ColumnInfo(name = "url")
    val url: String,

    @ColumnInfo(name = "content")
    val content: String,

    @ColumnInfo(name = "type")
    val type: String,

    @ColumnInfo(name = "category")
    val category: String,

    @ColumnInfo(name = "timestamp")
    val timestamp: Date
) {
    constructor(id: String, data:Map<String, Any>): this (
        id = null,
        fb_id = id,
        url = data["url"].toString(),
        content = data["content"].toString(),
        type = data["type"].toString(),
        category = data["category"].toString(),
        timestamp = (data["timestamp"] as Timestamp).toDate()
    )
    constructor(article: Article, category: String): this(
        id = null,
        fb_id = article.fb_id,
        url = article.url,
        content = article.content,
        type = article.type,
        category =  category,
        timestamp = article.timestamp
    )
}