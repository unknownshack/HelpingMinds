package com.example.helpingminds.Fragment

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.example.helpingminds.Model.Event
import com.example.helpingminds.R
import com.example.helpingminds.Utility.Retrofit.RestApiService
import java.text.SimpleDateFormat
import java.util.*

class CreateEventFragment : Fragment() {
    private lateinit var createEventFragment: View
    private lateinit var createEventButton: Button
    private lateinit var eventName: EditText
    private lateinit var eventDate: EditText
    private lateinit var setButton: Button
    private lateinit var builder: AlertDialog.Builder
    private lateinit var mProgressBar: ProgressDialog
    private var popped:Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        createEventFragment = inflater.inflate(R.layout.fragment_create_event, container, false)
        return createEventFragment
    }

    private fun initAlert(){
        builder = AlertDialog.Builder(activity)
        builder.setCancelable(true)

        builder.setPositiveButton(
            "OK",
            DialogInterface.OnClickListener { dialog, id -> run {
                dialog.cancel()
                if(!popped){
                    requireActivity().supportFragmentManager.popBackStack()
                    popped = true
                }
                //activity?.finish()
            }
            })

        builder.setOnDismissListener(){
            //activity?.finish()
            if (!popped){
                requireActivity().supportFragmentManager.popBackStack();
                popped = true
            }
        }
    }

    private fun initProgressBar(){
        mProgressBar = ProgressDialog(activity)
        mProgressBar.setTitle("Saving Event...")
        mProgressBar.setMessage("Please Wait...")
        mProgressBar.setCancelable(false)
        mProgressBar.isIndeterminate = true
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        createEventButton = createEventFragment.findViewById(R.id.createButton)
        eventName = createEventFragment.findViewById(R.id.nameEdit)
        eventDate = createEventFragment.findViewById(R.id.dateEdit)
        setButton = createEventFragment.findViewById(R.id.setButton)

        initAlert()
        initProgressBar()

        setButton.setOnClickListener{
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)


            val dpd = DatePickerDialog(requireActivity(), DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                c.set(year, monthOfYear, dayOfMonth)
                val sdf = SimpleDateFormat("yyyy-MM-dd")
                eventDate.setText(sdf.format(c.time))

            }, year, month, day)

            dpd.show()
        }

        createEventButton.setOnClickListener {
            mProgressBar.show()
            var event = Event(0, eventName.text.toString(), eventDate.text.toString())
            var apiService = RestApiService()
            apiService.createEvents(event){
                if(it != null){
                    builder.setMessage("Event created successfully")
                    var alert = builder.create()
                    alert.show()
                }
                else{
                    builder.setMessage("Event could not be created!!")
                    var alert = builder.create()
                    alert.show()
                }
                mProgressBar.dismiss()
            }
        }

    }

}