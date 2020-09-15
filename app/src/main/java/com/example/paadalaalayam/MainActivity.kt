package com.example.paadalaalayam

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        btnRegister.setOnClickListener{
//            Hide keypad on registration button click
            if (currentFocus != null) {
                val inputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
            }
//            Get input values from the user from registration form
            val inputEmail = editEmail.text?.trim().toString()
            val inputPassword = editPassword.text?.trim().toString()
            val inputMobile = editMobile.text?.trim().toString()
            val inputName = editName.text?.trim().toString()
            val inputCountry = resources.getString(R.string.country)
            val inputState = resources.getString(R.string.state)
            val inputCity = resources.getString(R.string.city)

//            Check all input values are given
            if(inputEmail.isNotEmpty() && inputPassword.isNotEmpty() && inputName.isNotEmpty() && inputPassword.isNotEmpty()) {

//                Pass input values to create user function
                createUser(inputEmail, inputPassword, inputName, inputMobile, inputCountry, inputState, inputCity)
            } else {

//                Display error if input values are missing
                Toast.makeText(this, "Enter all field values", Toast.LENGTH_LONG).show()
            }
        }
//        Clicking on login redirect to login page
        tvLogin.setOnClickListener{
            val intentLogin = Intent( this, LoginActivity::class.java)
            startActivity(intentLogin)
        }
    }

    /* User registration based on firebase authentication
    *  @param email as string
    *  @param password as string
    *  @param mobile as string
    *  @param country as string
    *  @param state as string
    *  @param city as string
    *  @return
    * */
    private fun createUser(email: String, password: String, name:String, mobile: String, country: String, state: String, city: String) {
//        Firebase authentication table for email and password creation
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this){task ->
                    if(task.isSuccessful) {
//                    If email and password is successful with firebase authentication add user details to user table on firebase realtime DB
                        val ref = FirebaseDatabase.getInstance().getReference("users")
//                    Set the firebase auth uid as userid into user table with other user details
                        val userId = auth.uid
                        if (userId != null) {
                            val userDetails = UserDetails(userId, email, name, mobile, country, state, city)

                            ref.child(userId).setValue(userDetails)
                            val intentDashboard = Intent(this, DashboardActivity::class.java)
                            startActivity(intentDashboard)
                        }
                    } else {
                        Toast.makeText(this, "Email already exist! Choose other email id", Toast.LENGTH_LONG).show()
                    }
                }
    }

    /*
    * Check if the user has already registered on this device and redirect to dashboard
    * */
    override fun onStart() {
        super.onStart()

        val user = auth.currentUser
        if(user != null) {
            val intentDashboard = Intent(this, DashboardActivity::class.java)
            startActivity(intentDashboard)
        }
    }


}