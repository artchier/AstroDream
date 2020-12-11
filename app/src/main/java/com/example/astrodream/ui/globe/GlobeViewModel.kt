package com.example.astrodream.ui.globe

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.astrodream.services.Service
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch

class GlobeViewModel(private val service: Service) : ViewModel() {
    var imageArray = MutableLiveData<ArrayList<String>>()
    private var epicImageArrayList = ArrayList<Bitmap>()
    var epicImage = MutableLiveData<ArrayList<Bitmap>>()

    fun getAllEPIC(chosenDate: String) {
        imageArray.value?.clear()
        epicImageArrayList.clear()
        val imageArrayList = ArrayList<String>()
        viewModelScope.launch {
            try {
                Log.v("GloboActivity", chosenDate)
                val imageJsonArray =
                    service.getAllEPIC(chosenDate)
                imageJsonArray.forEach {
                    val imageName = it.asJsonObject.get("image").toString().replace("\"", "")
                    //imageName.replace("\"", "")
                    imageArrayList.add(imageName)
                }
                imageArray.value = imageArrayList
                /*if(epicImageArrayList.isEmpty())
                    getEPIC("png", "${imageArrayList[0]}.png")*/
            } catch (ignored: Exception) {
            }
        }
    }

    fun saveEPIC(resource: Bitmap) {
        viewModelScope.launch {
            try {
                epicImageArrayList.add(resource)
                epicImage.value = epicImageArrayList
            } catch (ignored: Exception) {
            }
        }
    }
}