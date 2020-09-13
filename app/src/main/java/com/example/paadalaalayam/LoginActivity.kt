package com.example.paadalaalayam

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        btnLogin.setOnClickListener{
//            Hide keypad on login button click
            if (currentFocus != null) {
                val inputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
            }
//            Get input values from the user from Login form
            val inputEmail = editEmail.text?.trim().toString()
            val inputPassword = editPassword.text?.trim().toString()
//            Check all input values are given
            if(inputEmail.isNotEmpty() || inputPassword.isNotEmpty()) {
//                Authenticate the email and password with firebase auth
                signInUser(inputEmail, inputPassword)
            } else {
//                Display error if input values are missing
                Toast.makeText(this, "Enter both Email and password", Toast.LENGTH_LONG).show()
            }
        }

//        clicking on registration redirect to register page
        tvRegister.setOnClickListener{
            val intentRegister = Intent(this, MainActivity::class.java)
            startActivity(intentRegister)
        }
    }

    /*
    * Login based on firebase authentication with email and password
    * @param email as string
    * @param password as string
    * @return
    * */
    private fun signInUser(email: String, password: String) {

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {task ->
                if(task.isSuccessful) {
                    val intentDashbaord = Intent(this, DashboardActivity::class.java)
                    startActivity(intentDashbaord)
                } else {
                    Toast.makeText(this, "Error !! " + task.exception, Toast.LENGTH_LONG).show()
                }
            }
    }

    /*
    * Check if the user has already login on this device and redirect to dashboard
    * */
    override fun onStart() {
        super.onStart()

        val user = auth.currentUser
        if(user != null) {
            var intent = Intent(this, DashboardActivity::class.java);
            startActivity(intent)
        }
    }
}