package com.example.helpingminds.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.helpingminds.R

class AdminPageFragment : Fragment() {
    private lateinit var adminPageView:View
    private lateinit var create:Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        adminPageView = inflater.inflate(R.layout.fragment_admin_page, container, false)
        return adminPageView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        create = adminPageView.findViewById(R.id.createButton)

        create.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack();
        }
    }

}