package com.example.helpingminds.Utility.Session
import android.content.Context
import android.content.SharedPreferences
import com.example.helpingminds.Model.User

class SessionManagement {
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor:SharedPreferences.Editor
    val SHARED_PREF_NAME = "session"
    val SESSION_KEY = "session_user"

    constructor(context: Context){
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,  Context.MODE_PRIVATE);
        editor = sharedPreferences.edit()
    }

    fun saveSession(user: User){
        var id = user.userId
        editor.putInt(SESSION_KEY, id).commit()
    }

    fun getSession(): Int{
        return sharedPreferences.getInt(SESSION_KEY, -1);
    }
}