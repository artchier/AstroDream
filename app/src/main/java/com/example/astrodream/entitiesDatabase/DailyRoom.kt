package com.example.astrodream.entitiesDatabase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily")
data class DailyRoom (

    @PrimaryKey
    var title: String,
    var date: String,
    var url: String,
//    val hdurl: String,
    var explanation: String
)