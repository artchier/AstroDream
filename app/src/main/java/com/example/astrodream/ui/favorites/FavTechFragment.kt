package com.example.astrodream.ui.favorites

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.astrodream.R
import com.example.astrodream.database.AppDatabase
import com.example.astrodream.domain.TranslatorEngToPort
import com.example.astrodream.entitiesDatabase.Tech
import com.example.astrodream.services.ServiceDatabaseImplementationTech
import com.example.astrodream.services.shareText
import com.example.astrodream.ui.tech.detailsTech.DetailsTechViewModel
import kotlinx.android.synthetic.main.fragment_details_tech.*
import kotlinx.android.synthetic.main.fragment_details_tech.view.*

class FavTechFragment : Fragment() {

    private lateinit var favView: View

    private val viewModelFav: FavViewModel by activityViewModels()

    lateinit var tech: Tech

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        favView = inflater.inflate(R.layout.fragment_details_tech, container, false)

        tech = viewModelFav.detail.value as Tech

        favView.ivTech.setImageResource(R.drawable.ic_tecnologia)

        favView.tvCodReferenceTech.text = tech.codReferenceTech
        TranslatorEngToPort.translateEnglishToPortuguese(tech.titleTech, favView.tvTitleTech)
        TranslatorEngToPort.translateEnglishToPortuguese(tech.descTech, favView.tvDescTech)

        return favView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnFavorTech.setImageResource(R.drawable.ic_star_filled)

        btnFavorTech.setOnClickListener {
            val db = AppDatabase.invoke(requireActivity())
            val serviceDatabaseTech = ServiceDatabaseImplementationTech(db.techDAO())
            val viewModelDetails by viewModels<DetailsTechViewModel> {
                object : ViewModelProvider.Factory {
                    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                        return DetailsTechViewModel(serviceDatabaseTech) as T
                    }
                }
            }

            viewModelDetails.deleteTechDB(tech.codReferenceTech)
            requireActivity().onBackPressed()
        }

        btnShareTech.setOnClickListener {
            shareText(favView.tvTitleTech.text.toString(), favView.tvDescTech.text.toString(), requireContext())
        }
    }
}