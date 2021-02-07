package com.example.astrodream.ui.globe

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.astrodream.services.Service
import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.cnradapter.executeWithRetry
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList

class GlobeViewModel(private val service: Service) : ViewModel() {

    var imageArray = MutableLiveData<MutableList<String>>()
    var epicAvailableDates = MutableLiveData<MutableList<String>>()
    val hasInternetConnection = MutableLiveData(true)
    val unknownErrorAPI = MutableLiveData(false)

    fun getAllEPIC(chosenDate: String) {
        imageArray.value?.clear()

        val imageArrayList = ArrayList<String>()

        viewModelScope.launch {
            val imageJsonArray = executeWithRetry(times = 5) {
                service.getAllEPIC(chosenDate)
            }
            when (imageJsonArray) {
                is NetworkResponse.Success -> {
                    imageJsonArray.body.forEach {
                        val imageName = it.asJsonObject.get("image").toString().replace("\"", "")
                        imageArrayList.add(imageName)
                    }
                    imageArray.value = imageArrayList
                }
                is NetworkResponse.ServerError -> {
                    unknownErrorAPI.value = true
                }
                is NetworkResponse.NetworkError -> {
                    hasInternetConnection.value = false
                }
                is NetworkResponse.UnknownError -> {
                    unknownErrorAPI.value = true
                }
            }
        }
    }

    fun getAllAvailableEPIC() {
        epicAvailableDates.value?.clear()
        val epicAvailableList = ArrayList<String>()

        viewModelScope.launch {
            val epicImageJsonArray = executeWithRetry(times = 5) {
                service.getAllAvailableEPIC()
            }
            when (epicImageJsonArray) {
                is NetworkResponse.Success -> {
                    hasInternetConnection.value = true
                    epicImageJsonArray.body.forEach {
                        val datesAvailable = it.toString().replace("\"", "")
                        epicAvailableList.add(datesAvailable)
                    }
                    epicAvailableDates.value = epicAvailableList
                }
                is NetworkResponse.ServerError -> {
                    unknownErrorAPI.value = true
                }
                is NetworkResponse.NetworkError -> {
                    hasInternetConnection.value = false
                }
                is NetworkResponse.UnknownError -> {
                    unknownErrorAPI.value = true
                }
            }
        }
    }
}