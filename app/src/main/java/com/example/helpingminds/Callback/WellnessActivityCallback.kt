package com.example.helpingminds.Callback

import java.util.*

interface WellnessActivityCallback {
    fun setNotification(
        date: String,
        hour: Int,
        minute: Int,
        reminderId: Int,
        forWellnes: Boolean = false
    ): UUID;

    fun clearNotification(uuid: UUID)
}