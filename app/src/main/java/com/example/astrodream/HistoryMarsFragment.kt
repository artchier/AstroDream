package com.example.astrodream

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_history_mars.*
import kotlinx.android.synthetic.main.fragment_history_mars.view.*

class HistoryMarsFragment : Fragment(), MarsHistoryAdapter.OnClickMarsPostListener {

    var listMarsPosts = getMarsPosts()
    val adapterMarsHistory = MarsHistoryAdapter(listMarsPosts, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_history_mars, container, false)

        view.rvMarsHistory.adapter = adapterMarsHistory
        if (container != null) {
            view.rvMarsHistory.layoutManager = GridLayoutManager(container.context, 3)
        }
        view.rvMarsHistory.setHasFixedSize(true)

        return view
    }

    override fun onClickMarsPost(position: Int) {
        var marsPost = listMarsPosts.get(position)
        val bundleRest: Bundle = Bundle().apply {
            putStringArrayList("marsPicsList", marsPost.img_list)
            putString("postDate", marsPost.earth_date)
        }
        findNavController().navigate(
            R.id.action_historyMarsFragment_to_recentMarsFragment,
            bundleRest
        )
    }

    fun getMarsPosts() : ArrayList<MarsPost> {
        val mars1 = MarsPost(
            1,
            "20 de Novembro",
            arrayListOf(
                "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02947/opgs/edr/fcam/FLB_659123269EDR_F0832382FHAZ00302M_.JPG",
                "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02947/opgs/edr/rcam/RLB_659123404EDR_F0832382RHAZ00311M_.JPG",
                "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02947/opgs/edr/ncam/NLB_659124657EDR_F0832382NCAM00294M_.JPG",
                "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02947/opgs/edr/ncam/NLB_659124625EDR_F0832382NCAM00294M_.JPG",
                "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02947/opgs/edr/ncam/NLB_659124442EDR_F0832382NCAM00294M_.JPG")
        )
        val mars2 = MarsPost(
            2,
            "18 de Novembro",
            arrayListOf(
                "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02946/opgs/edr/fcam/FLB_659019582EDR_F0831974FHAZ00337M_.JPG",
                "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02946/opgs/edr/rcam/RLB_659019616EDR_F0831974RHAZ00337M_.JPG",
                "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02946/opgs/edr/ncam/NRB_659022395EDR_S0831974NCAM00594M_.JPG",
                "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02946/opgs/edr/ncam/NRB_659022181EDR_S0831974NCAM00594M_.JPG")
        )
        val mars3 = MarsPost(
            3,
            "17 de Novembro",
            arrayListOf(
                "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02945/opgs/edr/fcam/FLB_658949218EDR_F0831974FHAZ00337M_.JPG",
                "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02945/opgs/edr/rcam/RRB_658949266EDR_F0831974RHAZ00337M_.JPG",
                "https://mars.nasa.gov/msl-raw-images/msss/02945/mcam/2945MR0153690001301823E01_DXXX.jpg",
                "https://mars.nasa.gov/msl-raw-images/msss/02945/mhli/2945MH0001630001004214R00_DXXX.jpg",
                "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02945/opgs/edr/ncam/NRB_658916751EDR_M0831974NCAM00580M_.JPG")
        )
        val mars4 = MarsPost(
            4,
            "16 de Novembro",
            arrayListOf(
                "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02944/opgs/edr/fcam/FLB_658838602EDR_F0831974FHAZ00341M_.JPG",
                "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02944/opgs/edr/rcam/RLB_658838636EDR_F0831974RHAZ00341M_.JPG",
                "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02944/opgs/edr/ccam/CR0_658839819EDR_F0831974CCAM15120M_.JPG",
                "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02944/opgs/edr/ncam/NRB_658840499EDR_F0831974CCAM04942M_.JPG",
                "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02944/opgs/edr/ncam/NRB_658842731EDR_S0831974NCAM00594M_.JPG",
                "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02944/opgs/edr/ncam/NRB_658842679EDR_S0831974NCAM00594M_.JPG")
        )
        return arrayListOf(mars1, mars2, mars3, mars4, mars1, mars2, mars3, mars4, mars1, mars2, mars3, mars4, mars1, mars2, mars3, mars4, mars1, mars2, mars3, mars4, mars1, mars2, mars3, mars4)
    }

}