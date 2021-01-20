package com.example.astrodream.ui.tech

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.astrodream.entitiesDatabase.Tech
import com.example.astrodream.services.ServiceDatabase
import kotlinx.coroutines.launch

class DetailsTechViewModel(var serviceDatabase: ServiceDatabase, val context: Context) : ViewModel(){
    val listTech = MutableLiveData<List<Tech>>()
    val tech = MutableLiveData<Tech>()
    val isFav = MutableLiveData<Boolean>()

    fun favTechDB(tech: Tech) {
        viewModelScope.launch {
            if (serviceDatabase.getTechByCodeTask(tech.codReferenceTech) == null) {
                serviceDatabase.addTechTask(tech)
                isFav.value = true
            } else {
                serviceDatabase.deleteTechTask(tech.codReferenceTech)
                isFav.value = false
            }
        }
    }

    fun addTechDB(tech: Tech) {
        viewModelScope.launch {
            serviceDatabase.addTechTask(tech)
        }
    }

    fun deleteTechDB(codReference: String) {
        viewModelScope.launch {
            serviceDatabase.deleteTechTask(codReference)
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