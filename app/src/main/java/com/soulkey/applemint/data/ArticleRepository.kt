package com.soulkey.applemint.data

import com.google.android.gms.tasks.Task
import com.soulkey.applemint.model.Article

interface ArticleRepository {
    fun removeArticle(id: String): Task<Void>
    fun keepArticle(id: String): Task<Void>
    fun restoreArticle(item: Article): Task<Void>
}