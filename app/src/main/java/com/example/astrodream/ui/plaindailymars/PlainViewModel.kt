package com.example.astrodream.ui.plaindailymars

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ToggleButton
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.astrodream.R
import com.example.astrodream.domain.Camera
import com.example.astrodream.domain.MarsImage
import com.example.astrodream.domain.PlainClass
import com.example.astrodream.domain.TempSol
import com.example.astrodream.domain.util.AstroDreamUtil
import com.example.astrodream.domain.util.saveImage
import com.example.astrodream.domain.util.transformPlainToDailyDBClass
import com.example.astrodream.domain.util.transformPlainToMarsDBClass
import com.example.astrodream.entitiesDatabase.DailyRoom
import com.example.astrodream.entitiesDatabase.MarsPicRoom
import com.example.astrodream.entitiesDatabase.MarsRoom
import com.example.astrodream.services.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.haroldadmin.cnradapter.NetworkResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

class PlainViewModel(val service: Service, private val type: PlainActivityType, val repository: ServiceDB): ViewModel() {

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
                    fetchDailyImage(date)
                    focusResult.value = detail
                }

                PlainActivityType.DailyImage -> {
                    for (i in 1..timesToFetch) {
                        fetchDailyImage(date)
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

    private suspend fun fetchDailyImage(date: LocalDate) {
        when (val response = service.getDaily(date.toString())) {
            is NetworkResponse.Success -> {
                // Handle successful response
                if ((response.body.url).contains("youtube")) {
                    fetchDailyImage(date.minusDays(1))
                } else {
                    detail = response.body
                    detail.date = date.format(dateFormatter).toString()

                    if((repository as ServiceDBDaily).getDailyAtDateTask(detail.date) != null) {
                        detail.isFav = true
                    }
                    if (!::detailRoot.isInitialized) {
                        detailRoot = detail
                        focusResult.value = detailRoot
                    }
                    adapterHistory.replaceItem(detail)
                    this.date = date.minusDays(1)
                }
            }
            is NetworkResponse.ServerError -> {
                // Handle server error (unavailable date falls into ServerError)
                fetchDailyImage(date.minusDays(1))
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

                    detail = PlainClass(earth_date = date.toString(), sol = marsImageList[0].sol, img_list = marsImageList, isFav = false)

                    if (detail.sol in availableTempListLong) {
                        setTemp(detail)
                    }

                    detail.earth_date = date.format(dateFormatter).toString()

                    if((repository as ServiceDBMars).getMarsAtDateTask(detail.earth_date) != null) {
                        detail.isFav = true
                    }

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

    fun favPlainDB(detail: PlainClass, favBtn: ToggleButton, context: Context) {
        val dateFmt = detail.date.replace("/", "_")
        if(type == PlainActivityType.DailyImage) {
            Glide.with(context)
                .load(detail.url)
                .into(object : CustomTarget<Drawable?>() {
                    override fun onResourceReady(
                        resource: Drawable,
                        transition: Transition<in Drawable?>?
                    ) {
                        viewModelScope.launch {
                            val dailyRoom = AstroDreamUtil.transformPlainToDailyDBClass(detail, "")
                            if ((repository as ServiceDBDaily).getDailyAtDateTask(detail.date) == null) {
                                val fileUri = AstroDreamUtil.saveImage(resource.toBitmap(), context, "Daily", "daily_${dateFmt}")
                                dailyRoom.url = fileUri
                                repository.addDailyTask(dailyRoom)
                                favBtn.isChecked = true
                                if(adapterHistory.itemCount > 1) {
                                    adapterHistory.replaceItemAt(detail.apply { this.isFav = true })
                                }
                            } else {
                                repository.deleteDailyTask(dailyRoom)
                                favBtn.isChecked = false
                                if(adapterHistory.itemCount > 1) {
                                    adapterHistory.replaceItemAt(detail.apply {
                                        this.isFav = false
                                    })
                                }
                            }
                            Log.i("===PLAINVIEWMODEL==", repository.getAllDailyFavsTask().toString())
                        }
                    }
                    override fun onLoadCleared(placeholder: Drawable?) {}
                })
        }
        if(type == PlainActivityType.Mars) {
            val marsRoom = AstroDreamUtil.transformPlainToMarsDBClass(detail)
            viewModelScope.launch {
                if ((repository as ServiceDBMars).getMarsAtDateTask(marsRoom.earth_date) == null) {
                    repository.addMarsTask(marsRoom)
                    favBtn.isChecked = true
                    if(adapterHistory.itemCount > 1) {
                        adapterHistory.replaceItemAt(detail.apply { this.isFav = true })
                    }
                    detail.img_list.forEachIndexed {index, marsImage ->
                        Glide.with(context)
                            .load(marsImage.img_src)
                            .into(object : CustomTarget<Drawable?>() {
                                override fun onResourceReady(
                                    resource: Drawable,
                                    transition: Transition<in Drawable?>?
                                ) {
                                    viewModelScope.launch {
                                        val fileUri = AstroDreamUtil.saveImage(
                                            resource.toBitmap(),
                                            context,
                                            "Mars",
                                            "mars_${dateFmt}_${index}"
                                        )
                                        repository.addMarsPicTask(
                                            MarsPicRoom(
                                                id = 0,
                                                url = fileUri,
                                                earth_date = detail.earth_date,
                                                cameraFullName = marsImage.camera.full_name
                                            )
                                        )
                                    }
                                }
                                override fun onLoadCleared(placeholder: Drawable?) {}
                            })
                    }
                } else {
                    repository.deleteMarsTask(marsRoom)
                    favBtn.isChecked = false
                    if(adapterHistory.itemCount > 1) {
                        adapterHistory.replaceItemAt(detail.apply { this.isFav = false })
                    }
                }
                Log.e("====PLAINVIEWMODEL===", repository.getAllMarsFavsTask().toString())

            }
        }
    }

    fun favMarsState(mars: MarsRoom, favBtn: ToggleButton) {
        viewModelScope.launch {
            favBtn.isChecked = (repository as ServiceDBMars).getMarsAtDateTask(mars.earth_date) != null
        }
    }

    fun favDailyState(daily: DailyRoom, favBtn: ToggleButton) {
        viewModelScope.launch {
            favBtn.isChecked = (repository as ServiceDBDaily).getDailyAtDateTask(daily.date) != null
        }
    }
    fun getAllMarsDB() {
        viewModelScope.launch {
            listFavsMars.value = (repository as ServiceDBMars).getAllMarsFavsTask()
        }
    }

}
