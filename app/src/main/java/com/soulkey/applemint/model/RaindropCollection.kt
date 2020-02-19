package com.soulkey.applemint.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "tb_raindrop_collections")
data class RaindropCollection(
    @PrimaryKey
    @SerializedName("_id")
    val id: String,
    @SerializedName("title")
    val collectionName: String
)