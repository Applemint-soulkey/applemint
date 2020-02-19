package com.soulkey.applemint.ui.login

import android.content.Context
import androidx.lifecycle.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.iid.FirebaseInstanceId
import com.soulkey.applemint.common.raindrop.RaindropClient
import com.soulkey.applemint.data.RaindropCollectionRepository
import com.soulkey.applemint.data.UserRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class LoginViewModel(
    private val context: Context,
    private val db: FirebaseFirestore,
    private val userRepository: UserRepository,
    private val raindropCollectionRepository: RaindropCollectionRepository,
    private val raindropClient: RaindropClient
) : ViewModel() {
    private val isUserUpdated : MutableLiveData<Boolean> = MutableLiveData(false)
    val isCollectionUpdated: MutableLiveData<Boolean> = MutableLiveData(false)
    val isUpdateComplete : MediatorLiveData<Boolean> = MediatorLiveData()
    val updateProcess: MutableLiveData<String> = MutableLiveData("Connect to Server..")

    init {
        isUpdateComplete.addSource(isUserUpdated) {
            isUpdateComplete.value = isUserUpdated.value!! && isCollectionUpdated.value!!
        }
        isUpdateComplete.addSource(isCollectionUpdated){
            isUpdateComplete.value = isUserUpdated.value!! && isCollectionUpdated.value!!
        }
    }

    fun checkCollectionData() = raindropCollectionRepository.getCollections()

    fun loginProcess(email:String){
        db.collection("user").document(email).get().addOnSuccessListener { userDoc->
            userDoc.data?.let { data->
                FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
                    Observable.fromCallable { it.token }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext { token ->
                            Timber.v("diver:/ Step 1")
                            context.getSharedPreferences("currentUser", Context.MODE_PRIVATE).also {
                                it.edit().putString("email", email).apply()
                                userRepository.setCurrentUser(email, data["dapina"].toString(), token, data["raindrop"].toString())
                            }
                        }.flatMapSingle {
                            raindropClient.getCollections()
                        }.subscribe {response ->
                            if (response.isSuccessful){
                                response.body()?.collections?.let {
                                    raindropCollectionRepository.insertCollections(it)
                                    isUserUpdated.value = true
                                }
                            } else {
                                Timber.v("diver:/ Response Error:: ${response.errorBody()}")
                            }
                            Timber.v("diver:/ ")
                        }
                }
            }
        }
    }
}