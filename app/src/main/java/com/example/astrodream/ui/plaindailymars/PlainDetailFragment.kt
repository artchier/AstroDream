package com.example.astrodream.ui.plaindailymars

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.astrodream.domain.PlainClass
import com.example.astrodream.domain.util.AstroDreamUtil
import com.example.astrodream.domain.util.transformDailyDBClassToPlain
import com.example.astrodream.domain.util.transformMarsDBClassToPlain
import com.example.astrodream.entitiesDatabase.DailyRoom
import com.example.astrodream.entitiesDatabase.MarsRoom
import com.example.astrodream.ui.favorites.FavViewModel
import com.example.astrodream.utils.TranslationEnglishToPortuguese

abstract class PlainDetailFragment(private val layoutId: Int) : Fragment() {

    lateinit var plainDetail: PlainClass

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View =  inflater.inflate(layoutId, container, false)

        val contextActivity = this.requireActivity()

        if (contextActivity is PlainActivity) {
            val viewModel: PlainViewModel by activityViewModels()

            viewModel.focusResult.observe(viewLifecycleOwner) {
                plainDetail = viewModel.focusResult.value!!
                popView(view)
            }
        } else {
            val viewModel: FavViewModel by activityViewModels()
            if(viewModel.favType.value == "daily") {
                val dailyRoom = viewModel.detail.value as DailyRoom
                plainDetail =
                    AstroDreamUtil.transformDailyDBClassToPlain(dailyRoom).apply { isFav = true }
            }
            if(viewModel.favType.value == "mars") {
                val marsRoom = viewModel.detail.value as PlainClass
                plainDetail = marsRoom
            }
            popView(view)
        }

        return view
    }

    abstract fun popView(view: View)

}