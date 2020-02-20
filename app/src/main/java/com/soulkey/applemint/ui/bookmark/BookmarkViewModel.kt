package com.soulkey.applemint.ui.bookmark

import androidx.lifecycle.ViewModel
import com.soulkey.applemint.common.raindrop.CreateRaindropResponse
import com.soulkey.applemint.common.raindrop.RaindropClient
import com.soulkey.applemint.data.RaindropCollectionRepository
import io.reactivex.Single
import retrofit2.Response

class BookmarkViewModel(private val raindropCollectionRepository: RaindropCollectionRepository, private val raindropClient: RaindropClient) : ViewModel() {
    fun loadCollections() = raindropCollectionRepository.getCollections()
    fun sendToRaindrop(title: String, url: String, collectionName: String, tags: List<String>): Single<Response<CreateRaindropResponse>> {
        val collectionId = raindropCollectionRepository.getIdByCollectionName(collectionName)
        return raindropClient.createRaindrop(title, url, tags, collectionId)
    }
}