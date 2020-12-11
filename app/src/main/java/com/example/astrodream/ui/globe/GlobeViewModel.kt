package com.example.astrodream.ui.globe

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.astrodream.services.Service
import kotlinx.coroutines.launch

class GlobeViewModel(private val service: Service) : ViewModel() {

    var imageArray = MutableLiveData<MutableList<String>>()
    private var epicImageArrayList = mutableListOf<Bitmap>()
    var epicImage = MutableLiveData<MutableList<Bitmap>>()

    fun getAllEPIC(chosenDate: String) {
        imageArray.value?.clear()
        epicImageArrayList.clear()

        val imageArrayList = ArrayList<String>()

        viewModelScope.launch {
            try {
                Log.v("GloboActivity", chosenDate)
                val imageJsonArray = service.getAllEPIC(chosenDate)
                imageJsonArray.forEach {
                    val imageName = it.asJsonObject.get("image").toString().replace("\"", "")
                    imageArrayList.add(imageName)
                }
                imageArray.value = imageArrayList

            } catch (e: Exception) {
                Log.e("GlobeViewModel", "Erro ao carregar imagens: ${e.message}")
            }
        }
    }

    fun saveEPIC(resource: Bitmap) {
        viewModelScope.launch {
            epicImageArrayList.add(resource)
            epicImage.value = epicImageArrayList
        }
    }
}