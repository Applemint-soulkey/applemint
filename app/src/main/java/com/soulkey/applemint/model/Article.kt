package com.soulkey.applemint.model
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp
import java.util.Date

@Entity(tableName = "tb_article")
data class Article(
    @PrimaryKey(autoGenerate = true)
    val id: Long?,
    val fb_id: String,
    val url: String,
    val type: String,
    val content: String,
    val timestamp: Date,
    var state: String,
    val source: String
){
    constructor(id: String, data: Map<String, Any>) : this(
        id = null,
        fb_id = id,
        url = data["url"].toString(),
        type = data["type"].toString(),
        content = data["textContent"].toString(),
        timestamp = (data["timestamp"] as Timestamp).toDate(),
        state = data["state"].toString(),
        source = data["crawlSource"].toString()
    )
}