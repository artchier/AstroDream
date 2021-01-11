package com.example.astrodream.ui.tech.patents

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.astrodream.domain.Patent
import com.example.astrodream.services.Service
import com.example.astrodream.ui.tech.TechViewModel
import kotlinx.coroutines.launch

class PatentsViewModel(service: Service) : TechViewModel<Patent>(service) {

    override val techPieces = MutableLiveData<Patent>()

    override fun getTechPieces() {
        viewModelScope.launch {
            val patentList = service.getPatents()
            techPieces.value = patentList
        }
    }
}