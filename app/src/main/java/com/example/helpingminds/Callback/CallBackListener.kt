package com.example.helpingminds.Callback

interface CallBackListener {
    fun signInCallBack()
    fun adminLoginCallBack()
    fun SigningIn(username:String, password:String)
}