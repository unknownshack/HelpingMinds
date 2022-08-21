package com.example.helpingminds.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.helpingminds.R


class WellnessMenuFragment : Fragment() {

    private lateinit var menuView: View
    private lateinit var yoga: TextView
    private lateinit var meditation: TextView
    private lateinit var walking: TextView
    private lateinit var cooking: TextView
    private lateinit var stressManagement: TextView
    private lateinit var hiking: TextView
    private lateinit var gardening: TextView
    private lateinit var other: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        menuView = inflater.inflate(R.layout.fragment_wellness_menu, container, false)
        return menuView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViewAndListener()
    }

    private fun initViewAndListener(){
        yoga = menuView.findViewById(R.id.yoga)
        yoga.setOnClickListener {
            setReminder(yoga.text.toString())
        }

        meditation = menuView.findViewById(R.id.meditation)
        meditation.setOnClickListener {
            setReminder(meditation.text.toString())
        }

        walking = menuView.findViewById(R.id.walking)
        walking.setOnClickListener {
            setReminder(walking.text.toString())
        }

        cooking = menuView.findViewById(R.id.cooking)
        cooking.setOnClickListener {
            setReminder(cooking.text.toString())
        }

        stressManagement = menuView.findViewById(R.id.stress)
        stressManagement.setOnClickListener {
            setReminder(stressManagement.text.toString())
        }

        hiking = menuView.findViewById(R.id.hiking)
        hiking.setOnClickListener {
            setReminder(hiking.text.toString())
        }

        gardening = menuView.findViewById(R.id.gardening)
        gardening.setOnClickListener {
            setReminder(gardening.text.toString())
        }

        other = menuView.findViewById(R.id.other)
        other.setOnClickListener {
            setReminder(other.text.toString())
        }
    }

    private fun setReminder(topic: String){
        var transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.body, SetReminderForWellnessFragment(topic))
        transaction?.addToBackStack(null)
        transaction?.commit()
    }
}