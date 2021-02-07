package com.example.astrodream.ui.asteroids

import android.os.Build
import android.util.AndroidRuntimeException
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.astrodream.R
import com.example.astrodream.domain.*
import com.example.astrodream.domain.util.AstroDreamUtil
import com.example.astrodream.domain.util.formatDate
import com.example.astrodream.domain.util.isPotentiallyHazardousAsteroid
import com.example.astrodream.entitiesDatabase.AsteroidRoom
import com.example.astrodream.services.Service
import com.example.astrodream.services.ServiceDBAsteroids
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.lang.Exception
import java.time.LocalDate

class AsteroidViewModel(
    private val serviceAPI: Service,
    private val serviceDB: ServiceDBAsteroids,
    private val reference: DatabaseReference
) : ViewModel() {

    val listAllResultsAPI = MutableLiveData<Asteroid>()
    val listAllAsteroidsAPI = ArrayList<Asteroid>()
    val listResultsDateAPI = MutableLiveData<AsteroidRes>()
    val listAsteroidsDateAPI = ArrayList<Asteroid>()
    var listAllAsteroidsDB = ArrayList<AsteroidRoom>()
    var oneAsteroidFromAPI = MutableLiveData<Asteroid>()

    @RequiresApi(Build.VERSION_CODES.O)
    fun execute() = viewModelScope.launch {
        doInBackground()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun doInBackground() {
        viewModelScope.launch {
            val listAsteroidsDate =
                serviceAPI.getAsteroidsDate(LocalDate.now().toString())

            listAsteroidsDate.near_earth_objects.keySet().toList().forEach {
                val list = Gson().fromJson(
                    listAsteroidsDate.near_earth_objects.get(it),
                    object : TypeToken<List<AsteroidData>>() {}.type
                ) as List<AsteroidData>
                listAsteroidsDateAPI.addAll(list.map { a -> a.getAsteroid() })
            }
            listResultsDateAPI.postValue(listAsteroidsDate)
        }
    }

    private fun onPreExecute(v: View) {
        v.findViewById<ProgressBar>(R.id.progressbar_asteroides).visibility = ProgressBar.VISIBLE
    }

    private fun onPostExecute(v: View) {
        v.findViewById<ProgressBar>(R.id.progressbar_asteroides).visibility = ProgressBar.GONE
    }

    // ####### OPÇÕES DO BANCO DE DADOS LOCAL #######

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

    // ####### OPÇÕES DO BANCO DE DADOS FIREBASE #######

    fun getListAsteroidesFromFirebase(){
        viewModelScope.launch {
            reference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    dataSnapshot.children.forEach {
                        val result = it.value as HashMap<*, *>
                        val data = result["first_obs"]!!.toString().split("-")
                        try {
                            val asteroid = Asteroid(
                                it.key!!, result["full_name"]!!.toString(),
                                AstroDreamUtil.isPotentiallyHazardousAsteroid(result["pha"]!!.toString()),
                                null, null,
                                AstroDreamUtil.formatDate(data[2].toInt(), data[1].toInt(), data[0].toInt()),
                                null, null,
                                null, null
                            )
                            //Log.i("-----asteroide-------", asteroid.toString()
                            listAllAsteroidsAPI.add(asteroid)
                            listAllResultsAPI.postValue(asteroid)
                        } catch (ignored: Exception) {
                            Log.w("AsteroidViewModel", "Erro ao dar parse na data: $data")
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Erro", error.toString())
                }
            })
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getOneAsteroById(id: Int){
            viewModelScope.launch {
                try {
                    val asteroid = serviceAPI.getAsteroidId(id)
                    oneAsteroidFromAPI.value = asteroid.getAsteroid()
                } catch (e: HttpException){
                    Log.w("AsteroidViewModel", "Erro ao obter asteroide\n${e.stackTrace}")
                    oneAsteroidFromAPI.value = null
                }
            }
    }
}