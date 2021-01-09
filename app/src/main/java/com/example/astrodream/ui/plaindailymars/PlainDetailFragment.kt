package com.example.astrodream.ui.plaindailymars

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import com.example.astrodream.domain.PlainClass
import com.example.astrodream.ui.favorites.FavViewModel

abstract class PlainDetailFragment(private val layoutId: Int) : Fragment() {

    lateinit var plainDetail: PlainClass

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View =  inflater.inflate(layoutId, container, false)

        val contextActivity = this.requireActivity()

        if ( contextActivity is PlainActivity ) {
            val viewModel: PlainViewModel by activityViewModels()

            viewModel.focusResult.observe(viewLifecycleOwner) {
                plainDetail = viewModel.focusResult.value!!
                popView(view)
                Log.i("===PLAINDETALFRAG==", "$plainDetail")

            }
        } else {
            val viewModel: FavViewModel by activityViewModels()
            plainDetail = viewModel.detail.value!!
            popView(view)
            Log.i("===PLAINDETALFRAG==", "$plainDetail")
        }

        return view
    }

    abstract fun popView(view: View)
}