package com.example.astrodream.ui.favorites

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.astrodream.domain.PlainClass

class FavViewModel: ViewModel() {

    var detail = MutableLiveData<PlainClass>()
    var favType = MutableLiveData("daily")

    fun selectDetail(detailSelected: PlainClass) {
        detail.value = detailSelected
    }

    fun setFavType(type: String) {
        favType.value = type
    }

}