package com.example.paadalaalayam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var mDatabase: DatabaseReference
    private lateinit var mUser: UserDetails


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        auth = FirebaseAuth.getInstance()

        mDatabase = FirebaseDatabase.getInstance().reference

//        Based on the user details get the user data from firebase db
        fun currentUserReference(): DatabaseReference =
            mDatabase.child("users").child(auth.currentUser!!.uid)

        currentUserReference().addListenerForSingleValueEvent(
            ValueListenerAdapter{
                mUser = it.asUser()!!
//                Set the welcome message with the user full name
                val welcomeMessage = getString(R.string.dashboard_title)+" "+mUser.name
                tvWelcome.text = welcomeMessage
            }
        )

//        Add new movie
        cvAddMovie.setOnClickListener{
            Toast.makeText(applicationContext,"Add Movie",Toast.LENGTH_SHORT).show()
        }

//        Add new lyrics
        cvAddLyrics.setOnClickListener{
            Toast.makeText(applicationContext,"Add Lyrics",Toast.LENGTH_SHORT).show()
        }

//        View the existing lyrics
        cvViewLyrics.setOnClickListener{
            Toast.makeText(applicationContext,"View Lyrics",Toast.LENGTH_SHORT).show()
        }

//        Logout from the application based
        cvLogout.setOnClickListener{
            auth.signOut()
            val intentLogin = Intent(this, LoginActivity::class.java)
            startActivity(intentLogin)
        }
    }

}