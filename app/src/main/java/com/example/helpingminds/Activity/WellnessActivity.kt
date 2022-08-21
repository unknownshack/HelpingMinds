package com.example.helpingminds.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.helpingminds.Callback.AfterLoginActivityCallback
import com.example.helpingminds.Callback.WellnessActivityCallback
import com.example.helpingminds.Fragment.MenuFragment
import com.example.helpingminds.Fragment.SetReminderFragment
import com.example.helpingminds.Fragment.WellnessMenuFragment
import com.example.helpingminds.Model.Event
import com.example.helpingminds.R
import com.example.helpingminds.Utility.WorkManager.NotifyWorker
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class WellnessActivity : AppCompatActivity(), WellnessActivityCallback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wellness)
    }

    override fun onStart() {
        super.onStart()

        initFragment()

    }

    private fun initFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.body, WellnessMenuFragment())
        transaction.commit()
    }

    override fun clearNotification(uuid: UUID) {
        WorkManager.getInstance(this).cancelWorkById(uuid)
    }

    override fun setNotification(date: String, hour: Int, minute: Int, reminderId: Int, forWellness: Boolean): UUID {
        //we set a tag to be able to cancel all work of this type if needed
        val workTag = "notificationWork"

        //store DBEventID to pass it to the PendingIntent and open the appropriate event page on notification click
        val inputData: Data = Data.Builder().putInt("ReminderId", reminderId).build()

        // we then retrieve it inside the NotifyWorker with:
        // final int DBEventID = getInputData().getInt(DBEventIDTag, ERROR_VALUE);
        val notificationWork = OneTimeWorkRequest.Builder(NotifyWorker::class.java)
            .setInitialDelay(calculateDelay(date, hour, minute, forWellness), TimeUnit.MILLISECONDS)
            //.setInitialDelay(10000, TimeUnit.MILLISECONDS)
            .setInputData(inputData)
            .addTag(workTag)
            .build()

        var uuid = notificationWork.id

        WorkManager.getInstance(this).enqueue(notificationWork)

        return uuid
    }

    private fun calculateDelay(dateString: String, hour: Int, minute: Int, addDayDelay: Boolean): Long {
        var sdf: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        val date: Date = sdf.parse(dateString.split("T")[0])
        val futureCal = Calendar.getInstance()
        futureCal.time = date
        futureCal.set(Calendar.HOUR_OF_DAY, hour)
        futureCal.set(Calendar.MINUTE, minute)

        val currentCal = Calendar.getInstance()
        if(futureCal.timeInMillis - currentCal.timeInMillis < 0 && addDayDelay){
            futureCal.add(Calendar.DAY_OF_MONTH, 1)
        }

        return futureCal.timeInMillis - currentCal.timeInMillis
    }
}