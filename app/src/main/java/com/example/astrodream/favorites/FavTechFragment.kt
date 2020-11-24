package com.example.astrodream.favorites

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.astrodream.R
import kotlinx.android.synthetic.main.fragment_fav_tech.view.*

class FavTechFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_fav_tech, container, false)

        try {
            // Caso tenha dados no bundle, ou seja, o fragment foi carregado a partir do Favoritos,
            // atualiza a imagem e textos de acordo com a informação do favorito clicado lá no Favoritos
            if (container != null) {
                // Salva dados do bundle em variaveis
                val typeTech = requireArguments().getString("typeTech") as String
                val title = requireArguments().getString("title") as String
                val img = requireArguments().getString("img") as String
                val details = requireArguments().getString("details") as String
                // Acerta o tipo de tecnologia
                view.tvTechType.text = typeTech
                // Acerta o titulo
                view.tvTechTitle.text = title
                // Acerta a imagem
                Glide.with(view).asBitmap()
                    .load(img)
                    .into(view.ivTech)
                // Acerta os detalhes
                view.tvTechDetails.text = details
            }
        } catch (e: IllegalStateException) {
            Log.e("FavTechFrag", e.toString())
        }
        return view
    }

}