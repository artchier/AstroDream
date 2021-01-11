package com.example.astrodream.domain

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.astrodream.domain.util.format
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import java.io.Serializable

data class AsteroidRes(val element_count: Int, val near_earth_objects: JsonObject)

data class AsteroidData(
    val id: String,
    val name: String,
    val is_potentially_hazardous_asteroid: Boolean,
    val absolute_magnitude_h: Double,
    val close_approach_data: JsonArray,
    val estimated_diameter: JsonObject
) : Serializable, Comparable<AsteroidData>{

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDataFormatada(): String {
        val date: List<String> = close_approach_data[0].asJsonObject.get("close_approach_date").toString().removeSurrounding("\"").split("-")
        return "${date[2]}/${date[1]}/${date[0]}"
    }

    private fun getVelocity(): String{
        return "${close_approach_data[0].asJsonObject.get("relative_velocity")
            .asJsonObject.get("kilometers_per_second")
            .toString().removeSurrounding("\"").toDouble().format(2)
    } km/s"
    }

    private fun getTamanho(): String{
        return "${estimated_diameter.get("meters")
            .asJsonObject.get("estimated_diameter_max").toString().toDouble().format(2)
    } metros"
    }

    private fun getDistancia(): String {
        return "${close_approach_data[0].asJsonObject.get("miss_distance")
            .asJsonObject.get("lunar")
            .toString().removeSurrounding("\"").toDouble().format(2)
        } LD"
    }

    private fun getLinkExterno(): String{
        return "https://ssd.jpl.nasa.gov/sbdb.cgi?sstr=$id;orb=1;cov=0;log=0;cad=0#orb"
    }

    private fun getOrbitingBody(): String{
        val orbita = close_approach_data[0].asJsonObject.get("orbiting_body")
            .toString().removeSurrounding("\"")
        when (orbita){
            "Earth" -> return "Terra"
            "Sun" -> return "Sol"
            "Mercury"-> return "Mercúrio"
            "Venus" -> return "Vênus"
            "Mars" -> return "Marte"
            "Jupiter" -> return "Júpiter"
            "Saturn" -> return "Saturno"
            "Uranus" -> return "Urano"
            "Neptune" -> return "Netuno"
        }
        return "Indefinido"
    }

    private fun getMagnitudeAbsoluta(): String = absolute_magnitude_h.format(2)

    override fun compareTo(other: AsteroidData): Int {
        return name.compareTo(other.name)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAsteroid(): Asteroid {
       return Asteroid(
            id, name, is_potentially_hazardous_asteroid,
           getMagnitudeAbsoluta(), getVelocity(), getDataFormatada(), getDistancia(), getTamanho(), getOrbitingBody(), getLinkExterno()
        )
    }
}

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

    override fun compareTo(other: Asteroid): Int {
        return name.compareTo(other.name)
    }
}