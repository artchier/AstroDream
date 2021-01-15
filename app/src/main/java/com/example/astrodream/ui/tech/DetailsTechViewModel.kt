package com.example.astrodream.ui.tech

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.astrodream.entitiesDatabase.Tech
import com.example.astrodream.services.ServiceDatabase
import kotlinx.coroutines.launch

class DetailsTechViewModel(private val serviceDatabase: ServiceDatabase) : ViewModel() {
    
    fun addTechDB(tech: Tech) {
        viewModelScope.launch {
            serviceDatabase.addTechTask(tech)
        }
    }

    fun getAllTechnologiesDB() {
        viewModelScope.launch {
            val listTechnologies = serviceDatabase.getAllTechnologiesTask()
            Log.i("Tecnologias", listTechnologies.toString())
        }
    }
}