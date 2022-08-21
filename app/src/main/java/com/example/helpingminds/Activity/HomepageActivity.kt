package com.example.helpingminds.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.example.helpingminds.R
import com.example.helpingminds.Utility.CustomClass.Phone

class HomepageActivity : AppCompatActivity() {
    private lateinit var footerView: View
    private lateinit var footerText: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)
    }

    override fun onStart() {
        super.onStart()

        footerView = findViewById(R.id.footer)
        footerText = findViewById(R.id.phoneNumber)

        footerText.setOnClickListener {
            Phone.makeCall(this, footerText.text.split(":")[1])
        }

    }
}