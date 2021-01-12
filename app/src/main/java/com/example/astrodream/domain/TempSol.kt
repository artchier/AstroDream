package com.example.astrodream.domain

import com.google.gson.JsonObject
import kotlin.math.roundToInt

data class TempSol(
    val AT: JsonObject?,
    val PRE: JsonObject?,
) {
    var solMars: Long = 0
    var maxTempMars: String = ""
    var minTempMars: String = ""

    fun setTemperature(sol: Long) {
        solMars = sol
        val source = PRE ?: AT ?: return
        val maxTempFah = source.get("mx").toString().toDouble()
        val minTempFah = source.get("mn").toString().toDouble()
        maxTempMars = (((maxTempFah) - 32) * 5 / 9).roundToInt().toString()
        minTempMars = (((minTempFah) - 32) * 5 / 9).roundToInt().toString()
    }

    override fun toString(): String {
        return "TempSol(PRE=$PRE, solMars=$solMars, maxTempMars='$maxTempMars', minTempMars='$minTempMars')"
    }

}
