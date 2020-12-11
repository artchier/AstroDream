package com.example.astrodream.ui.tech.patents

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.astrodream.domain.Patent
import com.example.astrodream.services.Service
import kotlinx.coroutines.launch

class PatentsViewModel(val service: Service) : ViewModel() {
    val patents = MutableLiveData<Patent>()

    fun getPatents() {
        viewModelScope.launch {
            val patentList = service.getPatents()
            patents.value = patentList
        }
    }
}