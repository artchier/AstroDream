package com.example.astrodream.ui.plaindailymars

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.astrodream.domain.MarsImage
import com.example.astrodream.domain.PlainClass
import com.example.astrodream.domain.TempSol
import com.example.astrodream.services.Service
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate

class PlainViewModel(val service: Service, private val type: PlainActivityType): ViewModel() {

    val listResults = MutableLiveData<PlainClass>()
    val focusResult = MutableLiveData<PlainClass>()
    val hasOngoingRequest = MutableLiveData<Boolean>()
    private var numFetches = 0
    private val timesToFetch = 24

    private var date: LocalDate = LocalDate.now()
    private lateinit var detail: PlainClass
    private lateinit var detailRoot: PlainClass

    private var listTemp: MutableList<TempSol> = mutableListOf()
    private var availableTempListLong = listOf<Long>()

    fun populateList() {
        hasOngoingRequest.value = true

        viewModelScope.launch {
            if (numFetches == 0) {
                listResults.value = PlainClass()

                if (type == PlainActivityType.DailyImage) {
                    fetchDailyImages()

                } else if (type == PlainActivityType.Mars) {
                    fetchTemperatures()
                    fetchRoverPics()

                    if (detail.sol in availableTempListLong) {
                        val tempSolCurr = listTemp.filter {it.solMars == detail.sol}[0]
                        detail.maxTemp = "Max: " + tempSolCurr.maxTempMars + "°C"
                        detail.minTemp = "Min: " + tempSolCurr.minTempMars + "°C"
                    }
                }

                listResults.value = detail
                date = date.minusDays(1)
                detailRoot = detail
                focusResult.value = detailRoot
                numFetches++

            } else {
                if (type == PlainActivityType.DailyImage) {
                    for (i in 1..timesToFetch) {
                        fetchDailyImages()
                        listResults.value = detail
                        date = date.minusDays(1)
                    }
                    numFetches++

                } else if (type == PlainActivityType.Mars) {
                    for (i in 1..timesToFetch) {
                        val dummy = getMars(date.toString())

                        if (dummy != null) {
                            detail = dummy
                            Log.i("===ViewModel====", detail.sol.toString())
                            Log.i("===ViewModel====", availableTempListLong.toString())

                            if (detail.sol in availableTempListLong) {
                                val tempSolCurr = listTemp.filter {it.solMars == detail.sol}[0]
                                detail.maxTemp = "Max: " + tempSolCurr.maxTempMars + "°C"
                                detail.minTemp = "Min: " + tempSolCurr.minTempMars + "°C"
                            }

                            listResults.value = detail
                            date = date.minusDays(1)

                        } else {
                            date = date.minusDays(1)
                            continue
                        }
                    }
                    numFetches++
                }
            }
            hasOngoingRequest.value = false
        }
    }

    fun selectDetail(detail: PlainClass) {
        focusResult.value = detail
    }

    fun selectRoot() {
        focusResult.value = detailRoot
    }

    private suspend fun fetchRoverPics() {
        var dummy = getMars(date.toString())

        while (dummy == null) {
            date = date.minusDays(1)
            dummy = getMars(date.toString())
        }
        detail = dummy
    }

    private suspend fun fetchDailyImages() {
        var dummy = service.getDaily(date.toString())

        while (dummy.url.contains("youtube")) {
            date = date.minusDays(1)
            dummy = service.getDaily(date.toString())
        }

        detail = dummy
    }

    private suspend fun fetchTemperatures() {
        val temperatureJson = service.getMarsTemp("json", "1.0")
        val availableTempJson = temperatureJson.get("sol_keys")

        val availableTempListString = Gson().fromJson(
            availableTempJson,
            object : TypeToken<List<String>>() {}.type
        ) as List<String>

        availableTempListLong = availableTempListString.map { it.toLong() + 2241 }

        for (s in availableTempListString) {
            val tempSolCurr = Gson().fromJson(temperatureJson.get(s), object : TypeToken<TempSol>() {}.type) as TempSol
            tempSolCurr.wakeUp(s.toLong() + 2241)
            listTemp.add(tempSolCurr)
        }
    }

    private suspend fun getMars(earth_date: String): PlainClass? {
        val responseRover = service.getMars(earth_date)
        val photos = responseRover.get("photos")
        val marsImageList = Gson().fromJson(photos, object : TypeToken<List<MarsImage>>(){}.type) as List<MarsImage>
        val imgList = mutableListOf<String>()
        val checkerMars = mutableListOf<String>()
        var sol: Long = 1
        marsImageList.forEach {
            if (it.camera.name !in checkerMars) {
                imgList.add(it.img_src)
                checkerMars.add(it.camera.name)
            }
        }
        if (imgList.isNotEmpty()) sol = marsImageList[0].sol

        return if (imgList.isEmpty()) {
            null
        } else {
            PlainClass(earth_date = earth_date, sol = sol, img_list = imgList)
        }
    }

}
