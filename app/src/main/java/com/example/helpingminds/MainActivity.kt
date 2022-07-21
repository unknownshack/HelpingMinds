package com.example.helpingminds

import android.annotation.SuppressLint
import android.app.FragmentManager
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity


class MainActivity : AppCompatActivity(), CallBackListener {
    val manager = supportFragmentManager

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.body, HomePage())
        transaction.commit()
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

    override fun SigningIn() {
        val switchActivityIntent = Intent(this, AfterLoginActivity::class.java)
        startActivity(switchActivityIntent)
    }

    override fun onRestart() {
        super.onRestart()
        manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

}