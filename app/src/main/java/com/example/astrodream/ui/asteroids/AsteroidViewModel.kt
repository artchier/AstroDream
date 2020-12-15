package com.example.astrodream.ui.asteroids

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.astrodream.domain.*
import com.example.astrodream.services.Service
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch

class AsteroidViewModel(val service: Service) : ViewModel() {

    val listResults = MutableLiveData<AsteroidRes>()
    val listAsteroid = ArrayList<Asteroid>()

    fun popListResult() {
        viewModelScope.launch {
            val listAsteroids =
                service.getAsteroids("2020-12-11", "X6SUDXPVkjgyQoyKVunHMpwomboitIigBRVCSK1M")

            listAsteroids.near_earth_objects.keySet().toList().forEach {
                val list = Gson().fromJson(listAsteroids.near_earth_objects.get(it),
                    object : TypeToken<List<Asteroid>>() {}.type
                ) as List<Asteroid>
                listAsteroid.addAll(list)
            }
            listResults.value = listAsteroids
        }
    }
}
