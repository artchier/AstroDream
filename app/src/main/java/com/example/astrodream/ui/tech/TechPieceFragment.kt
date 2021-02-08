package com.example.astrodream.ui.tech

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.astrodream.domain.TechPiece
import com.example.astrodream.domain.util.AstroDreamUtil
import com.example.astrodream.domain.util.showErrorInternetConnection
import com.example.astrodream.domain.util.showUnknownError
import com.example.astrodream.ui.RealtimeViewModel

abstract class TechPieceFragment<T : TechPiece>(
    private val techType: String,
    private val fragmentXmlId: Int,
    private val recyclerViewId: Int,
    private val navigationToDetailsFragmentId: Int
) : Fragment() {

    private lateinit var contextTechActivity: TechActivity
    private lateinit var adapterTech: TechPieceAdapter
    private val realtimeViewModel: RealtimeViewModel by viewModels()

    abstract val viewModel: TechViewModel<T>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(fragmentXmlId, container, false)

        adapterTech = TechPieceAdapter(
            this::onClickTechPiece,
            contextTechActivity,
            realtimeViewModel,
            requireActivity()
        )

        view.findViewById<RecyclerView>(recyclerViewId).apply {
            adapter = adapterTech
            layoutManager = LinearLayoutManager(contextTechActivity)
            setHasFixedSize(true)
        }

        viewModel.getTechPieces()
        viewModel.techPieces.observe(contextTechActivity) {
            adapterTech.addTechPiece(it.results)
        }

        viewModel.hasInternetConnection.observe(contextTechActivity) {
            if (!it) {
                AstroDreamUtil.showErrorInternetConnection(contextTechActivity)
            }
        }

        viewModel.unknownErrorAPI.observe(contextTechActivity) {
            if (it) {
                AstroDreamUtil.showUnknownError(contextTechActivity)
            }
        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is TechActivity) contextTechActivity = context
    }

    private fun onClickTechPiece(position: Int) {
        val techPieces = adapterTech.getTechPieces()
        val techPiece = techPieces[position]

        val bundle = bundleOf(techType to techPiece)
        bundle.putString("type", arguments?.getString("type"))
        findNavController().navigate(navigationToDetailsFragmentId, bundle)
    }
}