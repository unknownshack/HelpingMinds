package com.example.helpingminds.Utility.Retrofit

import com.example.helpingminds.Model.Event
import com.example.helpingminds.Model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestApiService {
    private val retrofit = RetrofitHelper.buildService(LoginAPI::class.java)
    fun checkLogin(user: User, onResult: (Int?) -> Unit){
        retrofit.LogIn(user).enqueue(
            object : Callback<Int> {
                override fun onResponse(call: Call<Int>?, response: Response<Int>?) {
                    val id = response?.body()
                    onResult(id)
                }

                override fun onFailure(call: Call<Int>?, t: Throwable?) {
                    onResult(null)
                }

            }
        )
    }

    fun checkAdminLogin(user:User, onResult:(Int?) -> Unit){
        retrofit.AdminLogIn(user).enqueue(
            object:Callback<Int>{
                override fun onResponse(call: Call<Int>?, response: Response<Int>?) {
                    var id = response?.body()
                    onResult(id)
                }

                override fun onFailure(call: Call<Int>?, t: Throwable?) {
                    onResult(null)
                }

            }
        )
    }

    fun getEvents(onResult: (ArrayList<Event>?) -> Unit){
        retrofit.GetEvents().enqueue(
            object : Callback<ArrayList<Event>> {
                override fun onResponse(
                    call: Call<ArrayList<Event>>?,
                    response: Response<ArrayList<Event>>?
                ) {
                    onResult(response?.body())
                }

                override fun onFailure(call: Call<ArrayList<Event>>?, t: Throwable?) {
                    onResult(null)
                }

            }
        )
    }

    fun getDateEvents(date: String, onResult: (ArrayList<Event>?) -> Unit){
        retrofit.GetDateEvents(date).enqueue(
            object : Callback<ArrayList<Event>> {
                override fun onResponse(
                    call: Call<ArrayList<Event>>?,
                    response: Response<ArrayList<Event>>?
                ) {
                    onResult(response?.body())
                }

                override fun onFailure(call: Call<ArrayList<Event>>?, t: Throwable?) {
                    onResult(null)
                }

            }
        )
    }
}