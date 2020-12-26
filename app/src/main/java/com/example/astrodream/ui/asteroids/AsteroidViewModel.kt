package com.example.astrodream.ui.asteroids

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.astrodream.domain.*
import com.example.astrodream.services.Service
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import java.time.LocalDate

class AsteroidViewModel(val service: Service) : ViewModel() {

    val listResults = MutableLiveData<AsteroidRes>()
    val listAsteroid = ArrayList<Asteroid>()

    @RequiresApi(Build.VERSION_CODES.O)
    fun popListResult() {
        viewModelScope.launch {
            val listAsteroids =
                service.getAsteroidsDate(LocalDate.now().toString(), "X6SUDXPVkjgyQoyKVunHMpwomboitIigBRVCSK1M")

            listAsteroids.near_earth_objects.keySet().toList().forEach {
                val list = Gson().fromJson(listAsteroids.near_earth_objects.get(it),
                    object : TypeToken<List<AsteroidData>>() {}.type
                ) as List<AsteroidData>
                listAsteroid.addAll(list.map { it.getAsteroid() })
            }
            listResults.value = listAsteroids
            Log.i("LIST ASTEROIDS", listResults.toString())
        }
    }
}
