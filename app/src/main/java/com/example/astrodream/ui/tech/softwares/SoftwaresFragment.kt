package com.example.astrodream.ui.tech.softwares

import android.content.Context
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.astrodream.R
import com.example.astrodream.services.service
import com.example.astrodream.ui.tech.TechActivity
import kotlinx.android.synthetic.main.fragment_softwares.view.*
import kotlinx.android.synthetic.main.item_software.*

class SoftwaresFragment : Fragment(), SoftwaresAdapter.OnClickSoftwareListener {
    private lateinit var contextTechActivity : TechActivity
    private lateinit var adapterSoftwares : SoftwaresAdapter

    private val viewModel by viewModels<SoftwaresViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return SoftwaresViewModel(service) as T
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_softwares, container, false)

        adapterSoftwares = SoftwaresAdapter(this)

        view.rvSoftwares.adapter = adapterSoftwares
        view.rvSoftwares.layoutManager = LinearLayoutManager(contextTechActivity)
        view.rvSoftwares.setHasFixedSize(true)

        viewModel.getSoftwares()
        viewModel.softwares.observe(contextTechActivity) {
            adapterSoftwares.addSoftware(it.results)
        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is TechActivity) contextTechActivity = context
    }

    override fun onClickSoftware(position: Int) {
        if (llDescSoftware.visibility == View.GONE) {
            TransitionManager.beginDelayedTransition(cvSoftware, AutoTransition())
            llDescSoftware.visibility = View.VISIBLE
        } else {
            TransitionManager.beginDelayedTransition(cvSoftware, AutoTransition())
            llDescSoftware.visibility = View.GONE
        }
    }
}