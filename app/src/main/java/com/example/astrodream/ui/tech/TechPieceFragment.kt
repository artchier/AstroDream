package com.example.astrodream.ui.tech

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.astrodream.domain.TechPiece

abstract class TechPieceFragment<T : TechPiece>(
    private val techType: String,
    private val fragmentXmlId: Int,
    private val recyclerViewId: Int,
    private val navigationToDetailsFragmentId: Int
    ) : Fragment() {

    private lateinit var contextTechActivity : TechActivity
    private lateinit var adapterTech : TechPieceAdapter

    abstract val viewModel: TechViewModel<T>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(fragmentXmlId, container, false)

        adapterTech = TechPieceAdapter(this::onClickTechPiece, contextTechActivity)

        view.findViewById<RecyclerView>(recyclerViewId).apply {
            adapter = adapterTech
            layoutManager = LinearLayoutManager(contextTechActivity)
            setHasFixedSize(true)
        }

        viewModel.getTechPieces()
        viewModel.techPieces.observe(contextTechActivity) {
            adapterTech.addTechPiece(it.results)
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