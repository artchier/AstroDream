package com.example.astrodream.ui.tech

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.astrodream.entitiesDatabase.Tech
import com.example.astrodream.services.ServiceDatabase
import kotlinx.coroutines.launch

class DetailsTechViewModel(var serviceDatabase: ServiceDatabase) : ViewModel(){
    val listTech = MutableLiveData<List<Tech>>()
    val tech = MutableLiveData<Tech>()

    fun addTechDB(tech: Tech) {
        viewModelScope.launch {
            serviceDatabase.addTechTask(tech)
        }
    }

    fun getAllTechnologiesDB() {
        viewModelScope.launch {
            listTech.value = serviceDatabase.getAllTechnologiesTask()
        }
    }

    fun getTechByCodeDB(codReference: String) {
        viewModelScope.launch {
            tech.value = serviceDatabase.getTechByCodeTask(codReference)
        }
    }

    fun deleteAllTechnologiesDB() {
        viewModelScope.launch {
            serviceDatabase.deleteAllTechnologiesTask()
        }
    }
}