package com.example.astrodream.domain

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import java.io.Serializable
import kotlin.math.roundToInt

data class PlainClass(
    val title: String = "",
    var date: String = "",
    val url: Any = "",
    val explanation: String = "",
    var earth_date: String = "",
    var sol: Long = 1,
    var img_list: List<MarsImage> = listOf(MarsImage(1, Camera("", ""), "")),
    var maxTemp: String = "Temperaturas",
    var minTemp: String = "indisponiveis"
)
