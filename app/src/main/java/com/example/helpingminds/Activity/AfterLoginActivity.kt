package com.example.helpingminds.Activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.helpingminds.*
import com.example.helpingminds.Callback.AfterLoginActivityCallback
import com.example.helpingminds.Fragment.MenuFragment
import com.example.helpingminds.Model.User
import com.example.helpingminds.Utility.Session.SessionManagement

class AfterLoginActivity : AppCompatActivity(), AfterLoginActivityCallback {
    val manager = supportFragmentManager
    private lateinit var sessionManagement: SessionManagement
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_after_login)
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.body, MenuFragment())
        transaction.commit()
    }

    override fun switchToCalendarActivity() {
        val calendarActivityIntent = Intent(this, CalendarActivity::class.java)
        startActivity(calendarActivityIntent)
    }

    override fun onStart() {
        super.onStart()
        sessionManagement = SessionManagement(this)
    }

    override fun signOut() {
        val user = User(-1,"","")
        sessionManagement.saveSession(user)
        MoveToMainActivity()
    }

    private fun MoveToMainActivity(){
        val switchActivityIntent = Intent(this, MainActivity::class.java)
        switchActivityIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(switchActivityIntent)
    }

}