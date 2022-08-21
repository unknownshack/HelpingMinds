package com.example.helpingminds.Fragment

import android.app.AlertDialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.helpingminds.Callback.WellnessActivityCallback
import com.example.helpingminds.Model.Reminder
import com.example.helpingminds.R
import com.example.helpingminds.Utility.CustomClass.CustomDate
import com.example.helpingminds.Utility.CustomClass.UserInfo
import com.example.helpingminds.Utility.Retrofit.RestApiService
import java.text.SimpleDateFormat
import java.util.*


class SetReminderForWellnessFragment(title: String) : Fragment() {
    private lateinit var reminderView: View
    private lateinit var titleText: TextView
    private var topic = title
    private lateinit var mprogress: ProgressDialog
    private lateinit var reminderBar: ProgressDialog

    private lateinit var deleteButton: Button
    private lateinit var confirmButton: Button
    private lateinit var editButton: Button
    private lateinit var updateButton: Button

    private lateinit var timeEditText: TextView
    private lateinit var noteEditText: EditText

    private lateinit var repeatSpinner: Spinner
    private lateinit var prioritySpinner: Spinner

    private lateinit var cb: WellnessActivityCallback

    private var hourToSet: Int = 0
    private var minuteToSet: Int = 0

    private val repetitions = arrayOf("Every Hour", "Never")
    private val priorities = arrayOf("Low", "Medium", "High")

    private var selectedRepetition: Int = 0
    private var selectedPriority: Int = 0

    private lateinit var uuid: UUID

    private lateinit var builder: AlertDialog.Builder

    private lateinit var reminder: Reminder



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        reminderView =
            inflater.inflate(R.layout.fragment_set_reminder_for_wellness, container, false)
        return reminderView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if(activity is WellnessActivityCallback){
            cb = activity as WellnessActivityCallback
        }

        InitProgressDialog()
        initViewAndListener()
        InitAlert()
    }

    private fun initViewAndListener() {
        titleText = reminderView.findViewById(R.id.title)
        titleText.text = titleText.text.toString() + " for " + topic

        deleteButton = reminderView.findViewById(R.id.cancel_button)
        deleteButton.setOnClickListener {
            reminder.uuid?.let { uuid ->
                cb.clearNotification(UUID.fromString(uuid))
            }
            reminder.reminderId?.let { it1 ->
                var apiService = RestApiService()
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

        updateButton = reminderView.findViewById(R.id.save_button)
        updateButton.setOnClickListener {
            reminderBar.show()
            var calendar = Calendar.getInstance()

            var year = 2000
            var month = 1
            var day = 1
            var hour = hourToSet
            var minute = minuteToSet
            calendar.set(year, month, day, hour, minute, 0)

            var sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")

            reminder.reminderDate = sdf.format(calendar.time)
            reminder.priority = selectedPriority
            reminder.repeat = selectedRepetition
            reminder.userId = UserInfo.userId
            reminder.nonEventNote = noteEditText.text.toString()
            reminder.isonoroff = true
            reminder.eventId = null
            reminder.purpose = topic.toLowerCase()
            //reminder.

            if(reminder.uuid != null){
                cb.clearNotification(UUID.fromString(reminder.uuid))
            }
            var apiService = RestApiService()
            var today = Calendar.getInstance()
            uuid = cb.setNotification(
                CustomDate.GetStringFromDate(today, format="yyyy-MM-dd'T'HH:mm:ss"),
                hourToSet,
                minuteToSet,
                reminder.reminderId!!,
                forWellnes = true
            )
            apiService.UpdateReminder(reminder.reminderId!!, reminder) {
                if (it == true) {
                    builder.setMessage("Saved successfully")
                } else {
                    cb.clearNotification(uuid)
                    builder.setMessage("Reminder could not be saved")
                }
                var alert = builder.create()
                alert.show()
                reminderBar.dismiss()
            }
        }

        confirmButton = reminderView.findViewById(R.id.confirm_button)
        confirmButton.setOnClickListener {
            //save to database
            reminderBar.show()
            var calendar = Calendar.getInstance()

            var year = 2000
            var month = 1
            var day = 1
            var hour = hourToSet
            var minute = minuteToSet
            calendar.set(year, month, day, hour, minute, 0)

            var sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")

            var reminderNew = Reminder(null, null, selectedPriority, selectedRepetition)
            reminderNew.nonEventNote = noteEditText.text.toString()
            reminderNew.reminderDate = sdf.format(calendar.time)
            reminderNew.userId = UserInfo.userId
            reminderNew.purpose = topic.toLowerCase()
            reminderNew.isonoroff = true

            var apiService = RestApiService()
            apiService.saveReminder(reminderNew) {
                if (it != null) {
                    var savedReminder = it
                    var today = Calendar.getInstance()
                    uuid = cb.setNotification(
                        CustomDate.GetStringFromDate(today, format="yyyy-MM-dd'T'HH:mm:ss"),
                        hourToSet,
                        minuteToSet,
                        savedReminder.reminderId!!,
                        forWellnes = true
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

        editButton = reminderView.findViewById(R.id.edit_button)
        editButton.setOnClickListener {
            enableView()
            updateButton.visibility = View.VISIBLE
            it.visibility = View.GONE
        }

        timeEditText = reminderView.findViewById(R.id.timeEditText)
        timeEditText.setOnClickListener {
            val timePicker: TimePickerDialog = TimePickerDialog(
                activity,
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                timePickerDialogListener,
                12,
                10,
                false
            )

            timePicker.window?.setBackgroundDrawableResource(android.R.color.transparent);
            timePicker.show()
        }

        noteEditText = reminderView.findViewById(R.id.noteEditText)

        repeatSpinner = reminderView.findViewById(R.id.repeatSpinner)
        prioritySpinner = reminderView.findViewById(R.id.prioritySpinner)
        initSpinner()

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

        var reminderService = RestApiService()
        mprogress.show()
        reminderService.GetAllReminder() {
            var filteredList: List<Reminder>? = null
            filteredList = it?.toList()
                ?.filter { x -> x.userId == UserInfo.userId && x.purpose == topic.toLowerCase() }

            if (filteredList != null && filteredList.isNotEmpty()) {
                confirmButton.visibility = View.GONE
                updateButton.visibility = View.GONE
                reminder = filteredList.first()
                reminder.priority?.let { it1 -> prioritySpinner.setSelection(it1) }
                reminder.repeat?.let { it1 -> repeatSpinner.setSelection(it1) }
                setTimeText(reminder)
                noteEditText.setText(reminder.nonEventNote)
                disableView()
            } else {
                updateButton.visibility = View.GONE
                editButton.visibility = View.GONE
                deleteButton.visibility = View.GONE
            }

            mprogress.dismiss()
        }

    }

    private fun InitProgressDialog() {
        mprogress = ProgressDialog(activity)
        mprogress.setTitle("Fetching Events...")
        mprogress.setMessage("Please wait...")
        mprogress.setCancelable(false)
        mprogress.isIndeterminate = true

        reminderBar = ProgressDialog(activity)
        reminderBar.setTitle("Saving Reminder")
        reminderBar.setMessage("Please Wait...")
        reminderBar.setCancelable(false)
        reminderBar.isIndeterminate = true

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
            timeEditText.setText(formattedTime)
        }

    private fun enableView() {
        timeEditText.isEnabled = true
        repeatSpinner.isEnabled = true
        prioritySpinner.isEnabled = true
        noteEditText.isEnabled = true
        noteEditText.isFocusable = true
        noteEditText.isFocusableInTouchMode = true
    }

    private fun disableView() {
        timeEditText.isEnabled = false
        repeatSpinner.isEnabled = false
        prioritySpinner.isEnabled = false
        noteEditText.isEnabled = false
        noteEditText.isFocusable = false
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
        timeEditText.text = formattedTime
    }

    private fun InitAlert() {
        builder = AlertDialog.Builder(context)
        builder.setCancelable(true)

        builder.setPositiveButton(
            "OK",
            DialogInterface.OnClickListener { dialog, id ->
                run {
                    dialog.cancel()
                    activity?.supportFragmentManager?.popBackStack()
                    //activity?.finish()
                }
            })

        builder.setOnDismissListener() {
            activity?.supportFragmentManager?.popBackStack()
            //activity?.finish()
        }

    }



}