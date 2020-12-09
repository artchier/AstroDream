package com.example.astrodream.ui.tech.patents

import android.util.Log
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
            val p = service.getPatents("EwczgEoEZWqHcxe6chrFvZR6Dn9ayWi1apxUOUze")
            Log.i("Patent", p.toString())
            patents.value = p
        }
    }
}