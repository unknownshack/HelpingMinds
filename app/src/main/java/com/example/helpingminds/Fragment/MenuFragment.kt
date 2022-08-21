package com.example.helpingminds.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.helpingminds.Callback.AfterLoginActivityCallback
import com.example.helpingminds.R


class MenuFragment : Fragment() {
    private lateinit var menuView:View
    private lateinit var calendar:TextView
    private lateinit var homepage:TextView
    private lateinit var questionAboutApp: TextView
    private lateinit var wellness: TextView
    private lateinit var manageReminder: TextView
    private lateinit var cb: AfterLoginActivityCallback
    private lateinit var signOutButton: Button
    private lateinit var importantEventsMenu: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        menuView = inflater.inflate(R.layout.fragment_menu, container, false)
        return menuView;
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        calendar = menuView.findViewById(R.id.calendar)
        signOutButton = menuView.findViewById(R.id.signOut)
        homepage = menuView.findViewById(R.id.homepage)
        importantEventsMenu = menuView.findViewById(R.id.importantEventsMenu)
        questionAboutApp = menuView.findViewById(R.id.questionAboutApp)
        wellness = menuView.findViewById(R.id.wellness)
        manageReminder = menuView.findViewById(R.id.manageReminder)

        if(activity is AfterLoginActivityCallback){
            cb = activity as AfterLoginActivityCallback
        }
        calendar.setOnClickListener{
            cb.switchToCalendarActivity()
        }

        homepage.setOnClickListener{
            cb.switchToHomePageActivity()
        }

        importantEventsMenu.setOnClickListener{
            cb.switchToImportantEventActivity()
        }

        questionAboutApp.setOnClickListener{
            cb.switchToQuestionAboutAppActivity()
        }

        wellness.setOnClickListener {
            cb.switchToWellnessActivity()
        }

        manageReminder.setOnClickListener {
            cb.switchToManageReminderActivity()
        }

        signOutButton.setOnClickListener {
            cb.signOut()
        }
    }
}