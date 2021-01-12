package com.example.astrodream.ui.tech.patents

import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.astrodream.R
import com.example.astrodream.domain.Patent
import com.example.astrodream.services.service
import com.example.astrodream.ui.tech.TechPieceFragment
import com.example.astrodream.ui.tech.TechViewModel

class PatentsFragment :
    TechPieceFragment<Patent>(
        "patent",
        R.layout.fragment_patents,
        R.id.rvPatents, R.id.action_patentsFragment_to_detailsTechFragment,
    ) {

    override val viewModel by viewModels<TechViewModel<Patent>> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return PatentsViewModel(service) as T
            }
        }
    }
}