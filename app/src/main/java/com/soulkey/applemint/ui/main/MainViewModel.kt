package com.soulkey.applemint.ui.main

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.soulkey.applemint.config.CurrentUser

class MainViewModel(private val currentUser: CurrentUser) : ViewModel() {
    lateinit var currentFragment: Fragment
    val isFilterOpen: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val isFilterApply: MutableLiveData<Boolean> = MutableLiveData()

    fun logout(){
        currentUser.user = null
    }
}
