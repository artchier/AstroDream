package com.example.astrodream.ui.favorites

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.astrodream.R
import com.example.astrodream.domain.Asteroid
import com.example.astrodream.ui.tech.TechActivity
import kotlinx.android.synthetic.main.fragment_details_tech.view.*
import kotlinx.android.synthetic.main.fragment_fav_tech.view.*
import kotlinx.android.synthetic.main.fragment_fav_tech.view.ivTech

class FavTechFragment : Fragment() {

    private lateinit var contextTechActivity : FavoritesActivity
    val viewModel: FavViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_details_tech, container, false)

        val tech = viewModel.detail.value as List<*>

        if (tech[10] != "") {
            Glide.with(contextTechActivity).asBitmap()
                .load(tech[10])
                .into(view.ivTech)
        } else {
            view.ivTech.setImageResource(R.drawable.ic_tecnologia)
        }

        view.tvCodReferenceTech.text = tech[1] as String
        view.tvTitleTech.text = tech[2] as String
        view.tvDescTech.text = tech[3] as String

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FavoritesActivity) contextTechActivity = context
    }
}