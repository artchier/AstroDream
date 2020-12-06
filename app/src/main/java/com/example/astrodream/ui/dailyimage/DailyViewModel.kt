package com.example.astrodream.ui.dailyimage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.astrodream.domain.DailyImage
import com.example.astrodream.services.Service
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.*

class DailyViewModel(val service: Service): ViewModel() {

    val listResults = MutableLiveData<List<DailyImage>>()
    val focusResult = MutableLiveData<DailyImage>()

    fun popList(dateEnd:String, num: Int) {
        viewModelScope.launch {
//            for (i in 1..num) {
//                var date = Calendar.getInstance()
//                val now = Date()
//                val now.
//
//                val daily = service.getDaily(
//                    dateEnd,
//                    "k070HGqyd0nQeVXvDaMsWeW4Q1aWernx6N4UDsDj"
//                )
//            }

            val day1 = DailyImage(
                "Dark Molecular Cloud Barnard 68",
                "22 de Novembro de 2020",
                "https://apod.nasa.gov/apod/image/2011/barnard68v2_vlt_960.jpg"
            )
            val day2 = DailyImage(
                "Mars and Meteor over Jade Dragon Snow Mountain",
                "21 de Novembro de 2020",
                "https://apod.nasa.gov/apod/image/2011/LeonidmeteorandMarsoverYulongsnowmountain1050.jpg"
            )
            val day3 = DailyImage(
                "Global Map: Mars at Opposition",
                "20 de Novembro de 2020",
                "https://apod.nasa.gov/apod/image/2011/marsglobalmap_1100.jpg"
            )
            val day4 = DailyImage(
                "Crew-1 Mission Launch Streak",
                "19 de Novembro de 2020",
                "https://apod.nasa.gov/apod/image/2011/spacex-crew-1-JenScottPhotography-11_1050.jpg"
            )
            val day5 = DailyImage(
                "A Double Star Cluster in Perseus",
                "18 de Novembro de 2020",
                "https://apod.nasa.gov/apod/image/2011/DoubleCluster_Polanski_960.jpg"
            )
            listResults.value = arrayListOf(
                day1, day2, day3, day4, day5,
                day1, day2, day3, day4, day5,
                day1, day2, day3, day4, day5,
                day1, day2, day3, day4, day5
            )
        }
    }

    fun selectDaily(item: Int) {
        focusResult.value = listResults.value!![item]
    }

    fun selectRoot() {
        focusResult.value = listResults.value!![0]
    }
}