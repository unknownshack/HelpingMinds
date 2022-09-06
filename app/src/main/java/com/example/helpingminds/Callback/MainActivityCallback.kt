package com.example.helpingminds.Callback

interface MainActivityCallback {
    fun signInCallBack()
    fun adminLoginCallBack()
    fun SigningIn(username:String, password:String)
    fun createAdminLoginPage(username:String, password:String)
    fun moveToAdminMenu()
    fun createUser()
    fun createEvent()
}