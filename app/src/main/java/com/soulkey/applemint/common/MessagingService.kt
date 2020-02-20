package com.soulkey.applemint.common

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService

class MessagingService(val db : FirebaseFirestore) : FirebaseMessagingService() {
    private lateinit var currentUserEmail: String

    override fun onNewToken(token: String) {
        sendToServer(currentUserEmail, token)
    }

    fun sendToServer(email: String?, token: String){
        email?.let {
            currentUserEmail = it
            db.collection("user").document(email).update("message_token", token)
        }
    }
}