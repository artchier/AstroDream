package com.example.astrodream.ui.tech

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.astrodream.R
import kotlinx.android.synthetic.main.fragment_tech.view.*

class TechFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tech, container, false)

        view.btnListarPatentes.setOnClickListener {
            navigationFragments(R.id.action_techFragment_to_patentsFragment)
        }

        view.btnListarSoftwares.setOnClickListener {
            navigationFragments(R.id.action_techFragment_to_softwaresFragment)
        }

        view.btnListarSpinoffs.setOnClickListener {
            navigationFragments(R.id.action_techFragment_to_spinoffsFragment)
        }

        return view
    }

    private fun navigationFragments(id: Int) {
        findNavController().navigate(id)
    }
}