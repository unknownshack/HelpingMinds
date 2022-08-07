package com.example.helpingminds.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import com.example.helpingminds.Callback.CallBackListener
import com.example.helpingminds.R

class ActionFragment : Fragment() {
    private lateinit var actionFragment:View
    private lateinit var createUser: LinearLayout
    private lateinit var createEvent: LinearLayout
    private lateinit var cb: CallBackListener
    private lateinit var createEventButton: Button
    private lateinit var createUserButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        actionFragment = inflater.inflate(R.layout.fragment_action, container, false)
        return actionFragment
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        createUser = actionFragment.findViewById(R.id.createUser)
        createEvent = actionFragment.findViewById(R.id.createEvent)
        createEventButton = actionFragment.findViewById(R.id.createEventButton)
        createUserButton = actionFragment.findViewById(R.id.createUserButton)

        if(activity is CallBackListener){
            cb = activity as CallBackListener
        }

        createUser.setOnClickListener{
            cb.createUser()
        }

        createUserButton.setOnClickListener {
            cb.createUser()
        }

        createEventButton.setOnClickListener {
            cb.createEvent()
        }

        createEvent.setOnClickListener {
            cb.createEvent()
        }
    }



}