package com.soulkey.applemint.model.article

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "article")
data class Article(
    @PrimaryKey(autoGenerate = true)
    var id: Long?,

    @ColumnInfo(name = "firebase_id")
    var firebase_id: String,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "text")
    var text: String,

    @ColumnInfo(name = "url")
    var url: String,

    @ColumnInfo(name = "domain")
    var domain: String,

    @ColumnInfo(name = "isBookmarked")
    var bookmark: Boolean,

    @ColumnInfo(name = "content_urls")
    var content_urls: ArrayList<String>,

    @ColumnInfo(name = "external_urls")
    var external_urls: ArrayList<String>,

    @ColumnInfo(name = "tags")
    var tags: ArrayList<String>
) {
    constructor(): this(
        null,
        "",
        "",
        "",
        "",
        "",
        false,
        content_urls=ArrayList(),
        external_urls=ArrayList(),
        tags=ArrayList()
    )
}

