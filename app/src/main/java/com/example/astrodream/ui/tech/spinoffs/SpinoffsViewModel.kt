package com.example.astrodream.ui.tech.spinoffs

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.astrodream.domain.Spinoff
import com.example.astrodream.services.Service
import com.example.astrodream.ui.tech.TechViewModel
import kotlinx.coroutines.launch

class SpinoffsViewModel(service: Service) : TechViewModel<Spinoff>(service) {
    override val techPieces = MutableLiveData<Spinoff>()

    override fun getTechPieces() {
        viewModelScope.launch {
            val spinoffList = service.getSpinoffs()
            techPieces.value = spinoffList
        }
    }
}