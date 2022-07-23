package com.example.helpingminds.Utility.Retrofit

import com.example.helpingminds.Model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface LoginAPI {
    @Headers("Content-Type: application/json")
    @POST("/helpingminds/api/Users")
    fun LogIn(@Body user: User?): Call<Int>

    @GET("/helpingminds/api/Users")
    fun getUser(): Call<List<User>>
}

