package com.soulkey.applemint.common.raindrop

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class CreateRaindropResponse(
    @SerializedName("result")
    val result: Boolean,
    @SerializedName("item")
    val itemDetail: JsonObject
)

data class GetCollectionsResponse(
    @SerializedName("result")
    val result: Boolean,
    @SerializedName("items")
    val collections: JsonArray
)