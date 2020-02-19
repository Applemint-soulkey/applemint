package com.soulkey.applemint.data

import android.content.Context
import androidx.lifecycle.LiveData
import com.google.firebase.iid.FirebaseInstanceId
import com.soulkey.applemint.common.MessagingService
import com.soulkey.applemint.common.raindrop.RaindropClient
import com.soulkey.applemint.config.CurrentUser
import com.soulkey.applemint.db.UserDao
import com.soulkey.applemint.model.User
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class UserRepositoryImpl(
    private val userDao: UserDao,
    private val messageService: MessagingService,
    private val currentUser: CurrentUser
) : UserRepository {

    override fun setCurrentUser(email: String, dapina: String, message_token: String, raindrop_key: String) {
        User(email, dapina, message_token, raindrop_key).also {
            userDao.insert(it)
            currentUser.user = it
            messageService.sendToServer(email, message_token)
        }
    }
}