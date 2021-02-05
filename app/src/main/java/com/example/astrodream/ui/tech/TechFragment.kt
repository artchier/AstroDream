package com.example.astrodream.ui.tech

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.astrodream.R
import com.example.astrodream.domain.Patent
import kotlinx.android.synthetic.main.fragment_tech.view.*

class TechFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tech, container, false)

        view.btnListarPatentes.setOnClickListener {
            val type = "Patente"
            val bundle = bundleOf("type" to type)
            navigationFragments(R.id.action_techFragment_to_patentsFragment, bundle)
        }

        view.btnListarSoftwares.setOnClickListener {
            val type = "Software"
            val bundle = bundleOf("type" to type)
            navigationFragments(R.id.action_techFragment_to_softwaresFragment, bundle)
        }

        view.btnListarSpinoffs.setOnClickListener {
            val type = "Spinoff"
            val bundle = bundleOf("type" to type)
            navigationFragments(R.id.action_techFragment_to_spinoffsFragment, bundle)
        }

        return view
    }

    private fun navigationFragments(id: Int, bundle: Bundle) {
        findNavController().navigate(id, bundle)
    }
}