package com.example.astrodream.services

import com.google.firebase.database.FirebaseDatabase

val firebaseDatabase = FirebaseDatabase.getInstance()
val databaseReference = firebaseDatabase.getReference("asteroides")
