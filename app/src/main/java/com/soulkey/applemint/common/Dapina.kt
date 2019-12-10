package com.soulkey.applemint.common

import com.dropbox.core.DbxRequestConfig
import com.dropbox.core.v2.DbxClientV2

class Dapina {
    private lateinit var dapinaKey: String

    fun setDapinaKey(key: String){
        dapinaKey = key
    }

    fun getDapinaClient(): DbxClientV2{
        DbxRequestConfig.newBuilder("dropbox/dapina").build().also { config->
            return DbxClientV2(config, dapinaKey)
        }
    }
}