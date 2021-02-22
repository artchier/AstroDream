package com.example.astrodream.ui.tech.softwares

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.astrodream.domain.Software
import com.example.astrodream.services.Service
import com.example.astrodream.ui.tech.TechViewModel
import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.cnradapter.executeWithRetry
import kotlinx.coroutines.launch

class SoftwaresViewModel(service: Service) : TechViewModel<Software>(service) {

    override val techPieces = MutableLiveData<Software>()

    override fun getTechPieces() {
        viewModelScope.launch {
            val softwareList = executeWithRetry(times = 5) {
                service.getSoftwares()
            }
            when (softwareList) {
                is NetworkResponse.Success -> {
                    techPieces.value = softwareList.body
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