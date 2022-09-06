package com.example.helpingminds.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.helpingminds.Callback.MainActivityCallback
import com.example.helpingminds.R

class SignInFragment : Fragment() {
    private lateinit var signInView: View
    private lateinit var cb: MainActivityCallback
    private lateinit var signInButton:Button

    private lateinit var email: EditText
    private lateinit var password: EditText

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
        if(activity is MainActivityCallback){
            cb = activity as MainActivityCallback
        }
        email = signInView.findViewById(R.id.email)
        password = signInView.findViewById(R.id.password)
        signInButton = signInView.findViewById(R.id.login)
        signInButton.setOnClickListener {
            cb.SigningIn(email.text.toString(), password.text.toString())
        }
    }
}