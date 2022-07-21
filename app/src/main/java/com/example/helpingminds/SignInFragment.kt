package com.example.helpingminds

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

class SignInFragment : Fragment() {
    private lateinit var signInView: View
    private lateinit var cb:CallBackListener
    private lateinit var signInButton:Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        signInView = inflater.inflate(R.layout.fragment_sign_in, container, false)
        return signInView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if(activity is CallBackListener){
            cb = activity as CallBackListener
        }
        signInButton = signInView.findViewById(R.id.login)
        signInButton.setOnClickListener {
            cb.SigningIn()
        }
    }
}