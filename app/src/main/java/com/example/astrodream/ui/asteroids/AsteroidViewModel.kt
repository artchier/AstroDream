package com.example.astrodream.ui.asteroids

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.astrodream.domain.Asteroid
import com.example.astrodream.domain.AsteroidRes
import com.example.astrodream.domain.Data
import com.example.astrodream.services.Service
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate

class AsteroidViewModel(val service: Service) : ViewModel() {

    val listResults = MutableLiveData<AsteroidRes>()

    fun popListResult() {
        viewModelScope.launch {
            val listAsteroids = mutableListOf<Asteroid>()
            var dateTT = LocalDate.now()
            val response = service.getResults(
                dateTT.toString(),
                dateTT.minusDays(2).toString(),
                "X6SUDXPVkjgyQoyKVunHMpwomboitIigBRVCSK1M"
            )
            for (i in 1..3) {
                val responseAtDate = response.get("near_earth_objects").asJsonObject.get(dateTT.toString()).asJsonArray
                val totalAsteroids = Gson().fromJson(responseAtDate,
                    object : TypeToken<List<Asteroid>>() {}.type) as List<Asteroid>
                listAsteroids.addAll(totalAsteroids)
                dateTT = dateTT.minusDays(1)
            }

            val asteroids = AsteroidRes(Data(listAsteroids.toList() as ArrayList<Asteroid>))

            listResults.value = asteroids
        }
    }
}
