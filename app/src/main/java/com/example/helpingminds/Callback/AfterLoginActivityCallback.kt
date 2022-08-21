package com.example.helpingminds.Callback

import java.util.*

interface AfterLoginActivityCallback {
    fun switchToCalendarActivity();
    fun signOut();

    fun setNotification(date:String, hour:Int, minute:Int, reminderId:Int):UUID;

    fun switchToHomePageActivity()

    fun switchToImportantEventActivity()

    fun switchToQuestionAboutAppActivity()

    fun switchToWellnessActivity()

    fun switchToManageReminderActivity()

    fun clearNotification(uuid: UUID)
}