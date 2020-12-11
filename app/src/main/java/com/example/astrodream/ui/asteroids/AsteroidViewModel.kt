package com.example.astrodream.ui.asteroids

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.astrodream.domain.AsteroidRes
import com.example.astrodream.domain.Repository
import kotlinx.coroutines.launch

class AsteroidViewModel(val repository: Repository) : ViewModel() {

    val listResults = MutableLiveData<AsteroidRes>()

    fun popListResult(date: String) {
        viewModelScope.launch {
            val asteroids = repository.getResults(
                date,
                "X6SUDXPVkjgyQoyKVunHMpwomboitIigBRVCSK1M"
            )
            listResults.value = asteroids
        }
    }
}
