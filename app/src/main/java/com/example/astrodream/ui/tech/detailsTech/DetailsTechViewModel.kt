package com.example.astrodream.ui.tech.detailsTech

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.astrodream.entitiesDatabase.Tech
import com.example.astrodream.services.ServiceDatabaseTech
import kotlinx.coroutines.launch

class DetailsTechViewModel(var serviceDatabaseTech: ServiceDatabaseTech, val context: Context) : ViewModel(){
    private val listTech = MutableLiveData<List<Tech>>()
    val tech = MutableLiveData<Tech>()
    val isFav = MutableLiveData<Boolean>()

    fun favTechDB(tech: Tech) {
        viewModelScope.launch {
            if (serviceDatabaseTech.getTechByCodeTask(tech.codReferenceTech) == null) {
                serviceDatabaseTech.addTechTask(tech)
                isFav.value = true
            } else {
                serviceDatabaseTech.deleteTechTask(tech.codReferenceTech)
                isFav.value = false
            }
        }
    }

    fun deleteAllTechnologiesDB() {
        viewModelScope.launch {
            serviceDatabaseTech.deleteAllTechnologiesTask()
        }
    }

    fun deleteTechDB(codReference: String) {
        viewModelScope.launch {
            serviceDatabaseTech.deleteTechTask(codReference)
        }
    }

    fun getAllTechnologiesDB() {
        viewModelScope.launch {
            listTech.value = serviceDatabaseTech.getAllTechnologiesTask()
        }
    }

    fun getTechByCodeDB(codReference: String) {
        viewModelScope.launch {
            tech.value = serviceDatabaseTech.getTechByCodeTask(codReference)
        }
    }
}