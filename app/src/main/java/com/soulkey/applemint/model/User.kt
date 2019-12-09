package com.soulkey.applemint.model

import androidx.room.Entity
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey

@Entity(tableName = "tb_users")
data class User(
    @PrimaryKey
    var email: String,
    var dapina_key: String
)