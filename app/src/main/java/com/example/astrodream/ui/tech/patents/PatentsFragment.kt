package com.example.astrodream.ui.tech.patents

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
import kotlinx.android.synthetic.main.fragment_patents.view.*

class PatentsFragment : Fragment(), PatentsAdapter.OnClickPatentListener {
    private lateinit var contextTechActivity : TechActivity
    private lateinit var adapterPatents : PatentsAdapter

    private val viewModel by viewModels<PatentsViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return PatentsViewModel(service) as T
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_patents, container, false)

        adapterPatents = PatentsAdapter(this, contextTechActivity)

        view.rvPatents.adapter = adapterPatents
        view.rvPatents.layoutManager = LinearLayoutManager(contextTechActivity)
        view.rvPatents.setHasFixedSize(true)

        viewModel.getPatents()
        viewModel.patents.observe(contextTechActivity) {
            adapterPatents.addPatent(it.results)
        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is TechActivity) contextTechActivity = context
    }

    override fun onClickPatent(position: Int) {
        val patents = adapterPatents.getPatents()
        val patent = patents[position]

        navigationFragments(R.id.action_patentsFragment_to_detailsTechFragment, patent)
    }

    private fun navigationFragments(id: Int, patent: List<String>) {
        val bundle = bundleOf("patent" to patent)
        findNavController().navigate(id, bundle)
    }
}