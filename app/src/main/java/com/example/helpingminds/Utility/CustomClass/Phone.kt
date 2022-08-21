package com.example.helpingminds.Utility.CustomClass

import android.content.Context
import android.content.Intent
import android.net.Uri

class Phone {
    companion object {
        fun makeCall(context: Context, text: String) {
            val callIntent = Intent(Intent.ACTION_DIAL)
            callIntent.data = Uri.parse("tel:$text")
            context.startActivity(callIntent)
        }
    }
}