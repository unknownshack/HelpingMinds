package com.example.helpingminds.Activity

import android.app.ProgressDialog
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.helpingminds.Model.Event
import com.example.helpingminds.Model.Reminder
import com.example.helpingminds.R
import com.example.helpingminds.Utility.CustomClass.CustomDate
import com.example.helpingminds.Utility.CustomClass.UserInfo
import com.example.helpingminds.Utility.Retrofit.RestApiService
import com.example.helpingminds.Utility.WorkManager.NotifyWorker
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class ManageReminderActivity : AppCompatActivity() {
    private lateinit var noReminderView: LinearLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter: ManageReminderRecycleViewAdapter
    private lateinit var reminderList: ArrayList<Reminder>
    private lateinit var mProgressBar: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_reminder)
    }

    override fun onStart() {
        super.onStart()
        InitProgressBar()
        InitViewAndListener()
    }

    private fun InitViewAndListener() {
        noReminderView = findViewById(R.id.no_event)

        reminderList = ArrayList<Reminder>()
        recyclerView = findViewById(R.id.list)
        recyclerAdapter = ManageReminderRecycleViewAdapter(reminderList, this)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = recyclerAdapter

        showProgressBarForRetrieving()
        var apiService = RestApiService()
        apiService.GetAllReminder {
            reminderList.clear()
            if (it != null) {
                var filteredReminder = it.filter { x -> x.userId == UserInfo.userId }
                if (filteredReminder.isNotEmpty()) {
                    noReminderView.visibility = View.GONE
                    reminderList.addAll(filteredReminder)
                } else {
                    noReminderView.visibility = View.VISIBLE
                }

            } else {
                noReminderView.visibility = View.VISIBLE
            }
            recyclerAdapter.notifyDataSetChanged()
            mProgressBar.dismiss()
        }
    }

    private fun showProgressBarForRetrieving() {
        mProgressBar.setTitle("Retrieving.. ")
        mProgressBar.setMessage("Please Wait.. ")
        mProgressBar.show()
    }

    private fun InitProgressBar() {
        mProgressBar = ProgressDialog(this)
        mProgressBar.setTitle("Retrieving...")
        mProgressBar.setMessage("Please Wait...")
        mProgressBar.setCancelable(false)
        mProgressBar.isIndeterminate = true
    }

    fun clearNotification(uuid: UUID) {
        WorkManager.getInstance(this).cancelWorkById(uuid)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun setNotification(date: String, hour: Int, minute: Int, reminderId: Int, forWellness: Boolean = false): UUID {

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

    fun showProgressBarForUpdating() {
        mProgressBar.setTitle("Updating...")
        mProgressBar.setMessage("Please Wait...")
        mProgressBar.show()
    }

    fun dismissProgressBar() {
        mProgressBar.dismiss()
    }
}

class ManageReminderRecycleViewAdapter(
    private val lst: ArrayList<Reminder>,
    context: Context
) : RecyclerView.Adapter<ManageReminderAdapterHolder>() {
    val activityContext: Context = context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManageReminderAdapterHolder {
        val inflatedView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.reminder_display_list, parent, false)
        return ManageReminderAdapterHolder(inflatedView)
    }

    override fun getItemCount(): Int {
        return lst.size
    }

    override fun onBindViewHolder(holder: ManageReminderAdapterHolder, position: Int) {
        val holder = holder as ManageReminderAdapterHolder
        holder.setUpViews(lst[position])
        holder.setUpListener(lst[position], activityContext)
    }

}

class ManageReminderAdapterHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var view: View = itemView
    private val descriptionText: TextView = itemView.findViewById(R.id.descriptionText)
    private val switch: Switch = itemView.findViewById(R.id.switchtoggle)
    private val timeText: TextView = itemView.findViewById(R.id.time)
    private var setHour: Int = 0
    private var setMinute: Int = 0

    private lateinit var event: Event


    fun setUpViews(reminder: Reminder) {
        descriptionText.text = reminder.nonEventNote
        val (hour, time, formattedTime) = CustomDate.getTimeFromDate(reminder.reminderDate)
        timeText.text = formattedTime
        setHour = hour
        setMinute = time
        switch.isChecked = reminder.isonoroff == true
    }

    fun setUpListener(reminder: Reminder, context: Context) {
        switch.setOnClickListener {
            reminder.isonoroff = switch.isChecked
            var apiService = RestApiService()
            if (context is ManageReminderActivity) {
                reminder.reminderId?.let { reminderId ->
                    var callback = context as ManageReminderActivity
                    if (switch.isChecked) {
                        reminder.uuid?.let { id -> callback.clearNotification(UUID.fromString(id)) }
                        var dateString = CustomDate.GetStringFromDate(
                            Calendar.getInstance(),
                            "yyyy-MM-dd'T'HH:mm:ss"
                        )
                        reminder.uuid = context.setNotification(
                            dateString,
                            setHour,
                            setMinute,
                            reminder.reminderId!!,
                            forWellness = true
                        ).toString()
                    } else {
                        reminder.uuid?.let { id -> callback.clearNotification(UUID.fromString(id)) }
                    }
                    callback.showProgressBarForUpdating()
                    apiService.UpdateReminder(reminderId, reminder) {
                        if (it == true) {

                        } else {

                        }
                        callback.dismissProgressBar()
                    }
                }
            }
        }
    }

}