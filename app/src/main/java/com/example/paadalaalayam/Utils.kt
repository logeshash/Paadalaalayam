package com.example.paadalaalayam

import com.google.firebase.database.DataSnapshot

fun DataSnapshot.asUser(): UserDetails? =
    getValue(UserDetails::class.java)