package com.soulkey.applemint.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp
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
    val timestamp: Date,

    @ColumnInfo(name = "state")
    val state: String
){
    constructor(id: String, data: Map<String, Any>) : this(
        id = null,
        fb_id = id,
        url = data["url"].toString(),
        type = data["type"].toString(),
        content = data["textContent"].toString(),
        timestamp = (data["timestamp"] as Timestamp).toDate(),
        state = data["state"].toString()
    )
}