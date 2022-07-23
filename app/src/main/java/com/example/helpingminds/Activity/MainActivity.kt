package com.example.helpingminds.Activity


import android.app.FragmentManager
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.helpingminds.*
import com.example.helpingminds.Callback.CallBackListener
import com.example.helpingminds.Fragment.AdminLoginFragment
import com.example.helpingminds.Fragment.HomePage
import com.example.helpingminds.Fragment.SignInFragment
import com.example.helpingminds.Model.User
import com.example.helpingminds.Utility.Retrofit.RestApiService
import com.example.helpingminds.Utility.Session.SessionManagement


class MainActivity : AppCompatActivity(), CallBackListener {
    val manager = supportFragmentManager
    private lateinit var sessionManagement: SessionManagement
    private lateinit var progress: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        initProgress()
        sessionManagement = SessionManagement(this)
        var userID = sessionManagement.getSession()

        if(userID != -1){
            MoveToAfterLoginActivity()
        }
        else{
            val transaction = manager.beginTransaction()
            transaction.replace(R.id.body, HomePage())
            transaction.commit()
        }
    }

    private fun initProgress(){
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


    override fun SigningIn(userName:String, password:String) {
        progress.show()
        val loginApiService = RestApiService()
        var user = User(-1, userName, password)

        loginApiService.checkLogin(user){
            if(it != null && it != -1){
                progress.setMessage("Logging In")
                user.userId = it!!
                sessionManagement.saveSession(user)

                MoveToAfterLoginActivity()
            }else{
                progress.dismiss()
                Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun MoveToAfterLoginActivity(){
        val switchActivityIntent = Intent(this, AfterLoginActivity::class.java)
        progress.dismiss()
        startActivity(switchActivityIntent)
        finish()
    }

    override fun onRestart() {
        super.onRestart()
        manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

}