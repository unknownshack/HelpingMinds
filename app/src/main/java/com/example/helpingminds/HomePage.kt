package com.example.helpingminds

import android.os.Bundle
import android.support.v4.app.Fragment
import android.telecom.Call
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout

class HomePage : Fragment() {
    private lateinit var fragmentView: View;
    private lateinit var cb: CallBackListener;

    private lateinit var signInButton: Button;
    private lateinit var adminLoginButton: Button;
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_home_page, container, false)
        signInButton = fragmentView.findViewById(R.id.signin);
        adminLoginButton = fragmentView.findViewById(R.id.adminlogin);
        return fragmentView;
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if(activity is CallBackListener){
            cb = activity as CallBackListener;
        }

        signInButton.setOnClickListener{
            cb.signInCallBack();
        }

        adminLoginButton.setOnClickListener{
            cb.adminLoginCallBack();
        }

    }

}