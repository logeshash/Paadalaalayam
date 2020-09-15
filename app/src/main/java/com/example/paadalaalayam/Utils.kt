package com.example.paadalaalayam

import com.google.firebase.database.DataSnapshot

/* Retrieve data from firebase based on the given user details*/
fun DataSnapshot.asUser(): UserDetails? =
    getValue(UserDetails::class.java)