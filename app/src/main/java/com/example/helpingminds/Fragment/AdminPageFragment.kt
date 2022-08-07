package com.example.helpingminds.Fragment

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.opengl.Visibility
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.helpingminds.Model.User
import com.example.helpingminds.R
import com.example.helpingminds.Utility.Retrofit.RestApiService

class AdminPageFragment : Fragment() {
    private lateinit var adminPageView:View
    private lateinit var create:Button
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPwdEditText: EditText

    private lateinit var nameValid: TextView
    private lateinit var emailValid: TextView
    private lateinit var confirmPwdValid: TextView

    private lateinit var builder: AlertDialog.Builder
    private lateinit var mProgressBar: ProgressDialog
    private var popped:Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        adminPageView = inflater.inflate(R.layout.fragment_admin_page, container, false)
        return adminPageView
    }

    private fun initFields(){
        nameEditText = adminPageView.findViewById(R.id.nameEdit)
        emailEditText = adminPageView.findViewById(R.id.emailEdit)
        passwordEditText = adminPageView.findViewById(R.id.passwordEdit)
        confirmPwdEditText = adminPageView.findViewById(R.id.confirmPasswordEdit)

        nameValid = adminPageView.findViewById(R.id.nameValid)
        emailValid = adminPageView.findViewById(R.id.emailValid)
        confirmPwdValid = adminPageView.findViewById(R.id.confirmPwdValid)

        nameValid.visibility = View.GONE
        emailValid.visibility = View.GONE
        confirmPwdValid.visibility = View.GONE
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
            }
            })

        builder.setOnDismissListener(){
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

    private fun validateFields():Boolean{
        var fail = false
        if(TextUtils.isEmpty(nameEditText.text.toString())){
            fail = true
            nameValid.visibility = View.VISIBLE
        }

        if(TextUtils.isEmpty(emailEditText.text.toString())){
            fail = true
            emailValid.visibility = View.VISIBLE
            emailValid.text = "* Email field cannot be null"
        }

        if(passwordEditText.text.toString() != confirmPwdEditText.text.toString()){
            fail = true
            confirmPwdValid.visibility = View.VISIBLE
            confirmPwdValid.text = "* Password do not match"
        }
        return fail
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        create = adminPageView.findViewById(R.id.createButton)

        initFields()
        initAlert()
        initProgressBar()

        create.setOnClickListener {
            if(!validateFields()){
                mProgressBar.show()
                var apiService = RestApiService()
                var user = User(0, nameEditText.text.toString(), passwordEditText.text.toString())
                apiService.createUser(user){
                    if(it != null){
                        builder.setMessage("User created successfully")
                        var alert = builder.create()
                        alert.show()
                    }
                    else{
                        builder.setMessage("User could not be created")
                        var alert = builder.create()
                        alert.show()
                    }
                    mProgressBar.dismiss()
                }
            }

        }
    }

}