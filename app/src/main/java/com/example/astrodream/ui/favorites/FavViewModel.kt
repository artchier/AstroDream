package com.example.astrodream.ui.favorites

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.astrodream.domain.*
import com.example.astrodream.services.*
import kotlinx.coroutines.launch

class FavViewModel(
    val dailyRepository: ServiceDBDaily,
    val techRepository: ServiceDatabaseTech,
    val marsRepository: ServiceDBMars
) : ViewModel() {

    var detail = MutableLiveData<Any>() // TODO: criar sealed class??
    var favType = MutableLiveData("daily")
    var favList = MutableLiveData<List<Any>>()
    var hasOngoingQuery = MutableLiveData(false)

    fun selectDetail(detailSelected: Any) { // TODO: criar sealed class??
        detail.value = detailSelected
    }

    fun setFavType(type: String) {
        favType.value = type
    }

    // TODO: dados de exemplo apenas, é necessário buscar dados do db
    // TODO: remover globo?
    fun dummyFavData(type: String) {
        hasOngoingQuery.value = true
        when (type) {
            "daily" -> {
                viewModelScope.launch {
                    val dailyRoomList = dailyRepository.getAllDailyFavsTask()
                    hasOngoingQuery.value = false
                    favList.value = dailyRoomList
                }
                Log.e("===FAVVIEMODEL===", "Caimos do DAIlY")
            }
            "asteroid" -> {
                favList.value = listOf(
                    Asteroid(
                        "1",
                        "Ananda",
                        true,
                        "100",
                        null,
                        "22/11/2020",
                        null,
                        "150",
                        null,
                        "https://media4.s-nbcnews.com/i/newscms/2017_16/1968396/170418-asteroid-mn-0730_b2c1f54812269d7ea29e5890b0c2b173.jpg"
                    ),
                    Asteroid(
                        "1",
                        "Arthur",
                        true,
                        "100",
                        null,
                        "21/11/2020",
                        null,
                        "150",
                        null,
                        "https://media4.s-nbcnews.com/i/newscms/2017_16/1968396/170418-asteroid-mn-0730_b2c1f54812269d7ea29e5890b0c2b173.jpg"
                    ),
                    Asteroid(
                        "1",
                        "Marina",
                        true,
                        "100",
                        null,
                        "20/11/2020",
                        null,
                        "150",
                        null,
                        "https://media4.s-nbcnews.com/i/newscms/2017_16/1968396/170418-asteroid-mn-0730_b2c1f54812269d7ea29e5890b0c2b173.jpg"
                    ),
                    Asteroid(
                        "1",
                        "Rafael",
                        true,
                        "100",
                        null,
                        "19/11/2020",
                        null,
                        "150",
                        null,
                        "https://media4.s-nbcnews.com/i/newscms/2017_16/1968396/170418-asteroid-mn-0730_b2c1f54812269d7ea29e5890b0c2b173.jpg"
                    ),
                    Asteroid(
                        "1",
                        "Raul",
                        true,
                        "100",
                        null,
                        "18/11/2020",
                        null,
                        "150",
                        null,
                        "https://media4.s-nbcnews.com/i/newscms/2017_16/1968396/170418-asteroid-mn-0730_b2c1f54812269d7ea29e5890b0c2b173.jpg"
                    )
                )
                hasOngoingQuery.value = false
            }
            "tech" -> {
                viewModelScope.launch {
                    hasOngoingQuery.value = false
                    favList.value = techRepository.getAllTechnologiesTask()
                }
            }
            "mars" -> {
                Log.e("===FAVVIEMODEL===", "Caimos do mars")
                viewModelScope.launch {
                    val marsRoomList = marsRepository.getAllMarsFavsTask()
                    val marsList = mutableListOf<PlainClass>()
                    marsRoomList.forEach { marsRoom ->
                        val imgRoomList = marsRepository.getMarsPicsAtDateTask(marsRoom.earth_date).marsPics
                        val imgList = mutableListOf<MarsImage>()
                        imgRoomList.forEach { marsPicRoom ->
                            imgList.add(MarsImage(marsRoom.sol, Camera("", marsPicRoom.cameraFullName), marsPicRoom.url))
                            Log.e("===FAVVIEMODEL===", imgList.toString())
                        }
                        marsList.add(PlainClass(earth_date = marsRoom.earth_date, sol = marsRoom.sol, img_list = imgList as List<MarsImage>, maxTemp = marsRoom.maxTemp, minTemp = marsRoom.minTemp, isFav = true))
                    }
                    hasOngoingQuery.value = false
                    favList.value = marsList
                    Log.e("===FAVVIEMODEL===", marsList.toString())
                }
//                favList.value = listOf(
//                    PlainClass(
//                        earth_date = "20/11/2020",
//                        sol = 706,
//                        img_list = listOf(
//                            MarsImage(1, Camera("", ""), "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02947/opgs/edr/fcam/FLB_659123269EDR_F0832382FHAZ00302M_.JPG"),
//                            MarsImage(1, Camera("", ""), "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02947/opgs/edr/fcam/FLB_659123269EDR_F0832382FHAZ00302M_.JPG"),
//                            MarsImage(1, Camera("", ""), "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02947/opgs/edr/fcam/FLB_659123269EDR_F0832382FHAZ00302M_.JPG")
//                        ),
//                    ),
//                    PlainClass(
//                        earth_date = "19/11/2020",
//                        sol = 705,
//                        img_list = listOf(
//                            MarsImage(1, Camera("", ""), "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02946/opgs/edr/fcam/FLB_659019582EDR_F0831974FHAZ00337M_.JPG"),
//                            MarsImage(1, Camera("", ""), "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02946/opgs/edr/fcam/FLB_659019582EDR_F0831974FHAZ00337M_.JPG"),
//                            MarsImage(1, Camera("", ""), "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02946/opgs/edr/fcam/FLB_659019582EDR_F0831974FHAZ00337M_.JPG")
//                        ),
//                    ),
//                    PlainClass(
//                        earth_date = "18/11/2020",
//                        sol = 704,
//                        img_list = listOf(
//                            MarsImage(1, Camera("", ""), "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02945/opgs/edr/fcam/FLB_658949218EDR_F0831974FHAZ00337M_.JPG"),
//                            MarsImage(1, Camera("", ""), "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02945/opgs/edr/fcam/FLB_658949218EDR_F0831974FHAZ00337M_.JPG"),
//                            MarsImage(1, Camera("", ""), "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02945/opgs/edr/fcam/FLB_658949218EDR_F0831974FHAZ00337M_.JPG")
//                        ),
//                    ),
//                    PlainClass(
//                        earth_date = "17/11/2020",
//                        sol = 703,
//                        img_list = listOf(
//                            MarsImage(1, Camera("", ""), "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02944/opgs/edr/fcam/FLB_658838602EDR_F0831974FHAZ00341M_.JPG"),
//                            MarsImage(1, Camera("", ""), "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02944/opgs/edr/fcam/FLB_658838602EDR_F0831974FHAZ00341M_.JPG"),
//                            MarsImage(1, Camera("", ""), "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02944/opgs/edr/fcam/FLB_658838602EDR_F0831974FHAZ00341M_.JPG")
//                        ),
//                    ),
//                    PlainClass(
//                        earth_date = "16/11/2020",
//                        sol = 702,
//                        img_list = listOf(
//                            MarsImage(1, Camera("", ""), "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02943/opgs/edr/fcam/FLB_658767605EDR_F0831974FHAZ00302M_.JPG"),
//                            MarsImage(1, Camera("", ""), "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02943/opgs/edr/fcam/FLB_658767605EDR_F0831974FHAZ00302M_.JPG"),
//                            MarsImage(1, Camera("", ""), "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02943/opgs/edr/fcam/FLB_658767605EDR_F0831974FHAZ00302M_.JPG")
//                        ),
//                    )
//                )
            }
        }
    }

}