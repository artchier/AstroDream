package com.example.astrodream.ui.tech.spinoffs

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.astrodream.domain.Spinoff
import com.example.astrodream.services.Service
import com.example.astrodream.ui.tech.TechViewModel
import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.cnradapter.executeWithRetry
import kotlinx.coroutines.launch

class SpinoffsViewModel(service: Service) : TechViewModel<Spinoff>(service) {
    override val techPieces = MutableLiveData<Spinoff>()

    override fun getTechPieces() {
        viewModelScope.launch {
            val spinoffList = executeWithRetry(times = 5) {
                service.getSpinoffs()
            }
            when (spinoffList) {
                is NetworkResponse.Success -> {
                    techPieces.value = spinoffList.body
                }
                is NetworkResponse.ServerError -> {
                    unknownErrorAPI.value = true
                }
                is NetworkResponse.NetworkError -> {
                    hasInternetConnection.value = false
                }
                is NetworkResponse.UnknownError -> {
                    unknownErrorAPI.value = true
                }
            }
        }
    }
}