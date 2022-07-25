package com.example.helpingminds.Activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.helpingminds.CalendarActivity
import com.example.helpingminds.Callback.AfterLoginActivityCallback
import com.example.helpingminds.Fragment.MenuFragment
import com.example.helpingminds.Fragment.SetReminderFragment
import com.example.helpingminds.Model.Event
import com.example.helpingminds.Model.User
import com.example.helpingminds.R
import com.example.helpingminds.Utility.Session.SessionManagement
import com.example.helpingminds.Utility.WorkManager.NotifyWorker
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class AfterLoginActivity : AppCompatActivity(), AfterLoginActivityCallback {
    val manager = supportFragmentManager
    private val NOTIFICATION_CHANNEL_ID = "10001"
    private val default_notification_channel_id = "default"
    private lateinit var sessionManagement: SessionManagement

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_after_login)

        val fragmentToStart = intent.extras?.getString("fragmentToStart")
        val transaction = manager.beginTransaction()
        if(fragmentToStart != null && fragmentToStart == "SetReminder"){
           val event = intent.extras?.getSerializable("event") as Event
            transaction.replace(R.id.body, SetReminderFragment(event))
        }
        else{
            transaction.replace(R.id.body, MenuFragment())
        }
        transaction.commit()

    }

    override fun switchToCalendarActivity() {
        val calendarActivityIntent = Intent(this, CalendarActivity::class.java)
        startActivity(calendarActivityIntent)
    }

    override fun onStart() {
        super.onStart()
        sessionManagement = SessionManagement(this)
    }


    override fun signOut() {
        val user = User(-1,"","")
        sessionManagement.saveSession(user)
        MoveToMainActivity()
    }

    override fun clearNotification(uuid: UUID) {
        WorkManager.getInstance(this).cancelWorkById(uuid)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun setNotification(date:String):UUID {

        //we set a tag to be able to cancel all work of this type if needed
        val workTag = "notificationWork"

        //store DBEventID to pass it to the PendingIntent and open the appropriate event page on notification click
        val inputData: Data = Data.Builder().putInt("Test", 1).build()

        // we then retrieve it inside the NotifyWorker with:
        // final int DBEventID = getInputData().getInt(DBEventIDTag, ERROR_VALUE);
        val notificationWork = OneTimeWorkRequest.Builder(NotifyWorker::class.java)
            .setInitialDelay(calculateDelay(date), TimeUnit.MILLISECONDS)
            .setInputData(inputData)
            .addTag(workTag)
            .build()

        var uuid = notificationWork.id

        WorkManager.getInstance(this).enqueue(notificationWork)

        return uuid
    }

    private fun calculateDelay(dateString:String): Long{
        var sdf:SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        val date: Date = sdf.parse(dateString.split("T")[0])
        val futureCal = Calendar.getInstance()
        futureCal.time = date
        futureCal.set(Calendar.HOUR_OF_DAY, 19)
        futureCal.set(Calendar.MINUTE, 55)

        val currentCal = Calendar.getInstance()

        return futureCal.timeInMillis - currentCal.timeInMillis
    }

    private fun MoveToMainActivity(){
        val switchActivityIntent = Intent(this, MainActivity::class.java)
        switchActivityIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(switchActivityIntent)
    }

}