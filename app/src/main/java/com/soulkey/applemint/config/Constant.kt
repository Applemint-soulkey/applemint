package com.soulkey.applemint.config

import android.view.View
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

fun getCheckedFilter(view: View?): List<String>{
    val filterList = mutableListOf<String>()
    view?.let {
        if (it.chip_filter_battlepage.isChecked) filterList.add("battlepage")
        if (it.chip_filter_dogdrip.isChecked) filterList.add("dogdrip")
        if (it.chip_filter_fmkorea.isChecked) filterList.add("fmkorea")
        if (it.chip_filter_direct.isChecked) filterList.add("direct")
        if (it.chip_filter_etc.isChecked) filterList.add("etc")
        if (it.chip_filter_imgur.isChecked) filterList.add("imgur")
        if (it.chip_filter_twtich.isChecked) filterList.add("twitch")
        if (it.chip_filter_youtube.isChecked) filterList.add("youtube")
    }
    return filterList
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