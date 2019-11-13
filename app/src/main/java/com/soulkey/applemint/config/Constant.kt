package com.soulkey.applemint.config

import android.view.View
import androidx.core.view.children
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.soulkey.applemint.R
import kotlinx.android.synthetic.main.view_chip_group_type.view.*

enum class itemType {
    BATTLEPAGE, DOGDRIP, IMGUR, YOUTUBE, FMKOREA, DIRECT, ETC
}

fun typeTagMapper(type: String) : Int {
    return when(type) {
        "youtube" -> R.drawable.background_article_youtube_tag
        "twitch"-> R.drawable.background_article_twtich_tag
        "battlepage" -> R.drawable.background_article_battlepage_tag
        "dogdrip" -> R.drawable.background_article_dogdrip_tag
        "fmkorea" -> R.drawable.background_article_fmkorea_tag
        "direct" -> R.drawable.background_article_direct_tag
        "imgur" -> R.drawable.background_article_imgur_tag
        else -> R.drawable.background_article_etc_tag
    }
}

fun getFilters(group: ChipGroup): List<String>  {
    return group.children
        .filter {(it as Chip).isChecked }
        .map {(it as Chip).text.toString() }
        .toList()
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