package com.soulkey.applemint

import android.util.Log
import com.google.gson.JsonArray
import com.soulkey.applemint.model.network.RetrofitClient
import org.junit.Test

import org.junit.Assert.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun get_data_from_pepper(){
        val result : ArrayList<String> = ArrayList()
        val call = RetrofitClient.getInstance().buildRetrofit().getAll()
        call.enqueue(object : Callback<JsonArray> {
            override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                Log.d("retrofit", t.message)
            }
            override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
                val iter_data = response.body()
                iter_data?.forEach {
                    //Log.d("retrofit", it.asJsonObject.get("id").toString())

                    result.add(it.asJsonObject.get("id").toString())
                }
                Log.d("retrofit", result.toString())
                System.out.println(result)
            }
        })
    }
}
