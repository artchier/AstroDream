package com.example.astrodream.domain

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlin.math.roundToInt

data class PlainClass(
    val title: String = "x",
    val date: String = "x",
    val url: String = "x",
    var earth_date: String = "x",
    var sol: Long = 1,
    var img_list: List<String> = listOf("x"),
    var maxTemp: String = "Temperaturas",
    var minTemp: String = "indisponiveis"
)
