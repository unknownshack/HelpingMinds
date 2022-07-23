package com.example.helpingminds.Utility.Retrofit

import com.example.helpingminds.Model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestApiService {
    fun checkLogin(user: User, onResult: (Int?) -> Unit){
        val retrofit = RetrofitHelper.buildService(LoginAPI::class.java)
        retrofit.LogIn(user).enqueue(
            object : Callback<Int> {
                override fun onResponse(call: Call<Int>?, response: Response<Int>) {
                    val id = response.body()
                    onResult(id)
                }

                override fun onFailure(call: Call<Int>?, t: Throwable?) {
                    onResult(null)
                }

            }
        )
    }
}