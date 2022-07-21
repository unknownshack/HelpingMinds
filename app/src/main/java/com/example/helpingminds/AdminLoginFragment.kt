package com.example.helpingminds

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class AdminLoginFragment : Fragment() {
    private lateinit var adminLoginView:View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        adminLoginView = inflater.inflate(R.layout.fragment_admin_login, container, false)
        return adminLoginView
    }
}