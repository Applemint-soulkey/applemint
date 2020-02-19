package com.soulkey.applemint.ui.bookmark

import androidx.lifecycle.ViewModel
import com.soulkey.applemint.data.RaindropCollectionRepository

class BookmarkViewModel(private val raindropCollectionRepository: RaindropCollectionRepository) : ViewModel() {
    init {

    }

    fun loadCollections() = raindropCollectionRepository.getCollections()
}