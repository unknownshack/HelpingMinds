package com.example.helpingminds.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.helpingminds.Callback.CallBackListener
import com.example.helpingminds.R

class AdminLoginFragment : Fragment() {
    private lateinit var adminLoginView:View
    private lateinit var createLogin:Button
    private lateinit var cb:CallBackListener
    private lateinit var username:EditText
    private lateinit var password:EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        adminLoginView = inflater.inflate(R.layout.fragment_admin_login, container, false)
        return adminLoginView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        createLogin = adminLoginView.findViewById(R.id.createlogin)
        username = adminLoginView.findViewById(R.id.email)
        password = adminLoginView.findViewById(R.id.password)

        if(activity is CallBackListener){
            cb = activity as CallBackListener
        }

        createLogin.setOnClickListener {
            cb.createAdminLoginPage(username.text.toString(), password.text.toString())
        }
    }
}