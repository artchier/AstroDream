package com.example.astrodream.favorites

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.astrodream.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_fav_tech.view.*
import kotlinx.android.synthetic.main.fragment_fav_today.view.*
import kotlinx.android.synthetic.main.fragment_fav_today.view.ivToday

class FavTechFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_fav_tech, container, false)

        try {
            // Caso tenha dados no bundle, ou seja, o fragment foi carregado a partir do Favoritos,
            // atualiza a imagem e textos de acordo com a informação do favorito clicado lá no Favoritos
            if (requireArguments() != null) {
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
                    Picasso.get().load(img).into(view.ivTech)
                    // Acerta os detalhes
                    view.tvTechDetails.text = details
                }
            }
        } catch (e: Exception) {
            Log.e("FavTechFrag", e.toString())
        }
        return view
    }

}