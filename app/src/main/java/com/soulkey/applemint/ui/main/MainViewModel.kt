package com.soulkey.applemint.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    val isFilterOpen: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val isFilterApply: MutableLiveData<Boolean> = MutableLiveData()
}
