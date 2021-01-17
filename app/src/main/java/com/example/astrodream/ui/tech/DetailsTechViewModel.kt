package com.example.astrodream.ui.tech

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.astrodream.entitiesDatabase.Tech
import com.example.astrodream.services.ServiceDatabase
import kotlinx.coroutines.launch

class DetailsTechViewModel(val serviceDatabase: ServiceDatabase) : ViewModel(){
    val listTech = MutableLiveData<List<Tech>>()

    fun addTechDB(tech: Tech) {
        viewModelScope.launch {
            serviceDatabase.addTechTask(tech)
        }
    }

    fun getAllTechnologiesDB() {
        viewModelScope.launch {
            listTech.value = serviceDatabase.getAllTechnologiesTask()
            Log.i("Tech", listTech.value.toString())
        }
    }

    fun deleteAllTechnologiesDB() {
        viewModelScope.launch {
            serviceDatabase.deleteAllTechnologiesTask()
        }
    }
}