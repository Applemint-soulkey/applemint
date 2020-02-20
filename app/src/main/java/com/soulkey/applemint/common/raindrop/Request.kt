package com.soulkey.applemint.common.raindrop

import com.google.gson.JsonObject

data class Raindrop (
    val title: String,
    val link: String,
    val tags: List<String>,
    val collection: JsonObject
)