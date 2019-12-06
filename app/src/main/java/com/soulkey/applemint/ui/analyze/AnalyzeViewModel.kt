package com.soulkey.applemint.ui.analyze

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import timber.log.Timber

class AnalyzeViewModel : ViewModel() {
    val targetTitle: MutableLiveData<String> by lazy {MutableLiveData<String>()}
    val mediaContents: MutableLiveData<List<String>> by lazy { MutableLiveData<List<String>>() }
    val externalLinks: MutableLiveData<List<String>> by lazy { MutableLiveData<List<String>>() }

    fun callAnalyze(fb_id: String) {
        Firebase.functions.getHttpsCallable("analyze").call(hashMapOf("id" to fb_id))
            .addOnCompleteListener {task->
                if (task.isSuccessful){
                    task.result?.let {analyzeResult->
                        val result = analyzeResult.data as HashMap<*, *>
                        Timber.v("diver:/ ${result["title"]}")
                        targetTitle.value = result["title"] as String?
                        mediaContents.value = result["midiContents"] as List<String>?
                        externalLinks.value = result["externalContents"] as List<String>?
                        Timber.v("diver:/ ${externalLinks.value}")
                    }
                } else {
                    Timber.v("diver:/ Analyze Call is Failed..")
                }
            }
    }
}