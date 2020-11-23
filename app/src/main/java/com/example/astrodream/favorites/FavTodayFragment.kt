package com.example.astrodream.favorites

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.astrodream.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_fav_today.view.*

class FavTodayFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_fav_today, container, false)
        try {
            // Caso tenha dados no bundle, ou seja, o fragment foi carregado a partir do Favoritos,
            // atualiza a imagem e textos de acordo com a informação do favorito clicado lá no Favoritos
            if (requireArguments() != null) {
                if (container != null) {
                    // Salva dados do bundle em variaveis
                    val title = requireArguments().getString("title") as String
                    val date = requireArguments().getString("date") as String
                    val img = requireArguments().getString("img") as String
                    // Acerta o titulo
                    view.tvTodayTitle.text = title
                    // Acerta a data
                    view.tvTodayDate.text = date
                    // Acerta a imagem
                    Picasso.get().load(img).into(view.ivToday)
                }
            }
        } catch (e: Exception) {
            Log.e("FavTodayFrag", e.toString())
        }
        return view
    }

}