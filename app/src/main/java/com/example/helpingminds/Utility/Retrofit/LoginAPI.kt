package com.example.helpingminds.Utility.Retrofit

import com.example.helpingminds.Model.Event
import com.example.helpingminds.Model.Reminder
import com.example.helpingminds.Model.User
import retrofit2.Call
import retrofit2.http.*

interface LoginAPI {
    @Headers("Content-Type: application/json")
    @POST("/helpingminds/api/Users")
    fun LogIn(@Body user: User?): Call<Int>

    @Headers("Content-Type: application/json")
    @POST("/helpingminds/api/Users/AdminLogin")
    fun AdminLogIn(@Body user: User?): Call<Void>

    @Headers("Content-Type: application/json")
    @POST("/helpingminds/api/Users/Create")
    fun CreateUser(@Body user: User): Call<User>

    @Headers("Content-Type: application/json")
    @POST("/helpingminds/Events")
    fun CreateEvent(@Body event: Event): Call<Event>

    @Headers("Content-Type: application/json")
    @GET("/helpingminds/Events")
    fun GetEvents(): Call<ArrayList<Event>>

    @Headers("Content-Type: application/json")
    @GET("/helpingminds/Events/id={date}")
    fun GetDateEvents(@Path("date") date: String): Call<ArrayList<Event>>

    @GET("/helpingminds/api/Reminders/eventId/{id}")
    fun checkIfReminderExist(@Path("id") id:Int): Call<ArrayList<Reminder>>

    @POST("/helpingminds/api/Reminders")
    fun saveReminder(@Body reminder: Reminder): Call<Reminder>

    @DELETE("/helpingminds/api/Reminders/{id}")
    fun deleteReminder(@Path("id") id:Int): Call<Any>

    @GET("/helpingminds/api/Users")
    fun getUser(): Call<List<User>>

    @PUT("/helpingminds/api/Reminders/{id}")
    fun updateReminder(@Path("id") id:Int, @Body reminder:Reminder): Call<Reminder>

    @GET("/helpingminds/api/Reminders/{id}")
    fun getReminder(@Path("id") id:Int): Call<Reminder>

}

