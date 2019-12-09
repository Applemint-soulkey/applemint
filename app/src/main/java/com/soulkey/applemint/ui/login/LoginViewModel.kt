package com.soulkey.applemint.ui.login

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.soulkey.applemint.common.Dapina
import com.soulkey.applemint.data.UserRepository

class LoginViewModel(
    private val db: FirebaseFirestore,
    private val userRepository: UserRepository,
    private val dapina: Dapina
) : ViewModel() {
    private val isUserUpdated : MutableLiveData<Boolean> = MutableLiveData(false)
    val isUpdateComplete : MediatorLiveData<Boolean> = MediatorLiveData()
    val updateProcess: MutableLiveData<String> = MutableLiveData("Connect to Server..")

    init {
        isUpdateComplete.addSource(isUserUpdated) {
            isUpdateComplete.value = isUserUpdated.value!!
        }
    }

    fun loginProcess(email:String){
        db.collection("user").document(email).get().addOnSuccessListener { userDoc->
            userDoc.data?.let { data->
                userRepository.insert(email, data["dapina"].toString())
                dapina.setDapinaKey(email)
                isUserUpdated.value = true
            }
        }
    }
}