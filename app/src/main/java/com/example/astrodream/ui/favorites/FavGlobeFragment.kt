package com.example.astrodream.ui.favorites

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.astrodream.R
import kotlinx.android.synthetic.main.fragment_fav_globe.view.*

class FavGlobeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_fav_globe, container, false)
        try {
            // Caso tenha dados no bundle, ou seja, o fragment foi carregado a partir do Favoritos,
            // atualiza a imagem e textos de acordo com a informação do favorito clicado lá no Favoritos
            if (container != null) {
                // Salva dados do bundle em variaveis
                val date = requireArguments().getString("date") as String
                val img = requireArguments().getString("img") as String
                // Acerta a data
                view.tvGlobeDate.text = date
                // Acerta a imagem
                Glide.with(view).asBitmap()
                    .load(img)
                    .into(view.ivGlobe)
            }
        } catch (e: IllegalStateException) {
            Log.e("FavGlobeFrag", e.toString())
        }
        return view
    }

}