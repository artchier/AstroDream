package com.example.astrodream.ui.tech.spinoffs

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.astrodream.R
import com.example.astrodream.services.service
import com.example.astrodream.ui.tech.TechActivity
import kotlinx.android.synthetic.main.fragment_spinoffs.view.*

class SpinoffsFragment : Fragment(), SpinoffsAdapter.OnClickSpinoffListener {
    private lateinit var contextTechActivity : TechActivity
    private lateinit var adapterSpinoffs : SpinoffsAdapter

    private val viewModel by viewModels<SpinoffsViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return SpinoffsViewModel(service) as T
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_spinoffs, container, false)

        adapterSpinoffs = SpinoffsAdapter(this)

        view.rvSpinoffs.adapter = adapterSpinoffs
        view.rvSpinoffs.layoutManager = LinearLayoutManager(contextTechActivity)
        view.rvSpinoffs.setHasFixedSize(true)

        viewModel.getSpinoffs()
        viewModel.spinoffs.observe(contextTechActivity) {
            adapterSpinoffs.addSpinoff(it.results)
        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is TechActivity) contextTechActivity = context
    }

    override fun onClickSpinoff(position: Int) {
        val spinoffs = adapterSpinoffs.getSpinoffs()
        val spinoff = spinoffs[position]

        navigationFragments(R.id.action_spinoffsFragment_to_detailsTechFragment, spinoff)
    }

    private fun navigationFragments(id: Int, spinoff: List<String>) {
        val bundle = bundleOf("spinoff" to spinoff)
        findNavController().navigate(id, bundle)
    }
}