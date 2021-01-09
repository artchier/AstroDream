package com.example.astrodream.domain

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.JsonArray
import org.json.JSONArray
import java.io.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class AsteroidRes(val data: Data)

data class Data(var results: ArrayList<Asteroid>)

class AsteroidsMap(var near_earth_objects: List<Map<String,Asteroid>>)

data class Asteroid(
    val id: String,
    val name: String,
    val is_potentially_hazardous_asteroid: Boolean,
    val absolute_magnitude: String?,
    val relative_velocity: String?,
    val close_approach_data: String?,
    val miss_distance: String?,
    val estimated_diameter: String,
    val orbiting_body: String?,
    val linkExterno: String?
) : Serializable, Comparable<Asteroid>{

    var date: String = close_approach_data.toString()


    override fun compareTo(other: Asteroid): Int {
        return name.compareTo(other.name)
    }
}