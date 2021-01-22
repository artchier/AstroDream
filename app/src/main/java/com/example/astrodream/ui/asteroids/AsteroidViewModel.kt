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
    var listAllAsteroidsDB = ArrayList<AsteroidRoom>()

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

    private fun addAsteroidDB (asteroid: AsteroidRoom){
        viewModelScope.launch {
            featureaddAsteroidDB(asteroid)
        }
    }

    fun getAllAsteroidsDB(){
        viewModelScope.launch {
           listAllAsteroidsDB = featuregetAllAsteroidsDB() as ArrayList<AsteroidRoom>
        }
    }

    private fun deleteAsteroidsDB(asteroid: AsteroidRoom){
        viewModelScope.launch {
            featuredeleteAsteroidsDB(asteroid)
        }
    }

    private suspend fun featureaddAsteroidDB(asteroid: AsteroidRoom) {
            serviceDB.addAsteroidTask(asteroid)
    }

    private suspend fun featuregetAllAsteroidsDB(): List<AsteroidRoom> {
        return serviceDB.getAllAsteroidsFavsTask()
    }

    private suspend fun featuredeleteAsteroidsDB(asteroid: AsteroidRoom) {
            serviceDB.deleteAsteroidsTask(asteroid)
    }

    fun addAsteroidInDB(asteroid: AsteroidRoom){
        listAllAsteroidsDB.add(asteroid)
        addAsteroidDB(asteroid)
    }

    fun deleteAsteroidInDB(asteroid: AsteroidRoom){
        listAllAsteroidsDB.remove(asteroid)
        deleteAsteroidsDB(asteroid)
    }
}