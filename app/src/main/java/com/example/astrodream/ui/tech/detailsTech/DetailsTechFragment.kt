package com.example.astrodream.ui.tech.detailsTech

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.astrodream.R
import com.example.astrodream.database.AppDatabase
import com.example.astrodream.entitiesDatabase.Tech
import com.example.astrodream.services.ServiceDatabaseTech
import com.example.astrodream.services.ServiceDatabaseImplementationTech
import com.example.astrodream.ui.tech.TechActivity
import kotlinx.android.synthetic.main.fragment_details_tech.*
import kotlinx.android.synthetic.main.fragment_details_tech.view.*


class DetailsTechFragment : Fragment() {
    private lateinit var contextTechActivity: TechActivity

    private lateinit var db: AppDatabase
    private lateinit var serviceDatabaseTech: ServiceDatabaseTech

    private lateinit var techPiece: ArrayList<String>

    private val viewModel by viewModels<DetailsTechViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return DetailsTechViewModel(serviceDatabaseTech, contextTechActivity) as T
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_details_tech, container, false)

        val patent = arguments?.getStringArrayList("patent")
        val software = arguments?.getStringArrayList("software")
        val spinoff = arguments?.getStringArrayList("spinoff")

        techPiece = patent ?: software ?: spinoff ?: return view

        if (techPiece[10] != "") {
            Glide.with(contextTechActivity).asBitmap()
                .load(techPiece[10])
                .into(view.ivTech)
        } else {
            view.ivTech.setImageResource(R.drawable.ic_tecnologia)
        }

        view.tvCodReferenceTech.text = techPiece[1]
        view.tvTitleTech.text = techPiece[2]
        view.tvDescTech.text = techPiece[3]

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = AppDatabase.invoke(contextTechActivity)
        serviceDatabaseTech = ServiceDatabaseImplementationTech(db.techDAO())

        viewModel.getTechByCodeDB(techPiece[1])
        viewModel.tech.observe(contextTechActivity) {
            if (it != null) {
                btnFavorTech.setImageResource(R.drawable.ic_star_filled)
            } else {
                btnFavorTech.setImageResource(R.drawable.ic_star_border)
            }
        }

        btnFavorTech.setOnClickListener {
            val typeTech = arguments?.getString("type")

            viewModel.favTechDB(Tech(techPiece[1], techPiece[2], techPiece[3], typeTech!!))
            viewModel.isFav.observe(contextTechActivity) {
                if (it) {
                    btnFavorTech.setImageResource(R.drawable.ic_star_filled)
                } else {
                    btnFavorTech.setImageResource(R.drawable.ic_star_border)
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is TechActivity) contextTechActivity = context
    }
}