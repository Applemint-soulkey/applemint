package com.soulkey.applemint.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "tb_item")
data class Item (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "fb_id")
    val fb_id: String,

    @ColumnInfo(name = "url")
    val url: String,

    @ColumnInfo(name = "text_content")
    val text_content: String,

    @ColumnInfo(name = "type")
    val type: String,

    @ColumnInfo(name = "timestamp")
    val timestamp: Date
)