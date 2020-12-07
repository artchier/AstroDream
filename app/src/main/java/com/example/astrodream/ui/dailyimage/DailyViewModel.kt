package com.example.astrodream.ui.dailyimage

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.astrodream.domain.DailyImage
import com.example.astrodream.services.Service
import com.google.gson.Gson
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import java.util.*

class DailyViewModel(val service: Service): ViewModel() {

    val listResults = MutableLiveData<List<DailyImage>>()
    val focusResult = MutableLiveData<DailyImage>()
    private var numFetches = 0
    private val num = 24

    var date = LocalDate.now()
    lateinit var daily: DailyImage
    lateinit var dailyRoot: DailyImage

    fun popList() {
        if (numFetches == 0) {
            var listFetched: MutableList<DailyImage> = mutableListOf()
            viewModelScope.launch {
                Log.i("FAZENDO A REQUISICAO", date.toString())
                daily = service.getDaily(
                    date.toString(),
                    "k070HGqyd0nQeVXvDaMsWeW4Q1aWernx6N4UDsDj"
                )
                listFetched.add(daily)
                date = date.minusDays(1)
                listResults.value = listFetched
                dailyRoot = listResults.value!![0]
                numFetches++
                popList()
            }
        } else {
            var listFetched: MutableList<DailyImage> = mutableListOf()
            viewModelScope.launch {
                for (i in 1..num) {
                    Log.i("FAZENDO A REQUISICAO", date.toString())
                    daily = service.getDaily(
                        date.toString(),
                        "k070HGqyd0nQeVXvDaMsWeW4Q1aWernx6N4UDsDj"
                    )
                    listFetched.add(daily)
                    date = date.minusDays(1)
                }
                listResults.value = listFetched
            }
            numFetches++
        }
    }

    fun selectDaily(daily: DailyImage) {
        focusResult.value = daily
    }

    fun selectRoot() {
        focusResult.value = dailyRoot
    }
}