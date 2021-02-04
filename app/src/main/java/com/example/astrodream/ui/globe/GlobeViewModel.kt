package com.example.astrodream.ui.globe

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.astrodream.services.Service
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class GlobeViewModel(private val service: Service) : ViewModel() {

    var imageArray = MutableLiveData<MutableList<String>>()
    var epicAvailableDates = MutableLiveData<MutableList<String>>()

    fun getAllEPIC(chosenDate: String) {
        imageArray.value?.clear()

        val imageArrayList = ArrayList<String>()

        viewModelScope.launch {
            try {
                val imageJsonArray = service.getAllEPIC(chosenDate)
                imageJsonArray.forEach {
                    val imageName = it.asJsonObject.get("image").toString().replace("\"", "")
                    imageArrayList.add(imageName)
                }
                imageArray.value = imageArrayList

            } catch (e: Exception) {
                // TODO dar algum feedback na interface para o usuário aqui
                Log.e("GlobeViewModel", "Erro ao carregar imagens: ${e.message}")
            }
        }
    }

    fun getAllAvailableEPIC() {
        epicAvailableDates.value?.clear()
        val epicAvailableList = ArrayList<String>()

        viewModelScope.launch {
            val epicImageJsonArray = service.getAllAvailableEPIC()

            try {
                epicImageJsonArray.forEach {
                    val datesAvailable = it.toString().replace("\"", "")
                    epicAvailableList.add(datesAvailable)
                }
                Log.v("Teste", epicAvailableList.last())
                epicAvailableDates.value = epicAvailableList

            } catch (e: Exception) {
                Log.e("GlobeViewModel", "Erro ao obter lista: ${e.message}")
            }
        }
    }
}