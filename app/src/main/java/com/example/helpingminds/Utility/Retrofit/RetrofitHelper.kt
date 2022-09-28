package com.example.helpingminds.Utility.Retrofit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
<<<<<<< HEAD
    val baseUrl = "http://192.168.1.70:80/"
=======
    val baseUrl = "http://rahulkawari-001-site1.atempurl.com/"
>>>>>>> f0a729018fe08d70d2a39ad3db3f79e93a22940d

    private val client = OkHttpClient.Builder().build()

    val retrofit by lazy {
        Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    fun<T> buildService(service: Class<T>): T{
        return retrofit.create(service)
    }
}