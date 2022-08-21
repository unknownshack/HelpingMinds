package com.example.helpingminds

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.helpingminds.Activity.AfterLoginActivity
import com.example.helpingminds.Model.Event
import com.example.helpingminds.Utility.Retrofit.RestApiService
import java.text.SimpleDateFormat
import java.util.*


class CalendarActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private var recycleViewAdapter: RecycleViewAdapter? = null
    private lateinit var mprogress: ProgressDialog
    private lateinit var menuButton: ImageView
    private lateinit var noEventView: LinearLayout

    private lateinit var calendarView: CalendarView
    private lateinit var today: Calendar
    private var selectedDate: Calendar = Calendar.getInstance()

    private var eventList: ArrayList<Event> = ArrayList<Event>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)
        recyclerView = findViewById(R.id.listView)
        menuButton = findViewById(R.id.expanded_menu)
        calendarView = findViewById(R.id.calendarView)

        noEventView = findViewById(R.id.no_event)
        InitProgressDialog()
        menuButton.setOnClickListener {
            finish()
        }

        calendarView.setOnDateChangeListener { view, year, month, day ->
            var dateString = "${month + 1}-$day-$year"

            val sdf = SimpleDateFormat("MM-dd-yyyy")
            val date: Date = sdf.parse(dateString)
            selectedDate.time = date

            GetEvents(dateString)
        }
        recycleViewAdapter = RecycleViewAdapter(eventList, this, selectedDate)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = recycleViewAdapter

        today = Calendar.getInstance()
        var todayString = "${today.get(Calendar.MONTH) + 1}-${today.get(Calendar.DAY_OF_MONTH)}-${
            today.get(Calendar.YEAR)
        }"
        GetEvents(todayString)


    }

    private fun GetEvents(dateString: String) {
        var calendarService = RestApiService()
        mprogress.show()
        calendarService.getDateEvents(dateString) {
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
            recyclerView.adapter = recycleViewAdapter
            recycleViewAdapter?.notifyDataSetChanged()
            mprogress.dismiss()
        }
    }

    private fun InitProgressDialog() {
        mprogress = ProgressDialog(this)
        mprogress.setTitle("Fetching Events...")
        mprogress.setMessage("Please wait...")
        mprogress.setCancelable(false)
        mprogress.isIndeterminate = true
    }
}

class RecycleViewAdapter(
    private val lst: ArrayList<Event>,
    context: Context,
    selectedDate: Calendar
) : RecyclerView.Adapter<AdapterHolder>() {
    val activityContext = context
    val selDate = selectedDate
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterHolder {
        val inflatedView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_view, parent, false)
        return AdapterHolder(inflatedView)
    }

    override fun getItemCount(): Int {
        return lst.size
    }

    override fun onBindViewHolder(holder: AdapterHolder, position: Int) {
        val holder = holder as AdapterHolder
        holder.setUpViews(lst[position], selDate)
        holder.setUpListener(activityContext, selDate)
    }

}

class AdapterHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var view: View = itemView
    private val listText: TextView = itemView.findViewById(R.id.listText)
    private val setButton: Button = itemView.findViewById(R.id.setButton)
    private lateinit var event: Event


    fun setUpViews(evnt: Event, selectedDate: Calendar) {
        listText.text = evnt.eventName
        event = evnt

        if (compareDate(selectedDate)) {
            setButton.visibility = View.VISIBLE
        }
    }

    private fun compareDate(selectedDate: Calendar): Boolean {
        var today = Calendar.getInstance()
        return if (selectedDate.get(Calendar.YEAR) > today.get(Calendar.YEAR)) {
            true
        } else if (selectedDate.get(Calendar.YEAR) < today.get(Calendar.YEAR)) {
            false
        } else {
            selectedDate.get(Calendar.DAY_OF_YEAR) >= today.get(Calendar.DAY_OF_YEAR)
        }
    }

    fun setUpListener(context: Context, selectedDate: Calendar) {
        view.setOnClickListener {

        }

        setButton.setOnClickListener {
            if (compareDate(selectedDate)) {
                var intent = Intent(context, AfterLoginActivity::class.java)
                intent.putExtra("fragmentToStart", "SetReminder")
                intent.putExtra("event", event)
                context.startActivity(intent)
            }
        }
    }

}