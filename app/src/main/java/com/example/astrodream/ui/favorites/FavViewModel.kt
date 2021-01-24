package com.example.astrodream.ui.favorites

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.astrodream.domain.*
import com.example.astrodream.services.*
import kotlinx.coroutines.launch

class FavViewModel(
    val dailyRepository: ServiceDBDaily,
    val techRepository: ServiceDatabaseTech,
    val marsRepository: ServiceDBMars,
    val asteroidRepository: ServiceDBAsteroids
) : ViewModel() {

    var detail = MutableLiveData<Any>() // TODO: criar sealed class??
    var favType = MutableLiveData("daily")
    var favList = MutableLiveData<List<Any>>()
    var hasOngoingQuery = MutableLiveData(false)

    fun selectDetail(detailSelected: Any) {
        detail.value = detailSelected
    }

    fun setFavType(type: String) {
        favType.value = type
    }

    // TODO: dados de exemplo apenas, é necessário buscar dados do db
    // TODO: remover globo?
    fun dummyFavData(type: String) {
        hasOngoingQuery.value = true
        when (type) {
            "daily" -> {
                viewModelScope.launch {
                    val dailyRoomList = dailyRepository.getAllDailyFavsTask()
                    hasOngoingQuery.value = false
                    favList.value = dailyRoomList
                }
            }
            "asteroid" -> {
                viewModelScope.launch {
                    val asteroidRoomList = asteroidRepository.getAllAsteroidsFavsTask()
                    favList.postValue(asteroidRoomList)
                    hasOngoingQuery.value = false
                }
            }
            "tech" -> {
                viewModelScope.launch {
                    hasOngoingQuery.value = false
                    favList.value = techRepository.getAllTechnologiesTask()
                }
            }
            "mars" -> {
                viewModelScope.launch {
                    val marsRoomList = marsRepository.getAllMarsFavsTask()
                    val marsList = mutableListOf<PlainClass>()
                    marsRoomList.forEach { marsRoom ->
                        val imgRoomList = marsRepository.getMarsPicsAtDateTask(marsRoom.earth_date).marsPics
                        val imgList = mutableListOf<MarsImage>()
                        imgRoomList.forEach { marsPicRoom ->
                            imgList.add(MarsImage(marsRoom.sol, Camera("", marsPicRoom.cameraFullName), marsPicRoom.url))
                        }
                        marsList.add(PlainClass(earth_date = marsRoom.earth_date, sol = marsRoom.sol, img_list = imgList as List<MarsImage>, maxTemp = marsRoom.maxTemp, minTemp = marsRoom.minTemp, isFav = true))
                    }
                    hasOngoingQuery.value = false
                    favList.value = marsList
                }
            }
        }
    }

}