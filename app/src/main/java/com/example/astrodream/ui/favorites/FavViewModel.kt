package com.example.astrodream.ui.favorites

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.astrodream.domain.*
import com.example.astrodream.services.ServiceDBAsteroids
import kotlinx.coroutines.launch

class FavViewModel(val asteroidRepository: ServiceDBAsteroids): ViewModel() {

    var detail = MutableLiveData<Any>() // TODO: criar sealed class??
    var favType = MutableLiveData("asteroid")
    var favList = MutableLiveData<List<Any>>()
    var hasOngoingQuery = MutableLiveData(false)

    fun selectDetail(detailSelected: Any) {
        detail.value = detailSelected
    }

    fun setFavType(type: String) {
        favType.value = type
    }

    fun dummyFavData(type: String) {
        when (type) {
            "daily" -> {
            }
            "asteroid" -> {
                viewModelScope.launch {
                    val asteroidRoomList = asteroidRepository.getAllAsteroidsFavsTask()
                    favList.postValue(asteroidRoomList)
                }
            }
            "globe" -> {
            }

            "tech" -> {
            }

            "mars" -> {
            }
        }
    }

}