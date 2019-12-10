package com.soulkey.applemint.data

import android.content.Context
import com.google.firebase.iid.FirebaseInstanceId
import com.soulkey.applemint.common.Dapina
import com.soulkey.applemint.common.MessagingService
import com.soulkey.applemint.db.UserDao
import com.soulkey.applemint.model.User

class UserRepositoryImpl(private val userDao: UserDao, private val dapina: Dapina, private val messageService: MessagingService, private val context: Context) : UserRepository{
    override fun insert(email: String, dapina: String, message_token: String) {
        userDao.insert(User(email = email, dapina_key = dapina, message_token = message_token))
    }

    override fun setCurrentUser(email: String, dapinaKey: String) {
        context.getSharedPreferences("currentUser", Context.MODE_PRIVATE).also {
            it.edit().putString("email", email).apply()
        }
        dapina.setDapinaKey(dapinaKey)
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            messageService.sendToServer(it.token)
            insert(email, dapinaKey, it.token)
        }
    }
}