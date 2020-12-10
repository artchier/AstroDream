package com.example.astrodream.ui.tech.spinoffs

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
import kotlinx.android.synthetic.main.fragment_spinoffs.view.*
import kotlinx.android.synthetic.main.item_spinoff.*

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
        if (llDescSpinoff.visibility == View.GONE) {
            TransitionManager.beginDelayedTransition(cvSpinoff, AutoTransition())
            llDescSpinoff.visibility = View.VISIBLE
        } else {
            TransitionManager.beginDelayedTransition(cvSpinoff, AutoTransition())
            llDescSpinoff.visibility = View.GONE
        }
    }
}