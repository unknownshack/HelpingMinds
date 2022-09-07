package com.example.helpingminds.Fragment

import android.app.*
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.helpingminds.Callback.AfterLoginActivityCallback
import com.example.helpingminds.Model.Event
import com.example.helpingminds.Model.Reminder
import com.example.helpingminds.R
import com.example.helpingminds.Utility.CustomClass.UserInfo
import com.example.helpingminds.Utility.Retrofit.RestApiService
import java.text.SimpleDateFormat
import java.util.*


class SetReminderFragment(evnt: Event) : Fragment() {
    private lateinit var dateText: EditText
    private lateinit var timeText: TextView
    private lateinit var noteText: EditText
    private lateinit var repeatSpinner: Spinner
    private lateinit var prioritySpinner: Spinner
    private lateinit var confirmButton: Button

    private lateinit var setReminderView: View
    private lateinit var cancelButton: Button

    private lateinit var editButton: Button
    private lateinit var completedButton: Button
    private lateinit var updateButton: Button

    private val event = evnt
    private lateinit var reminder: Reminder

    private val repetitions = arrayOf("Every Hour", "Never")
    private val priorities = arrayOf("Low", "Medium", "High")

    private lateinit var mProgressBar: ProgressDialog
    private lateinit var reminderBar: ProgressDialog

    private lateinit var builder: AlertDialog.Builder

    private var selectedRepetition: Int = 0
    private var selectedPriority: Int = 0

    private var apiService = RestApiService()

    private lateinit var cb: AfterLoginActivityCallback
    private lateinit var uuid: UUID

    private var hourToSet: Int = 0
    private var minuteToSet: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setReminderView = inflater.inflate(R.layout.fragment_set_reminder, container, false)
        return setReminderView
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (activity is AfterLoginActivityCallback) {
            cb = activity as AfterLoginActivityCallback
        }

        init()
        mProgressBar.show()
        initSpinner()
        initListener()
        initAlert()
        loadValues()
        checkIfReminderIsSet()
        mProgressBar.dismiss()
    }

    private fun initAlert() {
        builder = AlertDialog.Builder(context)
        builder.setCancelable(true)

        builder.setPositiveButton(
            "OK",
            DialogInterface.OnClickListener { dialog, id ->
                run {
                    dialog.cancel()
                    activity?.finish()
                }
            })

        builder.setOnDismissListener() {
            activity?.finish()
        }

    }

    private fun setTimeText(reminder: Reminder) {
        var time = reminder.reminderDate.split("T")[1]
        var hourOfDay = time.split(":")[0].toInt()
        var minute = time.split(":")[1].toInt()

        val formattedTime: String = when {
            hourOfDay == 0 -> {
                if (minute < 10) {
                    "${hourOfDay + 12}:0${minute} am"
                } else {
                    "${hourOfDay + 12}:${minute} am"
                }
            }
            hourOfDay > 12 -> {
                if (minute < 10) {
                    "${hourOfDay - 12}:0${minute} pm"
                } else {
                    "${hourOfDay - 12}:${minute} pm"
                }
            }
            hourOfDay == 12 -> {
                if (minute < 10) {
                    "${hourOfDay}:0${minute} pm"
                } else {
                    "${hourOfDay}:${minute} pm"
                }
            }
            else -> {
                if (minute < 10) {
                    "${hourOfDay}:${minute} am"
                } else {
                    "${hourOfDay}:${minute} am"
                }
            }
        }
        hourToSet = hourOfDay
        minuteToSet = minute
        timeText.text = formattedTime
    }

    private fun disableView() {
        timeText.isEnabled = false
        repeatSpinner.isEnabled = false
        prioritySpinner.isEnabled = false
    }

    private fun enableView() {
        timeText.isEnabled = true
        repeatSpinner.isEnabled = true
        prioritySpinner.isEnabled = true
    }

    private fun completedReminderVisibility() {
        cancelButton.visibility = View.GONE
        updateButton.visibility = View.GONE
        editButton.visibility = View.GONE
        confirmButton.visibility = View.GONE
        completedButton.visibility = View.VISIBLE
    }

    private fun unCompletedReminderVisibility() {
        cancelButton.visibility = View.VISIBLE
        updateButton.visibility = View.GONE
        editButton.visibility = View.VISIBLE
        confirmButton.visibility = View.GONE
        completedButton.visibility = View.GONE
    }

    private fun newReminderVisibility() {
        cancelButton.visibility = View.GONE
        updateButton.visibility = View.GONE
        editButton.visibility = View.GONE
        confirmButton.visibility = View.VISIBLE
        completedButton.visibility = View.GONE
    }

    private fun checkIfReminderIsSet() {
        apiService.checkIfReminderExist(event.eventId) {
            if (it != null && it.isNotEmpty()) {
                var filteredReminder = it!!.filter { x -> x.userId == UserInfo.userId }
                if (filteredReminder.isNotEmpty()) {
                    reminder = it[0]
                    if (reminder.completed == true) {
                        completedReminderVisibility()
                    } else {
                        unCompletedReminderVisibility()
                    }
                    it.first().priority?.let { it1 -> prioritySpinner.setSelection(it1) }
                    it.first().repeat?.let { it1 -> repeatSpinner.setSelection(it1) }
                    setTimeText(reminder)
                    disableView()
                } else {
                    newReminderVisibility()
                }
            } else {
                newReminderVisibility()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadValues() {
        //val formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy")
        dateText.text = Editable.Factory.getInstance().newEditable(event.eventDate.split("T")[0])
        //timeText.text = Editable.Factory.getInstance().newEditable("00:00am")
        noteText.text = Editable.Factory.getInstance().newEditable(event.eventName)
    }

    private val timePickerDialogListener: TimePickerDialog.OnTimeSetListener =
        TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute -> // logic to properly handle
            // the picked timings by user
            val formattedTime: String = when {
                hourOfDay == 0 -> {
                    if (minute < 10) {
                        "${hourOfDay + 12}:0${minute} am"
                    } else {
                        "${hourOfDay + 12}:${minute} am"
                    }
                }
                hourOfDay > 12 -> {
                    if (minute < 10) {
                        "${hourOfDay - 12}:0${minute} pm"
                    } else {
                        "${hourOfDay - 12}:${minute} pm"
                    }
                }
                hourOfDay == 12 -> {
                    if (minute < 10) {
                        "${hourOfDay}:0${minute} pm"
                    } else {
                        "${hourOfDay}:${minute} pm"
                    }
                }
                else -> {
                    if (minute < 10) {
                        "${hourOfDay}:${minute} am"
                    } else {
                        "${hourOfDay}:${minute} am"
                    }
                }
            }
            hourToSet = hourOfDay
            minuteToSet = minute
            timeText.text = formattedTime
        }

    private fun initListener() {
        repeatSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                selectedRepetition = position
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        prioritySpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                selectedPriority = position
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        editButton.setOnClickListener {
            enableView()
            updateButton.visibility = View.VISIBLE
            editButton.visibility = View.GONE
        }

        updateButton.setOnClickListener {
            reminderBar.show()
            var calendar = Calendar.getInstance()

            var year = dateText.text.split("-")[0].toInt()
            var month = dateText.text.split("-")[1].toInt()
            var day = dateText.text.split("-")[2].toInt()
            var hour = hourToSet
            var minute = minuteToSet
            calendar.set(year, month - 1, day, hour, minute, 0)

            var sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")

            reminder.reminderDate = sdf.format(calendar.time)
            reminder.priority = selectedPriority
            reminder.repeat = selectedRepetition

            cb.clearNotification(UUID.fromString(reminder.uuid))
            uuid = cb.setNotification(
                event.eventDate,
                hourToSet,
                minuteToSet,
                reminder.reminderId!!
            )
            reminder.uuid = uuid.toString()
            apiService.UpdateReminder(reminder.reminderId!!, reminder) {
                if (it == true) {
                    builder.setMessage("Saved successfully")
                } else {
                    builder.setMessage("Reminder could not be saved")
                }
                var alert = builder.create()
                alert.show()
                reminderBar.dismiss()
            }
        }

        confirmButton.setOnClickListener {
            //save to database
            reminderBar.show()
            var calendar = Calendar.getInstance()

            var year = dateText.text.split("-")[0].toInt()
            var month = dateText.text.split("-")[1].toInt()
            var day = dateText.text.split("-")[2].toInt()
            var hour = hourToSet
            var minute = minuteToSet
            calendar.set(year, month - 1, day, hour, minute, 0)

            var sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")

            var reminderNew = Reminder(null, event.eventId, selectedPriority, selectedRepetition)
            reminderNew.userId = UserInfo.userId
            reminderNew.reminderDate = sdf.format(calendar.time)
            reminderNew.purpose = event.eventName
            reminderNew.nonEventNote = event.eventName
            reminderNew.isonoroff = true
            apiService.saveReminder(reminderNew) {
                if (it != null) {
                    var savedReminder = it
                    uuid = cb.setNotification(
                        event.eventDate,
                        hourToSet,
                        minuteToSet,
                        savedReminder.reminderId!!
                    )
                    savedReminder.uuid = uuid.toString()
                    apiService.UpdateReminder(savedReminder.reminderId!!, savedReminder) {
                        if (it != null) {

                        } else {
                            cb.clearNotification(uuid)
                        }
                    }
                    builder.setMessage("Saved successfully")
                    var alert = builder.create()
                    alert.show()
                } else {
                    builder.setMessage("Reminder could not be saved")
                }

            }
            reminderBar.dismiss()
        }

        cancelButton.setOnClickListener {
            reminder.uuid?.let { uuid ->
                cb.clearNotification(UUID.fromString(uuid))
            }
            reminder.reminderId?.let { it1 ->
                apiService.DeleteReminder(it1) {
                    if (it != null && it) {
                        //delete successful
                        builder.setMessage("Deleted Sucessfully")
                        var alert = builder.create()
                        alert.show()
                    } else {
                        //delete unsucessful
                    }
                }
            }
        }

        timeText.setOnClickListener {
            val timePicker: TimePickerDialog = TimePickerDialog(
                // pass the Context
                activity,
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                // listener to perform task
                // when time is picked
                timePickerDialogListener,
                // default hour when the time picker
                // dialog is opened
                12,
                // default minute when the time picker
                // dialog is opened
                10,
                // 24 hours time picker is
                // false (varies according to the region)
                false
            )

            timePicker.window?.setBackgroundDrawableResource(android.R.color.transparent);

            // then after building the timepicker
            // dialog show the dialog to user
            timePicker.show()
        }
    }

    private fun initSpinner() {
        val adapter = activity?.let {
            ArrayAdapter(
                it,
                android.R.layout.simple_spinner_item, repetitions
            )
        }
        repeatSpinner.adapter = adapter


        val priorityAdapter = activity?.let {
            ArrayAdapter(
                it,
                android.R.layout.simple_spinner_item, priorities
            )
        }
        prioritySpinner.adapter = priorityAdapter

    }

    private fun init() {
        dateText = setReminderView.findViewById(R.id.dateEditText)
        timeText = setReminderView.findViewById(R.id.timeEditText)
        noteText = setReminderView.findViewById(R.id.noteEditText)
        repeatSpinner = setReminderView.findViewById(R.id.repeatSpinner)
        prioritySpinner = setReminderView.findViewById(R.id.prioritySpinner)
        confirmButton = setReminderView.findViewById(R.id.confirm_button)
        cancelButton = setReminderView.findViewById(R.id.cancel_button)

        editButton = setReminderView.findViewById(R.id.edit_button)

        completedButton = setReminderView.findViewById(R.id.completedButton)
        completedButton.visibility = View.GONE

        updateButton = setReminderView.findViewById(R.id.save_button)

        mProgressBar = ProgressDialog(activity)
        mProgressBar.setTitle("Loading...")
        mProgressBar.setMessage("Please Wait...")
        mProgressBar.setCancelable(false)
        mProgressBar.isIndeterminate = true

        reminderBar = ProgressDialog(activity)
        reminderBar.setTitle("Saving Reminder")
        reminderBar.setMessage("Please Wait...")
        reminderBar.setCancelable(false)
        reminderBar.isIndeterminate = true

    }

}