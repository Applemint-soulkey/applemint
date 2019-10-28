package com.soulkey.applemint.config

enum class itemType {
    BATTLEPAGE, DOGDRIP, IMGUR, YOUTUBE, FMKOREA, DIRECT, ETC
}

fun typeMapper(keyword: String): itemType {
    return when(keyword){
        "battlepage"-> itemType.BATTLEPAGE
        "dogdrip"-> itemType.DOGDRIP
        "imgur"-> itemType.IMGUR
        "youtube"-> itemType.YOUTUBE
        "fmkorea"-> itemType.FMKOREA
        "direct"-> itemType.DIRECT
        else-> itemType.ETC
    }
}