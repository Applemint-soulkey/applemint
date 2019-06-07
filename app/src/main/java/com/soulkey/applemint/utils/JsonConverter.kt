package com.soulkey.applemint.utils

import androidx.room.TypeConverter
import com.google.gson.Gson

class JsonConverter {
    companion object {
        @TypeConverter
        @JvmStatic
        fun listToJson(value: ArrayList<String>?): String {
            return Gson().toJson(value)
        }

        @TypeConverter
        @JvmStatic
        fun jsonToList(value: String): ArrayList<String>? {
            val list = ArrayList<String>()
            val objects = Gson().fromJson(value, Array<String>::class.java) as Array<String>
            for (item in objects){
                list.add(item)
            }
            return list
        }
    }
}