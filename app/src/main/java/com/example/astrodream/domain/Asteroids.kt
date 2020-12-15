package com.example.astrodream.domain

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.io.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class AsteroidRes(val element_count: Int, val near_earth_objects: JsonObject)

data class Asteroid(
    val name: String,
    val is_potentially_hazardous_asteroid: Boolean,
    val absolute_magnitude: Double,
    val relative_velocity: AsteroidVelocidade,
    val close_approach_data: JsonArray,
    val miss_distance: AsteroidDistancia,
    val orbiting_body: String
) : Serializable{
    var date: String = close_approach_data[0].toString()

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDataFormatada(): String {
        return LocalDateTime.parse(date).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    }
}