package com.soulkey.applemint.config

import androidx.core.view.children
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.soulkey.applemint.R

fun typeTagMapper(type: String) : Int {
    return when(type) {
        "youtube" -> R.drawable.background_article_youtube_tag
        "twitch"-> R.drawable.background_article_twtich_tag
        "battlepage" -> R.drawable.background_article_battlepage_tag
        "dogdrip" -> R.drawable.background_article_dogdrip_tag
        "fmkorea" -> R.drawable.background_article_fmkorea_tag
        "direct" -> R.drawable.background_article_direct_tag
        "imgur" -> R.drawable.background_article_imgur_tag
        "twitter" -> R.drawable.background_article_twitter_tag
        "dcinside" -> R.drawable.background_article_dcinside_tag
        "ruliweb" -> R.drawable.background_article_ruliweb_tag
        else -> R.drawable.background_article_etc_tag
    }
}

fun getFilters(group: ChipGroup): List<String>  {
    return group.children
        .filter {(it as Chip).isChecked }
        .map {(it as Chip).text.toString() }
        .toList()
}