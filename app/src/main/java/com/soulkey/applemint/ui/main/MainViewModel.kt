package com.soulkey.applemint.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class MainViewModel : ViewModel() {
    val db = FirebaseFirestore.getInstance()
    val isFilterOpen: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val isFilterApply: MutableLiveData<Boolean> = MutableLiveData()
}
