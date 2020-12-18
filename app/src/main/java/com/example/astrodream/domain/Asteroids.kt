package com.example.astrodream.domain

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.JsonArray
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
) : Serializable, Comparable<Asteroid>{

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDataFormatada(): String {
        val date: List<String> = close_approach_data[0].asJsonObject.get("close_approach_date").toString().removeSurrounding("\"").split("-")
        return "${date[2]}/${date[1]}/${date[0]}"
     //   return LocalDateTime.parse(close_approach_data[0].asJsonObject.get("close_approach_date").toString().removeSurrounding("\"")).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    }

    override fun compareTo(other: Asteroid): Int {
        return name.compareTo(other.name)
    }
}