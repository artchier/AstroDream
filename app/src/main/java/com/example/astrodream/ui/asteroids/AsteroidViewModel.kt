package com.example.astrodream.ui.asteroids

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.astrodream.domain.*
import com.example.astrodream.services.Service
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate

class AsteroidViewModel(val service: Service) : ViewModel() {

    val listResults = MutableLiveData<AsteroidRes>()
    val listAsteroid = ArrayList<Asteroid>()

    fun popListResult() {
        viewModelScope.launch {
            val listAsteroids = service.getAsteroids("2020-12-11", "X6SUDXPVkjgyQoyKVunHMpwomboitIigBRVCSK1M")
            Log.i("TAG", listAsteroids.toString())

           // val near_earth_objects = listAsteroids.near_earth_objects

            for(je in listAsteroids.near_earth_objects.keySet().toList()){
                val list = Gson().fromJson(listAsteroids.near_earth_objects.get(je),
                    object : TypeToken<List<Asteroid>>() {}.type) as List<Asteroid>
                listAsteroid.addAll(list)
                Log.i("TAG", list.size.toString())
            }
            Log.i("TAG", listAsteroid.size.toString())

//            lisAsteroids.forEach { list.add(Asteroid(it.asJsonObject.get("name").toString(),
//                it.asJsonObject.get("is_potentially_hazardous_asteroid").asBoolean,
//                it.asJsonObject.get("absolute_magnitude_h").asDouble,
//                it.asJsonObject.get("relative_velocity") as AsteroidVelocidade,
//                it.asJsonObject.get("close_approach_data").asJsonArray,
//                it.asJsonObject.get("miss_distance") as AsteroidDistancia,
//                it.asJsonObject.get("orbiting_body").toString()))
//            }
      //      val list2 = listAsteroids.near_earth_objects.keySet().toList()

//            val list = ArrayList<Asteroid>()
//            val data = Asteroids2(Data(lisAsteroids.toList() as ArrayList<Asteroid>))
 //           Log.i("TAG", data.data.toString())
//            var dateTT = LocalDate.now()
//            val response = service.getResults(
//                dateTT.toString(),
//                dateTT.minusDays(2).toString(),
//                "X6SUDXPVkjgyQoyKVunHMpwomboitIigBRVCSK1M"
//            )
//            for (i in 1..3) {
//                val responseAtDate = response.get("near_earth_objects").asJsonObject.get(dateTT.toString()).asJsonArray
//                val totalAsteroids = Gson().fromJson(responseAtDate,
//                    object : TypeToken<List<Asteroid>>() {}.type) as List<Asteroid>
//                listAsteroids.addAll(totalAsteroids)
//                dateTT = dateTT.minusDays(1)
//            }
//
//            val asteroids = AsteroidRes((DatalistAsteroids.toList() as ArrayList<Asteroid>))

            listResults.value = listAsteroids
        }
    }
}
