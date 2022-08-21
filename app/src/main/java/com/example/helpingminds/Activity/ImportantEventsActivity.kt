package com.example.helpingminds.Activity

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.helpingminds.Model.Event
import com.example.helpingminds.R
import com.example.helpingminds.Utility.CustomClass.CustomDate
import com.example.helpingminds.Utility.CustomClass.Phone
import com.example.helpingminds.Utility.Retrofit.RestApiService
import com.twinkle94.monthyearpicker.picker.YearMonthPickerDialog
import java.util.*


class ImportantEventsActivity : AppCompatActivity() {
    private lateinit var monthRelLayout: RelativeLayout
    private lateinit var yearRelLayout: RelativeLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var recycleViewAdapter: RecycleViewAdapter2
    private lateinit var noEventView: LinearLayout
    private lateinit var monthText: TextView
    private lateinit var yearText: TextView
    private var selectedYear = 0
    private var selectedMonth = 0
    private var eventList: ArrayList<Event> = ArrayList<Event>()
    private lateinit var mprogress: ProgressDialog

    private lateinit var footerView: View
    private lateinit var footerText: TextView


    private var selectedDate: Calendar = Calendar.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_important_events)

        InitView()
        InitListener()
        InitProgressDialog()
    }

    private fun InitView() {
        monthRelLayout = findViewById(R.id.relativeMonth)
        monthText = findViewById(R.id.monthText)
        yearText = findViewById(R.id.yearText)
        yearRelLayout = findViewById(R.id.relativeYear)
        recyclerView = findViewById(R.id.listView)
        noEventView = findViewById(R.id.no_event)
        footerView = findViewById(R.id.footer)
        footerText = findViewById(R.id.phoneNumber)


        var calendar = Calendar.getInstance()
        selectedYear = calendar.get(Calendar.YEAR)
        selectedMonth = calendar.get(Calendar.MONTH)


        recycleViewAdapter = RecycleViewAdapter2(eventList, this)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = recycleViewAdapter

    }

    private fun GetEventsByDate(month: Int, year: Int) {
        var calendarService = RestApiService()
        mprogress.show()
        calendarService.getEventByMonthYear(month + 1, year) {
            if (it != null) {
                eventList.clear()
                eventList.addAll(it)
            } else {
                eventList.clear()
            }

            if (eventList.isEmpty()) {
                noEventView.visibility = View.VISIBLE
            } else {
                noEventView.visibility = View.GONE
            }
            recycleViewAdapter?.notifyDataSetChanged()
            mprogress.dismiss()
        }
    }

    private fun InitListener() {
        footerText.setOnClickListener {
            Phone.makeCall(this, footerText.text.split(":")[1])
        }

        monthRelLayout.setOnClickListener {
            SelectMonthYear()
        }

        yearRelLayout.setOnClickListener {
            SelectMonthYear()
        }

    }

    private fun SelectMonthYear() {
        val calendar = Calendar.getInstance()

        val yearMonthPickerDialog =
            YearMonthPickerDialog(this, YearMonthPickerDialog.OnDateSetListener { year, month ->
                calendar[Calendar.YEAR] = year
                calendar[Calendar.MONTH] = month
                monthText.text = CustomDate.getMonthString(month)
                yearText.text = year.toString()
                GetEventsByDate(month, year)
            })
        yearMonthPickerDialog.show()
    }


    private fun InitProgressDialog() {
        mprogress = ProgressDialog(this)
        mprogress.setTitle("Fetching Events...")
        mprogress.setMessage("Please wait...")
        mprogress.setCancelable(false)
        mprogress.isIndeterminate = true
    }

}

class RecycleViewAdapter2(
    private val lst: ArrayList<Event>,
    context: Context
) : RecyclerView.Adapter<AdapterHolder>() {
    val activityContext = context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterHolder {
        val inflatedView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.event_display_list_view, parent, false)
        return AdapterHolder(inflatedView)
    }

    override fun getItemCount(): Int {
        return lst.size
    }

    override fun onBindViewHolder(holder: AdapterHolder, position: Int) {
        val holder = holder as AdapterHolder
        holder.setUpViews(lst[position])
        holder.setUpListener(activityContext)
    }

}

class AdapterHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var view: View = itemView
    private lateinit var event: Event
    private val descriptionText: TextView = itemView.findViewById(R.id.descriptionText)
    private val dateText: TextView = itemView.findViewById(R.id.dateText)


    fun setUpViews(evnt: Event) {
        event = evnt
        descriptionText.text = event.eventName
        var date = CustomDate.GetDateFromString(event.eventDate, "yyyy-MM-dd")
        var dayStringFormat = CustomDate.GetStringDayFormatFromDay(date.get(Calendar.DAY_OF_MONTH))
        var month = CustomDate.getMonthString(date.get(Calendar.MONTH))
        dateText.text = "$dayStringFormat $month -"
    }

    fun setUpListener(context: Context) {
        view.setOnClickListener {

        }
    }

}