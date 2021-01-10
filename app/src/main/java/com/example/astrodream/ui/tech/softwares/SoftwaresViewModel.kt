package com.example.astrodream.ui.tech.softwares

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.astrodream.domain.Software
import com.example.astrodream.services.Service
import com.example.astrodream.ui.tech.TechViewModel
import kotlinx.coroutines.launch

class SoftwaresViewModel(service: Service) : TechViewModel<Software>(service) {

    override val techPieces = MutableLiveData<Software>()

    override fun getTechPieces() {
        viewModelScope.launch {
            val softwareList = service.getSoftwares()
            techPieces.value = softwareList
        }
    }
}