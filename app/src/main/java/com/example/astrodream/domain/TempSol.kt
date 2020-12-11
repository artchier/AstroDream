package com.example.astrodream.domain

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlin.math.roundToInt

data class TempSol(
    val PRE: JsonObject,
    val maxTemp: String = "x",
    val minTemp: String = "x"
) {
    var solMars: Long = 0
    var maxTempMars: String = ""
    var minTempMars: String = ""

    fun wakeUp(sol: Long) {
        solMars = sol
        val maxTempFah =
            Gson().fromJson(this.PRE.get("mx"), object : TypeToken<Double>() {}.type) as Double
        val minTempFah =
            Gson().fromJson(this.PRE.get("mn"), object : TypeToken<Double>() {}.type) as Double
        maxTempMars = (((maxTempFah) - 32) * 5 / 9).roundToInt().toString()
        minTempMars = (((minTempFah) - 32) * 5 / 9).roundToInt().toString()
    }

    override fun toString(): String {
        return "TempSol(PRE=$PRE, solMars=$solMars, maxTempMars='$maxTempMars', minTempMars='$minTempMars')"
    }

}
