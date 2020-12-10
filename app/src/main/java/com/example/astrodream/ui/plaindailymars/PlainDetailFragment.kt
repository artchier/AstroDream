package com.example.astrodream.ui.plaindailymars

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.astrodream.domain.PlainClass

abstract class PlainDetailFragment(private val layoutId: Int) : Fragment() {

    val viewModel : PlainViewModel by activityViewModels()
    lateinit var plainDetail: PlainClass

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View =  inflater.inflate(layoutId, container, false)

        viewModel.listResults.observe(viewLifecycleOwner) {
            plainDetail = viewModel.listResults.value!![0]
            popView(view)
        }

        viewModel.focusResult.observe(viewLifecycleOwner) {
            plainDetail = viewModel.focusResult.value!!
            popView(view)
        }

        return view
    }

    abstract fun popView(view: View)
}