package com.soulkey.applemint.ui.login

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    private val isArticleUpdated : MutableLiveData<Boolean> = MutableLiveData(false)
    private val isBookmarkUpdated : MutableLiveData<Boolean> = MutableLiveData(true)
    val isUpdateComplete : MediatorLiveData<Boolean> = MediatorLiveData()
    val updateProcess: MutableLiveData<String> = MutableLiveData("Connect to Server..")

    init {
        isUpdateComplete.addSource(isArticleUpdated) {
            isUpdateComplete.value = isArticleUpdated.value!! && isBookmarkUpdated.value!!
        }
        isUpdateComplete.addSource(isBookmarkUpdated) {
            isUpdateComplete.value = isArticleUpdated.value!! && isBookmarkUpdated.value!!
        }
    }
}