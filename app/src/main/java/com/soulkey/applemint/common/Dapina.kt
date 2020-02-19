package com.soulkey.applemint.common

import com.dropbox.core.DbxRequestConfig
import com.dropbox.core.v2.DbxClientV2
import com.soulkey.applemint.config.CurrentUser

class Dapina(private val currentUser: CurrentUser) {
    fun getDapinaClient(): DbxClientV2{
        DbxRequestConfig.newBuilder("dropbox/dapina").build().also { config->
            return DbxClientV2(config, currentUser.getDapinaKey())
        }
    }
}