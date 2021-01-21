package com.example.astrodream.ui.favorites

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

    fun selectDetail(detailSelected: Any) { // TODO: criar sealed class??
        detail.value = detailSelected
    }

    fun setFavType(type: String) {
        favType.value = type
    }

    // TODO: dados de exemplo apenas, é necessário buscar dados do db
    // TODO: remover globo?
    fun dummyFavData(type: String) {
        when (type) {
            "daily" -> {
                viewModelScope.launch {
                    favList.value = dailyRepository.getAllDailyFavsTask()
                }
//                return listOf(
//                    PlainClass(
//                        title = "Dark Molecular Cloud Barnard 68",
//                        date = "22/11/2020",
//                        url = "https://apod.nasa.gov/apod/image/2011/barnard68v2_vlt_960.jpg",
//                        explanation = "xxxxxx"
//                    ),
//                    PlainClass(
//                        title = "Mars and Meteor over Jade Dragon Snow Mountain",
//                        date = "21/11/2020",
//                        url = "https://apod.nasa.gov/apod/image/2011/LeonidmeteorandMarsoverYulongsnowmountain1050.jpg",
//                        explanation = "yyyyyy"
//                    ),
//                    PlainClass(
//                        title = "Global Map: Mars at Opposition",
//                        date = "20/11/2020",
//                        url = "https://apod.nasa.gov/apod/image/2011/marsglobalmap_1100.jpg",
//                        explanation = "zzzzzz"
//                    ),
//                    PlainClass(
//                        title = "Crew-1 Mission Launch Streak",
//                        date = "19/11/2020",
//                        url = "https://apod.nasa.gov/apod/image/2011/spacex-crew-1-JenScottPhotography-11_1050.jpg",
//                        explanation = "aaaaa"
//                    ),
//                    PlainClass(
//                        title = "A Double Star Cluster in Perseus",
//                        date = "18/11/2020",
//                        url = "https://apod.nasa.gov/apod/image/2011/DoubleCluster_Polanski_960.jpg",
//                        explanation = "bbbbb"
//                    )
//                )
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
            }
            "globe" -> {
                favList.value = listOf(
                    Favorite(
                        type,
                        1,
                        "18 de Novembro de 2020",
                        "",
                        "https://api.nasa.gov/EPIC/archive/natural/2020/11/18/png/epic_1b_20201118001752.png?api_key=k070HGqyd0nQeVXvDaMsWeW4Q1aWernx6N4UDsDj"
                    ),
                    Favorite(
                        type,
                        2,
                        "17 de Novembro de 2020",
                        "",
                        "https://api.nasa.gov/EPIC/archive/natural/2020/11/17/png/epic_1b_20201117003633.png?api_key=k070HGqyd0nQeVXvDaMsWeW4Q1aWernx6N4UDsDj"
                    ),
                    Favorite(
                        type,
                        3,
                        "16 de Novembro de 2020",
                        "",
                        "https://api.nasa.gov/EPIC/archive/natural/2020/11/16/png/epic_1b_20201116005516.png?api_key=k070HGqyd0nQeVXvDaMsWeW4Q1aWernx6N4UDsDj"
                    ),
                    Favorite(
                        type,
                        4,
                        "15 de Novembro de 2020",
                        "",
                        "https://api.nasa.gov/EPIC/archive/natural/2020/11/15/png/epic_1b_20201115010437.png?api_key=k070HGqyd0nQeVXvDaMsWeW4Q1aWernx6N4UDsDj"
                    ),
                    Favorite(
                        type,
                        5,
                        "07 de Novembro de 2020",
                        "",
                        "https://api.nasa.gov/EPIC/archive/natural/2020/11/07/png/epic_1b_20201107023357.png?api_key=k070HGqyd0nQeVXvDaMsWeW4Q1aWernx6N4UDsDj"
                    )
                )
            }
            "tech" -> {
                viewModelScope.launch {
                    favList.value = techRepository.getAllTechnologiesTask()
                }
            }
            "mars" -> {
                favList.value = listOf(
                    PlainClass(
                        earth_date = "20/11/2020",
                        sol = 706,
                        img_list = listOf(
                            MarsImage(1, Camera("", ""), "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02947/opgs/edr/fcam/FLB_659123269EDR_F0832382FHAZ00302M_.JPG"),
                            MarsImage(1, Camera("", ""), "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02947/opgs/edr/fcam/FLB_659123269EDR_F0832382FHAZ00302M_.JPG"),
                            MarsImage(1, Camera("", ""), "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02947/opgs/edr/fcam/FLB_659123269EDR_F0832382FHAZ00302M_.JPG")
                        ),
                    ),
                    PlainClass(
                        earth_date = "19/11/2020",
                        sol = 705,
                        img_list = listOf(
                            MarsImage(1, Camera("", ""), "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02946/opgs/edr/fcam/FLB_659019582EDR_F0831974FHAZ00337M_.JPG"),
                            MarsImage(1, Camera("", ""), "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02946/opgs/edr/fcam/FLB_659019582EDR_F0831974FHAZ00337M_.JPG"),
                            MarsImage(1, Camera("", ""), "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02946/opgs/edr/fcam/FLB_659019582EDR_F0831974FHAZ00337M_.JPG")
                        ),
                    ),
                    PlainClass(
                        earth_date = "18/11/2020",
                        sol = 704,
                        img_list = listOf(
                            MarsImage(1, Camera("", ""), "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02945/opgs/edr/fcam/FLB_658949218EDR_F0831974FHAZ00337M_.JPG"),
                            MarsImage(1, Camera("", ""), "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02945/opgs/edr/fcam/FLB_658949218EDR_F0831974FHAZ00337M_.JPG"),
                            MarsImage(1, Camera("", ""), "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02945/opgs/edr/fcam/FLB_658949218EDR_F0831974FHAZ00337M_.JPG")
                        ),
                    ),
                    PlainClass(
                        earth_date = "17/11/2020",
                        sol = 703,
                        img_list = listOf(
                            MarsImage(1, Camera("", ""), "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02944/opgs/edr/fcam/FLB_658838602EDR_F0831974FHAZ00341M_.JPG"),
                            MarsImage(1, Camera("", ""), "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02944/opgs/edr/fcam/FLB_658838602EDR_F0831974FHAZ00341M_.JPG"),
                            MarsImage(1, Camera("", ""), "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02944/opgs/edr/fcam/FLB_658838602EDR_F0831974FHAZ00341M_.JPG")
                        ),
                    ),
                    PlainClass(
                        earth_date = "16/11/2020",
                        sol = 702,
                        img_list = listOf(
                            MarsImage(1, Camera("", ""), "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02943/opgs/edr/fcam/FLB_658767605EDR_F0831974FHAZ00302M_.JPG"),
                            MarsImage(1, Camera("", ""), "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02943/opgs/edr/fcam/FLB_658767605EDR_F0831974FHAZ00302M_.JPG"),
                            MarsImage(1, Camera("", ""), "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02943/opgs/edr/fcam/FLB_658767605EDR_F0831974FHAZ00302M_.JPG")
                        ),
                    )
                )
            }
        }
    }

}