package com.example.helpingminds.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.helpingminds.R

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