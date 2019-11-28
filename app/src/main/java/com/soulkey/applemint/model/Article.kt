package com.soulkey.applemint.model
import com.google.firebase.Timestamp
import java.util.Date

data class Article(
    val fb_id: String,
    val url: String,
    val type: String,
    val content: String,
    val timestamp: Date,
    var state: String,
    val source: String
){
    constructor(id: String, data: Map<String, Any>) : this(
        fb_id = id,
        url = data["url"].toString(),
        type = data["type"].toString(),
        content = data["textContent"].toString(),
        timestamp = (data["timestamp"] as Timestamp).toDate(),
        state = data["state"].toString(),
        source = data["crawlSource"].toString()
    )
}