package com.example.astrodream.ui.tech.spinoffs

import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.astrodream.R
import com.example.astrodream.domain.Spinoff
import com.example.astrodream.services.service
import com.example.astrodream.ui.tech.TechPieceFragment
import com.example.astrodream.ui.tech.TechViewModel

class SpinoffsFragment :
    TechPieceFragment<Spinoff>(
        "spinoff",
        R.layout.fragment_spinoffs,
        R.id.rvSpinoffs,
        R.id.action_spinoffsFragment_to_detailsTechFragment,
    ) {

    override val viewModel by viewModels<TechViewModel<Spinoff>> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return SpinoffsViewModel(service) as T
            }
        }
    }
}