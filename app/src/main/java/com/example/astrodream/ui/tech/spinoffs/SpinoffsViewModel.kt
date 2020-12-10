package com.example.astrodream.ui.tech.spinoffs

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.astrodream.domain.Spinoff
import com.example.astrodream.services.Service
import kotlinx.coroutines.launch

class SpinoffsViewModel(val service: Service) : ViewModel() {
    val spinoffs = MutableLiveData<Spinoff>()

    fun getSpinoffs() {
        viewModelScope.launch {
            val s = service.getSpinoffs("EwczgEoEZWqHcxe6chrFvZR6Dn9ayWi1apxUOUze")
            spinoffs.value = s
        }
    }
}