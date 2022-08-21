package com.example.helpingminds.Activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.helpingminds.R
import com.example.helpingminds.Utility.CustomClass.Phone


class QuestionActivity : AppCompatActivity() {
    private lateinit var footerView: View
    private lateinit var footerText: TextView
    private lateinit var addressText: TextView

    private val sourceLatitude = -32.10728277687657
    private val sourceLongitude = 115.84990938158865

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)
    }

    override fun onStart() {
        super.onStart()
        footerView = findViewById(R.id.footer)
        footerText = findViewById(R.id.phoneNumber)
        addressText = findViewById(R.id.location)
        addressText.setOnClickListener {

            val uri: String =
                "geo:${sourceLatitude},${sourceLongitude}?q=${sourceLatitude},${sourceLongitude}(HelpingMinds)"

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            intent.setPackage("com.google.android.apps.maps")
            if (intent.resolveActivity(packageManager) != null)
                startActivity(intent)
            else
                Toast.makeText(this, "No activity", Toast.LENGTH_SHORT).show()
        }

        footerText.setOnClickListener {
            Phone.makeCall(this, footerText.text.split(":")[1])
        }
    }
}