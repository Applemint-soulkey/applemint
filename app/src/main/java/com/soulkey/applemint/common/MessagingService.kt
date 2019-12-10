package com.soulkey.applemint.common

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService

class MessagingService(val context: Context, val db : FirebaseFirestore) : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        sendToServer(token)
    }

    fun sendToServer(token: String){
        context.getSharedPreferences("currentUser", Context.MODE_PRIVATE).getString("email", "undefined")?.let {email->
            db.collection("user").document(email).update("message_token", token)
        }
    }
}