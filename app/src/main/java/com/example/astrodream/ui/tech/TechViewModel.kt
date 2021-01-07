package com.example.astrodream.ui.tech

import com.example.astrodream.services.Service
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.astrodream.domain.TechPiece

abstract class TechViewModel<T : TechPiece>(val service: Service) : ViewModel() {

    abstract val techPieces: MutableLiveData<T>

    abstract fun getTechPieces()
}