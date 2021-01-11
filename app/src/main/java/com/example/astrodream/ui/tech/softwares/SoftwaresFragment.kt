package com.example.astrodream.ui.tech.softwares

import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.astrodream.R
import com.example.astrodream.domain.Software
import com.example.astrodream.services.service
import com.example.astrodream.ui.tech.TechPieceFragment
import com.example.astrodream.ui.tech.TechViewModel

class SoftwaresFragment :
    TechPieceFragment<Software>(
        "software",
        R.layout.fragment_softwares,
        R.id.rvSoftwares,
        R.id.action_softwaresFragment_to_detailsTechFragment,
    ) {

    override val viewModel by viewModels<TechViewModel<Software>> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return SoftwaresViewModel(service) as T
            }
        }
    }
}