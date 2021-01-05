package com.example.astrodream.domain

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.JsonArray
import java.io.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class AsteroidRes(val data: Data)

data class Data(var results: ArrayList<Asteroid>)

class AsteroidsMap(var near_earth_objects: List<Map<String,Asteroid>>)

class Asteroid(
    val name: String,
    val is_potentially_hazardous_asteroid: Boolean,
    val absolute_magnitude: Double,
    val relative_velocity: AsteroidVelocidade,
    close_approach_data: JsonArray,
    val miss_distance: AsteroidDistancia,
    val orbiting_body: String
) : Serializable {

    var date: String = close_approach_data[0].toString()

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDataFormatada(): String {
        return LocalDateTime.parse(date).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    }
}