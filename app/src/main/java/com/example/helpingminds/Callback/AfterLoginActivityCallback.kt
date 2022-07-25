package com.example.helpingminds.Callback

import java.util.*

interface AfterLoginActivityCallback {
    fun switchToCalendarActivity();
    fun signOut();

    fun setNotification(date:String):UUID;

    fun clearNotification(uuid: UUID)
}