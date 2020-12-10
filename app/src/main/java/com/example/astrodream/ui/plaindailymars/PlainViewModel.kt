package com.example.astrodream.ui.plaindailymars

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.astrodream.domain.PlainClass
import com.example.astrodream.services.Service
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate

class PlainViewModel(val service: Service, val type: String): ViewModel() {

    val listResults = MutableLiveData<List<PlainClass>>()
    val focusResult = MutableLiveData<PlainClass>()
    private var numFetches = 0
    private val num = 24
    private val apikey = "k070HGqyd0nQeVXvDaMsWeW4Q1aWernx6N4UDsDj"

    var date = LocalDate.now()
    lateinit var detail: PlainClass
    lateinit var detailRoot: PlainClass

    fun popList() {
        if (numFetches == 0) {
            var listFetched: MutableList<PlainClass> = mutableListOf()
            viewModelScope.launch {
                if (type == "Daily") {
                    detail = service.getDaily(
                        date.toString(),
                        apikey
                    )
                } else if (type === "Mars") {
                    //TODO:: requisicao marte
                    // nesse caso precisamos checar se data tem um retorno da API
                    detail = PlainClass(
                        earth_date = "20 de Novembro",
                        img_list = arrayListOf(
                        "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02947/opgs/edr/fcam/FLB_659123269EDR_F0832382FHAZ00302M_.JPG",
                        "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02947/opgs/edr/rcam/RLB_659123404EDR_F0832382RHAZ00311M_.JPG",
                        "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02947/opgs/edr/ncam/NLB_659124657EDR_F0832382NCAM00294M_.JPG",
                        "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02947/opgs/edr/ncam/NLB_659124625EDR_F0832382NCAM00294M_.JPG",
                        "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02947/opgs/edr/ncam/NLB_659124442EDR_F0832382NCAM00294M_.JPG"),
                        maxTemp = "Máxima: -11°C",
                        minTemp = "Mínima: -93°C")
                }
                listFetched.add(detail)
                date = date.minusDays(1)
                listResults.value = listFetched
                detailRoot = listResults.value!![0]
                numFetches++
                popList()
            }
        } else {
            var listFetched: MutableList<PlainClass> = mutableListOf()
            viewModelScope.launch {
                if (type == "Daily") {
                    for (i in 1..num) {
                        detail = service.getDaily(
                            date.toString(),
                            apikey
                        )
                        listFetched.add(detail)
                        date = date.minusDays(1)
                    }
                } else if (type === "Mars") {
                //TODO:: requisicao marte
                // nesse caso precisamos checar se data tem um retorno da API
                    listFetched = getMarsPosts()
                }
                listResults.value = listFetched
            }
            numFetches++
        }
    }

    fun selectDetail(detail: PlainClass) {
        focusResult.value = detail
    }

    fun selectRoot() {
        focusResult.value = detailRoot
    }

    private fun getMarsPosts() : ArrayList<PlainClass> {
        val mars2 = PlainClass(
            earth_date = "18 de Novembro",
            img_list = arrayListOf(
                "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02946/opgs/edr/fcam/FLB_659019582EDR_F0831974FHAZ00337M_.JPG",
                "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02946/opgs/edr/rcam/RLB_659019616EDR_F0831974RHAZ00337M_.JPG",
                "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02946/opgs/edr/ncam/NRB_659022395EDR_S0831974NCAM00594M_.JPG",
                "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02946/opgs/edr/ncam/NRB_659022181EDR_S0831974NCAM00594M_.JPG"
            ),
            maxTemp = "Máxima: -8°C",
            minTemp = "Mínima: -97°C"
        )
        val mars3 = PlainClass(
            earth_date = "17 de Novembro",
            img_list = arrayListOf(
                "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02945/opgs/edr/fcam/FLB_658949218EDR_F0831974FHAZ00337M_.JPG",
                "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02945/opgs/edr/rcam/RRB_658949266EDR_F0831974RHAZ00337M_.JPG",
                "https://mars.nasa.gov/msl-raw-images/msss/02945/mcam/2945MR0153690001301823E01_DXXX.jpg",
                "https://mars.nasa.gov/msl-raw-images/msss/02945/mhli/2945MH0001630001004214R00_DXXX.jpg",
                "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02945/opgs/edr/ncam/NRB_658916751EDR_M0831974NCAM00580M_.JPG"
            ),
            maxTemp = "Máxima: -9°C",
            minTemp = "Mínima: -96°C"
        )
        val mars4 = PlainClass(
            earth_date = "16 de Novembro",
            img_list = arrayListOf(
                "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02944/opgs/edr/fcam/FLB_658838602EDR_F0831974FHAZ00341M_.JPG",
                "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02944/opgs/edr/rcam/RLB_658838636EDR_F0831974RHAZ00341M_.JPG",
                "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02944/opgs/edr/ccam/CR0_658839819EDR_F0831974CCAM15120M_.JPG",
                "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02944/opgs/edr/ncam/NRB_658840499EDR_F0831974CCAM04942M_.JPG",
                "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02944/opgs/edr/ncam/NRB_658842731EDR_S0831974NCAM00594M_.JPG",
                "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02944/opgs/edr/ncam/NRB_658842679EDR_S0831974NCAM00594M_.JPG"
            ),
            maxTemp = "Temperaturas",
            minTemp = "indisponiveis"
        )
        return arrayListOf(
            mars2, mars3, mars4,
            mars2, mars3, mars4,
            mars2, mars3, mars4,
            mars2, mars3, mars4,
            mars2, mars3, mars4,
            mars2, mars3, mars4
        )
    }
}