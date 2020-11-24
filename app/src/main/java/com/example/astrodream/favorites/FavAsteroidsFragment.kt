package com.example.astrodream.favorites

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.astrodream.R
import kotlinx.android.synthetic.main.fragment_fav_asteroids.view.*

class FavAsteroidsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_fav_asteroids, container, false)
        try {
            // Caso tenha dados no bundle, ou seja, o fragment foi carregado a partir do Favoritos,
            // atualiza a imagem e textos de acordo com a informação do favorito clicado lá no Favoritos
            if (requireArguments() != null) {
                if (container != null) {
                    // Salva dados do bundle em variaveis
                    val name = requireArguments().getString("name") as String
                    val data = requireArguments().getString("data") as String
                    val img = requireArguments().getString("img") as String
                    // Acerta o nome
                    view.tvAsteroidName.text = name
                    // Acerta os dados
                    view.tvAsteroidData.text = data
                    // Acerta a imagem
                    Glide.with(view).asBitmap()
                        .load(img)
                        .into(view.ivAsteroid)
                }
            }
        } catch (e: Exception) {
            Log.e("FavAsteroidFrag", e.toString())
        }
        return view
    }

}