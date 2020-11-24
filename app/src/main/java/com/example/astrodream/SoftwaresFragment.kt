package com.example.astrodream

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_softwares.view.*

class SoftwaresFragment : Fragment() {
    private lateinit var contextTechActivity : TechActivity
    val listSoftwares = getAllSoftwares()
    val adapterSoftwares = SoftwaresAdapter(listSoftwares)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_softwares, container, false)

        view.rvSoftwares.adapter = adapterSoftwares
        view.rvSoftwares.layoutManager = LinearLayoutManager(contextTechActivity)
        view.rvSoftwares.setHasFixedSize(true)

        return view
    }

    fun getAllSoftwares(): ArrayList<Software> {
        val software = Software(1, R.drawable.ic_tecnologia, "LAR-19151-1", "Motor de anomalia hidrológica")

        return arrayListOf(software)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is TechActivity) contextTechActivity = context
    }
}