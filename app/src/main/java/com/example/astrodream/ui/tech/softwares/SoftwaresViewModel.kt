package com.example.astrodream.ui.tech.softwares

import android.util.Log
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
            val s = service.getSoftwares("EwczgEoEZWqHcxe6chrFvZR6Dn9ayWi1apxUOUze")
            Log.i("Patent", s.toString())
            softwares.value = s
        }
    }
}