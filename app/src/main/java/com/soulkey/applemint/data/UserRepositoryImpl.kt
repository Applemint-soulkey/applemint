package com.soulkey.applemint.data

import com.soulkey.applemint.db.UserDao
import com.soulkey.applemint.model.User

class UserRepositoryImpl(val userDao: UserDao) : UserRepository{
    override fun insert(email: String, dapina: String) {
        userDao.insert(User(email = email, dapina_key = dapina))
    }

    override fun getDapinaKey(email: String): String {
        userDao.getUser(email).also {user->
            return user.dapina_key
        }
    }
}