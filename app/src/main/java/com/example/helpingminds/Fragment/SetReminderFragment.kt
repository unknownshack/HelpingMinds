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
import com.example.helpingminds.Utility.Retrofit.RestApiService
import java.util.*


class SetReminderFragment(evnt: Event) : Fragment() {
    private lateinit var dateText:EditText
    private lateinit var timeText: EditText
    private lateinit var noteText:EditText
    private lateinit var repeatSpinner: Spinner
    private lateinit var prioritySpinner: Spinner
    private lateinit var confirmButton: Button

    private lateinit var setReminderView: View
    private lateinit var cancelButton: Button

    private val event = evnt
    private lateinit var reminder:Reminder

    private val repetitions = arrayOf("Every Hour", "Never")
    private val priorities = arrayOf("Low","Medium","High")

    private lateinit var mProgressBar: ProgressDialog
    private lateinit var reminderBar: ProgressDialog

    private lateinit var builder: AlertDialog.Builder

    private var selectedRepetition:Int = 0
    private var selectedPriority:Int = 0

    private var apiService = RestApiService()

    private lateinit var cb: AfterLoginActivityCallback
    private lateinit var uuid:UUID

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

        if(activity is AfterLoginActivityCallback){
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

    private fun initAlert(){
        builder = AlertDialog.Builder(context)
        builder.setCancelable(true)

        builder.setPositiveButton(
            "OK",
            DialogInterface.OnClickListener { dialog, id -> run {
                    dialog.cancel()
                    activity?.finish()
                }
            })

        builder.setOnDismissListener(){
            activity?.finish()
        }

    }

    private fun checkIfReminderIsSet(){
        apiService.checkIfReminderExist(event.eventId){
            if(it != null && it.isNotEmpty()){
                reminder = it[0]
                cancelButton.visibility = View.VISIBLE
                confirmButton.visibility = View.GONE
                prioritySpinner.setSelection(it.first().priority)
                repeatSpinner.setSelection(it.first().repeat)
            }
            else{
                cancelButton.visibility = View.GONE
                confirmButton.visibility = View.VISIBLE
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadValues(){
        //val formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy")
        dateText.text = Editable.Factory.getInstance().newEditable(event.eventDate.split("T")[0])
        timeText.text = Editable.Factory.getInstance().newEditable("6:00am")
        noteText.text = Editable.Factory.getInstance().newEditable(event.eventName)
    }

    
    private fun initListener(){
        repeatSpinner.onItemSelectedListener = object:
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
                selectedRepetition = position
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        prioritySpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
                selectedPriority = position
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        confirmButton.setOnClickListener {
            //save to database
            reminderBar.show()
            var reminder = Reminder(null, event.eventId, selectedPriority, selectedRepetition)
            apiService.saveReminder(reminder){
                if(it != null){
                    //save successful
                    uuid = cb.setNotification("2022-07-25T0")
                    builder.setMessage("Saved successfully")
                    var alert = builder.create()
                    alert.show()
                }
                else{
                    //save unsuccessful
                }

            }
            reminderBar.dismiss()
        }

        cancelButton.setOnClickListener {
            reminder.reminderId?.let { it1 ->
                apiService.DeleteReminder(it1){
                    if(it != null && it){
                        //delete successful
                        builder.setMessage("Deleted Sucessfully")
                        var alert = builder.create()
                        alert.show()
                    }
                    else{
                        //delete unsucessful
                    }
                }
            }
        }
    }

    private fun initSpinner(){
        val adapter = activity?.let {
            ArrayAdapter(
                it,
                android.R.layout.simple_spinner_item, repetitions)
        }
        repeatSpinner.adapter = adapter


        val priorityAdapter = activity?.let {
            ArrayAdapter(
                it,
                android.R.layout.simple_spinner_item, priorities)
        }
        prioritySpinner.adapter = priorityAdapter

    }

    private fun init(){
        dateText = setReminderView.findViewById(R.id.dateEditText)
        timeText = setReminderView.findViewById(R.id.timeEditText)
        noteText = setReminderView.findViewById(R.id.noteEditText)
        repeatSpinner = setReminderView.findViewById(R.id.repeatSpinner)
        prioritySpinner = setReminderView.findViewById(R.id.prioritySpinner)
        confirmButton = setReminderView.findViewById(R.id.confirm_button)
        cancelButton = setReminderView.findViewById(R.id.cancel_button)

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