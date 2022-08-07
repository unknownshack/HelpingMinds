package com.example.helpingminds.Utility.WorkManager

import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.helpingminds.Utility.Retrofit.RestApiService


class NotifyWorker(context: Context, params: WorkerParameters) :
    Worker(context, params) {
    private val NOTIFICATION_CHANNEL_ID = "10001"
    private val default_notification_channel_id = "default"

    private var reminderId = 0

    private var context = context
    override fun doWork(): Result {
        Log.i("Rohit", "Testing")
        reminderId = inputData.getInt("ReminderId", 0)
        triggerNotification()
        var apiService = RestApiService()
        apiService.GetReminder(reminderId){
            if(it != null){
                var savedReminder = it
                savedReminder.completed = true
                apiService.UpdateReminder(reminderId, savedReminder){
                    if(it != null){

                    }
                }
            }
        }
        return Result.success()
    }

    private fun getNotification(): Notification{
        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(context, default_notification_channel_id)
        builder.setContentTitle("Scheduled Notification")
        builder.setContentText("Date")
        builder.setSmallIcon(R.drawable.menu_frame)
        builder.setAutoCancel(true)
        builder.setChannelId(NOTIFICATION_CHANNEL_ID)
        return builder.build()
    }

    private fun triggerNotification(){
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "NOTIFICATION_CHANNEL_NAME",
                importance
            )
            assert(notificationManager != null)
            notificationManager!!.createNotificationChannel(notificationChannel)
        }
        assert(notificationManager != null)
        notificationManager!!.notify(1, getNotification())
    }
}