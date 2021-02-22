package com.example.astrodream.ui.tech.patents

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.astrodream.domain.Patent
import com.example.astrodream.services.Service
import com.example.astrodream.ui.tech.TechViewModel
import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.cnradapter.executeWithRetry
import kotlinx.coroutines.launch

class PatentsViewModel(service: Service) : TechViewModel<Patent>(service) {

    override val techPieces = MutableLiveData<Patent>()

    override fun getTechPieces() {
        viewModelScope.launch {
            val patentList = executeWithRetry(times = 5) {
                service.getPatents()
            }
            when (patentList) {
                is NetworkResponse.Success -> {
                    techPieces.value = patentList.body
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