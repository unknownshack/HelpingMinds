package com.example.helpingminds.Activity


import android.app.FragmentManager
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.helpingminds.*
import com.example.helpingminds.Callback.MainActivityCallback
import com.example.helpingminds.Fragment.*
import com.example.helpingminds.Model.User
import com.example.helpingminds.Utility.CustomClass.Phone
import com.example.helpingminds.Utility.CustomClass.UserInfo
import com.example.helpingminds.Utility.Retrofit.RestApiService
import com.example.helpingminds.Utility.Session.SessionManagement
import kotlinx.coroutines.awaitAll


class MainActivity : AppCompatActivity(), MainActivityCallback {
    val manager = supportFragmentManager
    private lateinit var sessionManagement: SessionManagement
    private lateinit var progress: ProgressDialog
    private lateinit var footerView: View
    private lateinit var footerPhone: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()

        initViewAndListener()
        initProgress()
        sessionManagement = SessionManagement(this)
        var userID = sessionManagement.getSession()

        if (userID != -1) {
            UserInfo.userId = userID
            MoveToAfterLoginActivity()
        } else {
            val transaction = manager.beginTransaction()
            transaction.replace(R.id.body, HomePage())
            transaction.commit()
        }
    }

    private fun initViewAndListener() {
        footerView = findViewById(R.id.footer)
        footerPhone = footerView.findViewById(R.id.phoneNumber)

        footerPhone.setOnClickListener {
            Phone.makeCall(this, footerPhone.text.split(":")[1])
        }
    }

    private fun initProgress() {
        progress = ProgressDialog(this)
        progress.setTitle("Processing...")
        progress.setMessage("Please wait...")
        progress.setCancelable(false)
        progress.isIndeterminate = true
    }

    override fun signInCallBack() {
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.body, SignInFragment())
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun adminLoginCallBack() {
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.body, AdminLoginFragment())
        transaction.addToBackStack(null)
        transaction.commit()
    }


    override fun SigningIn(userName: String, password: String) {
        progress.show()
        val loginApiService = RestApiService()
        var user = User(-1, userName, password)
        user.email = userName

        loginApiService.checkLogin(user) {
            if (it != null && it != -1) {
                progress.setMessage("Logging In")
                user.userId = it!!
                UserInfo.userId = it!!
                sessionManagement.saveSession(user)

                MoveToAfterLoginActivity()
            } else {
                progress.dismiss()
                Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun createAdminLoginPage(username: String, password: String) {
        progress.show()
        val loginApiService = RestApiService()
        val user = User(-1, username, password)
        user.email = username

        loginApiService.checkAdminLogin(user) {
            if (it != true) {
                Toast.makeText(this, "Admin Login Failed", Toast.LENGTH_SHORT).show()
            } else {
                MoveToMenu()
            }
            progress.dismiss()
        }

    }

    override fun moveToAdminMenu() {

    }

    override fun createEvent() {
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.body, CreateEventFragment())
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun createUser() {
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.body, CreateUserFragment())
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun MoveToAfterLoginActivity() {
        val switchActivityIntent = Intent(this, AfterLoginActivity::class.java)
        progress.dismiss()
        startActivity(switchActivityIntent)
        finish()
    }

    private fun MoveToMenu() {
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.body, AfterAdminLoginFragment())
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onRestart() {
        super.onRestart()
        manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

}