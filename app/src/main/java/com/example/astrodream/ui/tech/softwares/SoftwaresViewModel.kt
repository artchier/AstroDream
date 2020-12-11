package com.example.astrodream.ui.tech.softwares

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.astrodream.domain.Software
import com.example.astrodream.services.Service
import kotlinx.coroutines.launch

class SoftwaresViewModel(val service: Service) : ViewModel() {
    val softwares = MutableLiveData<Software>()

    fun getSoftwares() {
        viewModelScope.launch {
            val softwareList = service.getSoftwares()
            softwares.value = softwareList
        }
    }
}