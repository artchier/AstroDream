package com.example.astrodream

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_patents.*
import kotlinx.android.synthetic.main.fragment_patents.view.*

class PatentsFragment : Fragment() {
    private lateinit var contextTechActivity : TechActivity
    val listPatents = getAllPatents()
    val adapterPatents = PatentsAdapter(listPatents)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_patents, container, false)

        view.rvPatents.adapter = adapterPatents
        view.rvPatents.layoutManager = LinearLayoutManager(contextTechActivity)
        view.rvPatents.setHasFixedSize(true)

        return view
    }

    fun getAllPatents(): ArrayList<Patent> {
        val patent = Patent(1, R.drawable.ic_tecnologia, "LAR-TOPS-185", "Prevenção e mitigação de eventos de congelamento de aeronaves")

        return arrayListOf(patent)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is TechActivity) contextTechActivity = context
    }
}