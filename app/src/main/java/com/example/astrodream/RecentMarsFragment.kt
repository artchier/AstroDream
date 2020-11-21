package com.example.astrodream

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_recent_mars.view.*
import me.relex.circleindicator.CircleIndicator

class RecentMarsFragment : Fragment() {

    lateinit var marsPicsList: ArrayList<String>
    lateinit var adapterMars: MarsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_recent_mars, container, false)

        marsPicsList = getMarsPics()
        if (container != null) {
            adapterMars = MarsAdapter(container.getContext(), marsPicsList)
        }

        view.vpMarsRecent.adapter = adapterMars

        val indicator = view.ciMarsRecent as CircleIndicator
        indicator.setViewPager(view.vpMarsRecent)

        try {
            if (requireArguments() != null) {
                if (container != null) {
                    var listFromBundle: ArrayList<String> = requireArguments().getStringArrayList("marsPicsList") as ArrayList<String>
                    adapterMars = MarsAdapter(container.getContext(), listFromBundle)
                }
            }
        } catch(e: Exception) {
            Log.e("RecentMarsFragment", e.toString())
        }

        return view
    }


    // Imagens do dia 20/11/2020
    fun getMarsPics() = arrayListOf(
        "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02947/opgs/edr/fcam/FLB_659123269EDR_F0832382FHAZ00302M_.JPG",
        "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02947/opgs/edr/rcam/RLB_659123404EDR_F0832382RHAZ00311M_.JPG",
        "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02947/opgs/edr/ncam/NLB_659124657EDR_F0832382NCAM00294M_.JPG",
        "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02947/opgs/edr/ncam/NLB_659124625EDR_F0832382NCAM00294M_.JPG",
        "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02947/opgs/edr/ncam/NLB_659124442EDR_F0832382NCAM00294M_.JPG"
    )

}