package com.soulkey.applemint.common

import com.dropbox.core.DbxRequestConfig
import com.dropbox.core.v2.DbxClientV2
import com.soulkey.applemint.data.UserRepository

class Dapina (val userRepository: UserRepository){
    private lateinit var dapinaKey: String

    fun setDapinaKey(email: String){
        dapinaKey = userRepository.getDapinaKey(email)
    }

    fun getDapinaClient(): DbxClientV2{
        DbxRequestConfig.newBuilder("dropbox/dapina").build().also { config->
            return DbxClientV2(config, dapinaKey)
        }
    }
}