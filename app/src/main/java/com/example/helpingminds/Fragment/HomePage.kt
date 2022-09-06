package com.example.helpingminds.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.helpingminds.Callback.MainActivityCallback
import com.example.helpingminds.R

class HomePage : Fragment() {
    private lateinit var fragmentView: View;
    private lateinit var cb: MainActivityCallback;

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
        if(activity is MainActivityCallback){
            cb = activity as MainActivityCallback;
        }

        signInButton.setOnClickListener{
            cb.signInCallBack();
        }

        adminLoginButton.setOnClickListener{
            cb.adminLoginCallBack();
        }

    }

}