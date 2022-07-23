package com.example.helpingminds

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CalendarActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var recycleViewAdapter: RecycleViewAdapter

    private lateinit var menuButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)
        recyclerView = findViewById(R.id.listView)
        menuButton = findViewById(R.id.expanded_menu)

        menuButton.setOnClickListener {
            finish()
        }


        val arrayList = ArrayList<String>()
        for(i in 1..10){
            arrayList.add("Hello world$i")
        }


        recycleViewAdapter = RecycleViewAdapter(arrayList)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        recyclerView.adapter = recycleViewAdapter
    }
}

class RecycleViewAdapter(private val lst: ArrayList<String>) : RecyclerView.Adapter<AdapterHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_view, parent, false)
        return AdapterHolder(inflatedView)
    }

    override fun getItemCount(): Int {
        return lst.size
    }

    override fun onBindViewHolder(holder: AdapterHolder, position: Int) {
        val holder = holder as AdapterHolder
        holder.setUpViews(lst[position])
    }

}

class AdapterHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {
    private var itemView: View = itemView
    private val listText: TextView = itemView.findViewById(R.id.listText)

    override fun onClick(v: View?) {
        Log.i("Rohit","To do")
    }

    fun setUpViews(text:String){
        listText.text = text
    }

}