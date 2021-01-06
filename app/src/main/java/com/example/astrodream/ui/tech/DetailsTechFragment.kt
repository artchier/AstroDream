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
    private lateinit var contextTechActivity : TechActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_details_tech, container, false)

        val patent = arguments?.getStringArrayList("patent")
        val software = arguments?.getStringArrayList("software")
        val spinoff = arguments?.getStringArrayList("spinoff")

        // TODO melhorar isso
        if (patent != null) {

            if (patent[10] != "") {
                Glide.with(contextTechActivity).asBitmap()
                    .load(patent[10])
                    .into(view.ivTech)
            } else {
                view.ivTech.setImageResource(R.drawable.ic_tecnologia)
            }

            view.tvCodReferenceTech.text = patent[1]
            view.tvTitleTech.text = patent[2]
            view.tvDescTech.text = patent[3]

        } else if (software != null) {

            view.ivTech.setImageResource(R.drawable.ic_tecnologia)
            view.tvCodReferenceTech.text = software[1]
            view.tvTitleTech.text = software[2]
            view.tvDescTech.text = software[3]

        } else if (spinoff != null) {

            view.ivTech.setImageResource(R.drawable.ic_tecnologia)
            view.tvCodReferenceTech.text = spinoff[1]
            view.tvTitleTech.text = spinoff[2]
            view.tvDescTech.text = spinoff[3]

        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is TechActivity) contextTechActivity = context
    }
}