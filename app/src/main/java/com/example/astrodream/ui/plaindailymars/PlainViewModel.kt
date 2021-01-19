package com.example.astrodream.ui.plaindailymars

import android.util.Log
import android.view.View
import android.widget.ToggleButton
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.astrodream.domain.Camera
import com.example.astrodream.domain.MarsImage
import com.example.astrodream.domain.PlainClass
import com.example.astrodream.domain.TempSol
import com.example.astrodream.entitiesDatabase.MarsRoom
import com.example.astrodream.services.Service
import com.example.astrodream.services.ServiceDBMars
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.haroldadmin.cnradapter.NetworkResponse
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

class PlainViewModel(val service: Service, private val type: PlainActivityType, val repository: ServiceDBMars): ViewModel() {

    val listFavsMars = MutableLiveData<List<MarsRoom>>()

    val focusResult = MutableLiveData<PlainClass>()
    val hasOngoingRequest = MutableLiveData<Boolean>()
    // TODO: implementar o hasInternetConnection para abrir dialog caso nao haja conexao.
    // TODO: ao dar "OK" no dialog, voltar para Initial
    val hasInternetConnection = MutableLiveData(true)

    private var numFetches = 0
    private var timesToFetch = 24

    // RecyclerView adapter
    var adapterHistory = PlainAdapter()

    // List of dates with available temperature
    private var availableTempListLong = listOf<Long>()
    // List of available temperature
    private var listTemp: MutableList<TempSol> = mutableListOf()

    // List of empty shimmering views
    private var emptyDummyList = MutableList(size = timesToFetch) { PlainClass() }

    // Initial date
    var date: LocalDate = LocalDate.now().plusDays(2)
    // Date formatter
    private val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    // Current detail request
    private lateinit var detail: PlainClass
    // First detail request (corresponding to initial date)
    private lateinit var detailRoot: PlainClass


    fun populateList() {

        hasOngoingRequest.value = true
        adapterHistory.addList(emptyDummyList)

        viewModelScope.launch {
            when(type) {
                PlainActivityType.Initial -> {
                    fetchDailyImage()
                    focusResult.value = detail
                }

                PlainActivityType.DailyImage -> {
                    for (i in 1..timesToFetch) {
                        fetchDailyImage()
                    }
                }

                PlainActivityType.Mars -> {
                    for (i in 1..timesToFetch) {
                        fetchRoverPics()
                    }
                }
            }
            numFetches++
            hasOngoingRequest.value = false
        }
    }

    private suspend fun fetchDailyImage() {
        when (val response = service.getDaily(date.toString())) {
            is NetworkResponse.Success -> {
                // Handle successful response
                if ((response.body.url).contains("youtube")) {
                    date = date.minusDays(1)
                    fetchDailyImage()
                } else {
                    detail = response.body
                    detail.date = date.format(dateFormatter).toString()
                    if (!::detailRoot.isInitialized) {
                        detailRoot = detail
                        focusResult.value = detailRoot
                    }
                    adapterHistory.replaceItem(detail)
                    date = date.minusDays(1)
                }
            }
            is NetworkResponse.ServerError -> {
                // Handle server error (unavailable date falls into ServerError)
                date = date.minusDays(1)
                fetchDailyImage()
            }
            is NetworkResponse.NetworkError -> {
                // Handle network error
                detail = PlainClass()
                if (!::detailRoot.isInitialized) {
                    detailRoot = detail
                    focusResult.value = detailRoot
                }
                if(hasInternetConnection.value!!) { hasInternetConnection.value = false }
            }
            is NetworkResponse.UnknownError -> {
                // Handle other errors
                detail = PlainClass()
                if (!::detailRoot.isInitialized) {
                    detailRoot = detail
                    focusResult.value = detailRoot
                }
            }
        }
    }

    private suspend fun fetchRoverPics() {
        if (numFetches == 0) {
            fetchTemperatures()
        }
        // Request API
        when (val responseRover = service.getMars(date.toString())) {
            is NetworkResponse.Success -> {
                // Handle successful response
                // Get JSON element of photos
                val photos = responseRover.body.get("photos")
                // Deserialize the photos JsonElement into a List<MarsImage>
                var marsImageList = Gson().fromJson(photos, object : TypeToken<List<MarsImage>>(){}.type) as MutableList<MarsImage>
                // If current date does not contain photos, new request
                if (marsImageList.isEmpty()) {
                    date = date.minusDays(1)
                    fetchRoverPics()
                }
                else {
                    // Get a shorter list of images by taking only one picture from each camera
                    val picsToRemove = mutableListOf<MarsImage>()
                    val listOfTakenCameras = mutableListOf<String>()
                    marsImageList.forEach {
                        if (it.camera.name in listOfTakenCameras) {
                            picsToRemove.add(it)
                        } else {
                            listOfTakenCameras.add(it.camera.name)
                        }
                    }
                    marsImageList.removeAll(picsToRemove)
                    marsImageList = marsImageList.shuffled() as MutableList<MarsImage>

                    detail = PlainClass(earth_date = date.toString(), sol = marsImageList[0].sol, img_list = marsImageList)

                    if (detail.sol in availableTempListLong) {
                        setTemp(detail)
                    }

                    detail.earth_date = date.format(dateFormatter).toString()
                    if (!::detailRoot.isInitialized) {
                        detailRoot = detail
                        focusResult.value = detailRoot
                    }
                    adapterHistory.replaceItem(detail)
                    date = date.minusDays(1)
                }
            }
            is NetworkResponse.ServerError -> {
                // Handle server error
                detail = PlainClass(earth_date = "", img_list = listOf(MarsImage(0, Camera("", "O que será que aconteceu?"), "")))
                if (!::detailRoot.isInitialized) {
                    detailRoot = detail
                    focusResult.value = detailRoot
                }
            }
            is NetworkResponse.NetworkError -> {
                // Handle network error
                detail = PlainClass(earth_date = "", img_list = listOf(MarsImage(0, Camera("", "Estamos sem internet!"), "")))
                if (!::detailRoot.isInitialized) {
                    detailRoot = detail
                    focusResult.value = detailRoot
                }
                if(hasInternetConnection.value!!) { hasInternetConnection.value = false }
            }
            is NetworkResponse.UnknownError -> {
                // Handle other errors
                detail = PlainClass(earth_date = "", img_list = listOf(MarsImage(0, Camera("", "O que será que aconteceu?"), "")))
                if (!::detailRoot.isInitialized) {
                    detailRoot = detail
                    focusResult.value = detailRoot
                }
            }
        }
    }

    private suspend fun fetchTemperatures() {
        when (val temperatureJson = service.getMarsTemp("json", "1.0")) {
            is NetworkResponse.Success -> {
                // Handle successful response
                // Get JSON element of dates with available temperatures
                val availableTempJson = temperatureJson.body.get("sol_keys")
                val availableTempListString = Gson().fromJson(
                    availableTempJson,
                    object : TypeToken<List<String>>() {}.type
                ) as List<String>
                // Transform Sol date to long and use same Sol count from Curiosity Rover
                availableTempListLong = availableTempListString.map { it.toLong() + 2241 }

                // Create list of TempSol class
                for (s in availableTempListString) {
                    val tempSolCurr = Gson().fromJson(temperatureJson.body.get(s), object : TypeToken<TempSol>() {}.type) as TempSol
                    tempSolCurr.setTemperature(s.toLong() + 2241)
                    listTemp.add(tempSolCurr)
                }
            }
            is NetworkResponse.ServerError -> {
                // Handle server error
            }
            is NetworkResponse.NetworkError -> {
                // Handle network error
                if(hasInternetConnection.value!!) { hasInternetConnection.value = false }
            }
            is NetworkResponse.UnknownError -> {
                // Handle other errors
            }
        }
    }

    private fun setTemp(marsPost: PlainClass) {
        val tempSolCurr = listTemp.filter {it.solMars == marsPost.sol}[0]
        marsPost.maxTemp = "Max: " + tempSolCurr.maxTempMars + "°C"
        marsPost.minTemp = "Min: " + tempSolCurr.minTempMars + "°C"
    }

    fun selectDetail(detail: PlainClass) {
        focusResult.value = detail
    }

    fun selectRoot() {
        focusResult.value = detailRoot
    }

    fun favMarsDB(mars: MarsRoom, favBtn: ToggleButton) {
        viewModelScope.launch {
            if(repository.getMarsAtDateTask(mars.earth_date) == null) {
                repository.addMarsTask(mars)
                Log.i("===PLAINVIEWMODEL==", repository.getAllMarsFavsTask().toString())
                favBtn.isChecked = true
            } else {
                repository.deleteMarsTask(mars)
                favBtn.isChecked = false
            }
        }
    }

    fun favMarsState(mars: MarsRoom, favBtn: ToggleButton) {
        viewModelScope.launch {
            favBtn.isChecked = repository.getMarsAtDateTask(mars.earth_date) != null
        }
    }

    fun getAllMarsDB() {
        viewModelScope.launch {
            listFavsMars.value = repository.getAllMarsFavsTask()
        }
    }

}
