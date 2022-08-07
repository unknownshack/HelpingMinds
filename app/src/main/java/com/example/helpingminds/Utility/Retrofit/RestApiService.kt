package com.example.helpingminds.Utility.Retrofit

import com.example.helpingminds.Model.Event
import com.example.helpingminds.Model.Reminder
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

    fun checkAdminLogin(user:User, onResult:(Boolean?) -> Unit){
        retrofit.AdminLogIn(user).enqueue(
            object:Callback<Void>{
                override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                    onResult(response?.isSuccessful)
                }

                override fun onFailure(call: Call<Void>?, t: Throwable?) {
                    onResult(null)
                }


            }
        )
    }

    fun createUser(user:User, onResult: (User?) -> Unit){
        retrofit.CreateUser(user).enqueue(
            object:Callback<User>{
                override fun onResponse(call: Call<User>?, response: Response<User>?) {
                    onResult(response?.body())
                }

                override fun onFailure(call: Call<User>?, t: Throwable?) {
                    onResult(null)
                }

            }
        )
    }

    fun createEvents(event: Event, onResult: (Event?) -> Unit){
        retrofit.CreateEvent(event).enqueue(
            object : Callback<Event> {
                override fun onResponse(call: Call<Event>?, response: Response<Event>?) {
                    onResult(response?.body())
                }

                override fun onFailure(call: Call<Event>?, t: Throwable?) {
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

    fun checkIfReminderExist(id: Int, onResult: (ArrayList<Reminder>?) -> Unit){
        retrofit.checkIfReminderExist(id).enqueue(
            object : Callback<ArrayList<Reminder>> {
                override fun onResponse(
                    call: Call<ArrayList<Reminder>>?,
                    response: Response<ArrayList<Reminder>>?
                ) {
                    onResult(response?.body())
                }

                override fun onFailure(call: Call<ArrayList<Reminder>>?, t: Throwable?) {
                    onResult(null)
                }
            }
        )
    }

    fun saveReminder(reminder: Reminder, onResult: (Reminder?) -> Unit){
        retrofit.saveReminder(reminder).enqueue(
            object : Callback<Reminder> {
                override fun onResponse(call: Call<Reminder>?, response: Response<Reminder>?) {
                    onResult(response?.body())
                }

                override fun onFailure(call: Call<Reminder>?, t: Throwable?) {
                    onResult(null)
                }

            }
        )
    }

    fun DeleteReminder(id:Int, onResult: (Boolean?) -> Unit){
        retrofit.deleteReminder(id).enqueue(
            object : Callback<Any> {
                override fun onResponse(call: Call<Any>?, response: Response<Any>?) {
                    onResult(response?.isSuccessful)
                }

                override fun onFailure(call: Call<Any>?, t: Throwable?) {
                    onResult(null)
                }
            }
        )
    }

    fun UpdateReminder(id:Int, reminder: Reminder, onResult: (Boolean?) -> Unit){
        retrofit.updateReminder(id, reminder).enqueue(
            object : Callback<Reminder> {
                override fun onResponse(call: Call<Reminder>?, response: Response<Reminder>?) {
                    onResult(response?.isSuccessful)
                }

                override fun onFailure(call: Call<Reminder>?, t: Throwable?) {
                    onResult(null);
                }

            }
        )
    }

    fun GetReminder(id:Int, onResult: (Reminder?) -> Unit){
        retrofit.getReminder(id).enqueue(
            object : Callback<Reminder> {
                override fun onResponse(call: Call<Reminder>?, response: Response<Reminder>?) {
                    onResult(response?.body())
                }

                override fun onFailure(call: Call<Reminder>?, t: Throwable?) {
                    onResult(null);
                }

            }
        )
    }
}