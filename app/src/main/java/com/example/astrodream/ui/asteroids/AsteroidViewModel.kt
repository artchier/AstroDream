package com.example.astrodream.ui.asteroids

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.astrodream.domain.*
import com.example.astrodream.domain.exceptions.InternetConnectionException
import com.example.astrodream.domain.exceptions.ServerErrorException
import com.example.astrodream.domain.exceptions.UnknownErrorException
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
import com.haroldadmin.cnradapter.NetworkResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.lang.Exception
import java.time.LocalDate

class AsteroidViewModel(
    private val serviceAPI: Service,
    private val serviceDB: ServiceDBAsteroids,
    private val reference: DatabaseReference,
    private val context: Context
) : ViewModel() {

    val listAllResultsAPI = MutableLiveData<Asteroid>()
    val listAllAsteroidsAPI = ArrayList<Asteroid>()
    val listResultsDateAPI = MutableLiveData<AsteroidRes>()
    val listAsteroidsDateAPI = ArrayList<Asteroid>()
    var listAllAsteroidsDB = ArrayList<AsteroidRoom>()
    var oneAsteroidFromAPI = MutableLiveData<Asteroid>()

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAsteroidsDate() {
         viewModelScope.launch {
            try {
                getAsteroidsDateAPI()
            } catch (e: ServerErrorException) {
                e.showImageServerError(context)
            } catch (e: InternetConnectionException) {
                e.showImageWithoutInternetConnection(context)
            } catch (e: UnknownErrorException) {
                e.showImageUnknownError(context)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun getAsteroidsDateAPI() {
        when (val listAsteroidsDate =
            serviceAPI.getAsteroidsDate(LocalDate.now().toString())) {
            is NetworkResponse.Success -> {
                listAsteroidsDate.body.near_earth_objects.keySet().toList().forEach {
                    val list = Gson().fromJson(
                        listAsteroidsDate.body.near_earth_objects.get(it),
                        object : TypeToken<List<AsteroidData>>() {}.type
                    ) as List<AsteroidData>
                    listAsteroidsDateAPI.addAll(list.map { a -> a.getAsteroid() })
                }
                listResultsDateAPI.postValue(listAsteroidsDate.body)
            }
            is NetworkResponse.ServerError -> {
                throw ServerErrorException()
            }
            is NetworkResponse.NetworkError -> {
                throw InternetConnectionException()
            }
            is NetworkResponse.UnknownError -> {
                throw UnknownErrorException()
            }
        }
    }

    fun getOneAsteroById(id: Int) {
         viewModelScope.launch {
             try {
                 getOneAsteroByIdAPI(id)
             } catch (e: InternetConnectionException) {
                 e.showImageWithoutInternetConnection(context)
             } catch (e: ServerErrorException) {
                 e.showImageServerError(context)
             } catch (e: UnknownErrorException) {
                 e.showImageUnknownError(context)
             }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun getOneAsteroByIdAPI(id: Int) {
        when (val asteroid = serviceAPI.getAsteroidId(id)) {
            is NetworkResponse.Success -> {
                oneAsteroidFromAPI.value = asteroid.body.getAsteroid()
            }
            is NetworkResponse.ServerError -> {
                oneAsteroidFromAPI.value = null
                throw ServerErrorException()
            }
            is NetworkResponse.NetworkError -> {
                oneAsteroidFromAPI.value = null
                throw InternetConnectionException()
            }
            is NetworkResponse.UnknownError -> {
                oneAsteroidFromAPI.value = null
                throw UnknownErrorException()
            }
        }
    }

    // ####### OPÇÕES DO BANCO DE DADOS LOCAL #######

    private fun addAsteroidDB(asteroid: AsteroidRoom) {
        viewModelScope.launch {
            featureaddAsteroidDB(asteroid)
        }
    }

    fun getAllAsteroidsDB() {
        viewModelScope.launch {
            listAllAsteroidsDB = featuregetAllAsteroidsDB() as ArrayList<AsteroidRoom>
        }
    }

    private fun deleteAsteroidsDB(asteroid: AsteroidRoom) {
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

    fun addAsteroidInDB(asteroid: AsteroidRoom) {
        listAllAsteroidsDB.add(asteroid)
        addAsteroidDB(asteroid)
    }

    fun deleteAsteroidInDB(asteroid: AsteroidRoom) {
        listAllAsteroidsDB.remove(asteroid)
        deleteAsteroidsDB(asteroid)
    }

    // ####### OPÇÕES DO BANCO DE DADOS FIREBASE #######

    fun getListAsteroidesFromFirebase() {
        viewModelScope.launch {
            reference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    dataSnapshot.children.forEach {
                        val result = it.value as HashMap<String, String>
                        val data = result["first_obs"]!!.split("-")
                            val asteroid = Asteroid(
                                it.key!!, result["full_name"]!!,
                                AstroDreamUtil.isPotentiallyHazardousAsteroid(result["pha"]!!),
                                null, null, if (data.size == 3)
                                AstroDreamUtil.formatDate(
                                    data[2],
                                    data[1],
                                    data[0]
                                ) else "",
                                null, null,
                                null, null
                            )
                        listAllAsteroidsAPI.add(asteroid)
                        listAllResultsAPI.postValue(asteroid)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Erro", error.toString())
                }
            })
        }
    }
}