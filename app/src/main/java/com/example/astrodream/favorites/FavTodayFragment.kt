package com.example.astrodream.favorites

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.astrodream.FullScreenImgActivity
import com.example.astrodream.R
import kotlinx.android.synthetic.main.fragment_fav_today.view.*

class FavTodayFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_fav_today, container, false)
        try {
            // Caso tenha dados no bundle, ou seja, o fragment foi carregado a partir do Favoritos,
            // atualiza a imagem e textos de acordo com a informação do favorito clicado lá no Favoritos
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
                Glide.with(view).asBitmap()
                    .load(img)
                    .into(view.ivToday)

                view.ivToday.setOnClickListener {
                    val intent: Intent = Intent(view.context, FullScreenImgActivity::class.java)
                    intent.putExtra("img", img)
                    startActivity(intent)

                }
            }
        } catch (e: IllegalStateException) {
            Log.e("FavTodayFrag", e.toString())
        }
        return view
    }

}