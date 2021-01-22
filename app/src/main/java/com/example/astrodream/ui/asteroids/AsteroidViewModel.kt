package com.example.astrodream.ui.asteroids

import android.content.Context
import android.os.Build
import android.view.View
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.astrodream.R
import com.example.astrodream.domain.*
import com.example.astrodream.entitiesDatabase.AsteroidRoom
import com.example.astrodream.services.Service
import com.example.astrodream.services.ServiceDBAsteroids
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import java.time.LocalDate

class AsteroidViewModel(
    private val serviceAPI: Service,
    private val serviceDB: ServiceDBAsteroids,
    private val context: Context
) : ViewModel() {

    val listResults = MutableLiveData<AsteroidRes>()
    val listAsteroid = ArrayList<Asteroid>()

    @RequiresApi(Build.VERSION_CODES.O)
    fun execute(v: View) = viewModelScope.launch {
        if (listAsteroid.isEmpty()) onPreExecute(v)
        doInBackground()
        onPostExecute(v)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun doInBackground() {
        viewModelScope.launch {
            val listAsteroids =
                serviceAPI.getAsteroidsDate(
                    LocalDate.now().toString(),
                    context.getString(R.string.api_key)
                )

            listAsteroids.near_earth_objects.keySet().toList().forEach {
                val list = Gson().fromJson(
                    listAsteroids.near_earth_objects.get(it),
                    object : TypeToken<List<AsteroidData>>() {}.type
                ) as List<AsteroidData>
                listAsteroid.addAll(list.map { a -> a.getAsteroid() })
            }

            listResults.postValue(listAsteroids)
        }
    }

    private fun onPreExecute(v: View) {
        v.findViewById<ProgressBar>(R.id.progressbar_asteroides).visibility = ProgressBar.VISIBLE
    }

    private fun onPostExecute(v: View) {
        v.findViewById<ProgressBar>(R.id.progressbar_asteroides).visibility = ProgressBar.GONE
    }


    // ####### OPÇÕES DO BANCO DE DADOS #######

    fun addAsteroidDB(asteroid: AsteroidRoom) {
        viewModelScope.launch {
            serviceDB.addAsteroidTask(asteroid)
        }
    }

    fun getAllAsteroidsDB(): List<AsteroidRoom> {
        var listAsteroid: List<AsteroidRoom>? = null
        viewModelScope.launch {
            listAsteroid = serviceDB.getAllAsteroidsFavsTask()
        }
        return listAsteroid ?: listOf()
    }

    fun deleteAsteroidsDB(asteroid: AsteroidRoom) {
        viewModelScope.launch {
            serviceDB.deleteAsteroidsTask(asteroid)
        }
    }
}