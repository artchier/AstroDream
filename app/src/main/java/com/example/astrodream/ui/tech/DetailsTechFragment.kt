package com.example.astrodream.ui.tech

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.astrodream.R
import kotlinx.android.synthetic.main.fragment_details_tech.view.*

class DetailsTechFragment : Fragment() {
    private lateinit var contextTechActivity: TechActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_details_tech, container, false)

        val patent = arguments?.getStringArrayList("patent")
        val software = arguments?.getStringArrayList("software")
        val spinoff = arguments?.getStringArrayList("spinoff")

        val techPiece = patent ?: software ?: spinoff ?: return view

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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is TechActivity) contextTechActivity = context
    }
}