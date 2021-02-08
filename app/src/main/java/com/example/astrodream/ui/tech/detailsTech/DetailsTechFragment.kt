package com.example.astrodream.ui.tech.detailsTech

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.astrodream.R
import com.example.astrodream.database.AppDatabase
import com.example.astrodream.domain.TranslatorEngToPort
import com.example.astrodream.domain.util.AstroDreamUtil
import com.example.astrodream.domain.util.saveImage
import com.example.astrodream.domain.util.useGlide
import com.example.astrodream.entitiesDatabase.Tech
import com.example.astrodream.services.ServiceDatabaseTech
import com.example.astrodream.services.ServiceDatabaseImplementationTech
import com.example.astrodream.services.shareText
import com.example.astrodream.ui.FullScreenImgActivity
import kotlinx.android.synthetic.main.fragment_details_tech.*
import kotlinx.android.synthetic.main.fragment_details_tech.view.*

class DetailsTechFragment : Fragment() {

    private lateinit var db: AppDatabase
    private lateinit var serviceDatabaseTech: ServiceDatabaseTech

    private lateinit var techPiece: ArrayList<String>
    private lateinit var detailView: View

    private val viewModel by viewModels<DetailsTechViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return DetailsTechViewModel(serviceDatabaseTech) as T
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        detailView = inflater.inflate(R.layout.fragment_details_tech, container, false)

        val patent = arguments?.getStringArrayList("patent")
        val software = arguments?.getStringArrayList("software")
        val spinoff = arguments?.getStringArrayList("spinoff")

        techPiece = patent ?: software ?: spinoff ?: return detailView

        if (techPiece[10] != "") {
            Glide.with(requireActivity()).asBitmap()
                .load(techPiece[10])
                .into(detailView.ivTech)

            detailView.ivTech.setOnClickListener {
                val intent = Intent(detailView.context, FullScreenImgActivity::class.java)
                intent.putExtra("img", techPiece[10])
                ContextCompat.startActivity(requireContext(), intent, null)
            }
        } else {
            detailView.ivTech.setImageResource(R.drawable.ic_tecnologia)
        }

        detailView.tvCodReferenceTech.text = techPiece[1]
        TranslatorEngToPort.translateEnglishToPortuguese(techPiece[2], detailView.tvTitleTech)
        TranslatorEngToPort.translateEnglishToPortuguese(techPiece[3], detailView.tvDescTech)

        return detailView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = AppDatabase.invoke(requireActivity())
        serviceDatabaseTech = ServiceDatabaseImplementationTech(db.techDAO())

        viewModel.getTechByCodeDB(techPiece[1])
        viewModel.isFav.observe(requireActivity()) {
            if (it) {
                btnFavorTech.setImageResource(R.drawable.ic_star_filled)
            } else {
                btnFavorTech.setImageResource(R.drawable.ic_star_border)
            }
        }

        btnFavorTech.setOnClickListener {
            val typeTech = arguments?.getString("type")

            if (techPiece[10] != "") {
                AstroDreamUtil.useGlide(contextTechActivity, techPiece[10]) { resource ->
                    val pathImgTech = AstroDreamUtil
                        .saveImage(resource.toBitmap(), contextTechActivity, "img_${techPiece[1]}")

                    viewModel.favTechDB(Tech(techPiece[1], techPiece[2], techPiece[3], pathImgTech, typeTech!!))
                }
            } else {
                viewModel.favTechDB(Tech(techPiece[1], techPiece[2], techPiece[3], "", typeTech!!))
            }
        }

        btnShareTech.setOnClickListener {
            shareText(detailView.tvTitleTech.text.toString(), detailView.tvDescTech.text.toString(), requireContext())
        }
    }
}