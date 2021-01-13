package com.example.astrodream.ui.tech

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.astrodream.entitiesDatabase.Tech
import com.example.astrodream.services.ServiceDatabase
import kotlinx.coroutines.launch

class DetailsTechViewModel(val serviceDatabase: ServiceDatabase) : ViewModel(){
    val listTech = MutableLiveData<List<Tech>>()
    lateinit var msg: String

    fun addTechDB(tech: Tech) {
        viewModelScope.launch {

            getAllTechnologiesDB()

            listTech.value?.forEach {
                if (tech.codReferenceTech == it.codReferenceTech) {
                    msg = "Este item já está nos favoritos!"
                    return@launch
                }
            }

            serviceDatabase.addTechTask(tech)
            msg = "Item adicionado aos favoritos com sucesso!"
        }
    }

    fun getAllTechnologiesDB() {
        viewModelScope.launch {
            listTech.value = serviceDatabase.getAllTechnologiesTask()
        }
    }
}