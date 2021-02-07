package com.example.astrodream.ui.favorites

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.astrodream.R
import com.example.astrodream.database.AppDatabase
import com.example.astrodream.domain.Asteroid
import com.example.astrodream.domain.TranslatorEngToPort
import com.example.astrodream.entitiesDatabase.Tech
import com.example.astrodream.services.ServiceDatabaseImplementationTech
import com.example.astrodream.services.ServiceDatabaseTech
import com.example.astrodream.ui.FullScreenImgActivity
import com.example.astrodream.ui.tech.TechActivity
import com.example.astrodream.ui.tech.detailsTech.DetailsTechViewModel
import com.example.astrodream.utils.TranslationEnglishToPortuguese
import kotlinx.android.synthetic.main.fragment_details_tech.*
import kotlinx.android.synthetic.main.fragment_details_tech.view.*
import kotlinx.android.synthetic.main.fragment_fav_tech.view.*
import kotlinx.android.synthetic.main.fragment_fav_tech.view.ivTech

class FavTechFragment : Fragment() {

    private lateinit var contextTechActivity : FavoritesActivity

    private lateinit var db: AppDatabase
    private lateinit var serviceDatabaseTech: ServiceDatabaseTech

    private val viewModelDetails by viewModels<DetailsTechViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return DetailsTechViewModel(serviceDatabaseTech, contextTechActivity) as T
            }
        }
    }

    private val viewModelFav: FavViewModel by activityViewModels()

    lateinit var tech: Tech

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_details_tech, container, false)

        tech = viewModelFav.detail.value as Tech

        if (tech.pathImgTech != "") {
            Glide.with(contextTechActivity).asBitmap()
                .load(tech.pathImgTech)
                .into(view.ivTech)

            view.ivTech.setOnClickListener {
                val intent = Intent(view.context, FullScreenImgActivity::class.java)
                intent.putExtra("img", tech.pathImgTech)
                ContextCompat.startActivity(requireContext(), intent, null)
            }
        } else {
            view.ivTech.setImageResource(R.drawable.ic_tecnologia)
        }

        view.tvCodReferenceTech.text = tech.codReferenceTech
        TranslatorEngToPort.translateEnglishToPortuguese(tech.titleTech, view.tvTitleTech)
        TranslatorEngToPort.translateEnglishToPortuguese(tech.descTech, view.tvDescTech)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnFavorTech.setImageResource(R.drawable.ic_star_filled)

        db = AppDatabase.invoke(contextTechActivity)
        serviceDatabaseTech = ServiceDatabaseImplementationTech(db.techDAO())

        btnFavorTech.setOnClickListener {
            viewModelDetails.deleteTechDB(tech.codReferenceTech)
            FavoritesActivity().finish()
            startActivity(Intent(requireActivity(), FavoritesActivity::class.java))
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FavoritesActivity) contextTechActivity = context
    }
}