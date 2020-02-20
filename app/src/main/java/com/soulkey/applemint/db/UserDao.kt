package com.soulkey.applemint.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.soulkey.applemint.model.User
import io.reactivex.Single

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)
    @Query("select * from tb_users where email=:email")
    fun getUser(email: String?): LiveData<User>
}