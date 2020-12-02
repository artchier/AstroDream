package com.example.astrodream.daily

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
import kotlinx.android.synthetic.main.fragment_daily.view.*
import kotlinx.android.synthetic.main.fragment_fav_today.view.*

class DailyFragment : Fragment() {

    private lateinit var dailyPic: Daily

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_daily, container, false)

        dailyPic = Daily(
            "Dark Molecular Cloud Barnard 68",
            "22 de Novembro de 2020",
            "https://apod.nasa.gov/apod/image/2011/barnard68v2_vlt_960.jpg"
        )

        view.tvTitle.text = dailyPic.title
        Glide.with(view).asBitmap()
            .load(dailyPic.img)
            .into(view.ivDaily)
        view.tvDate.text = dailyPic.date

        view.ivDaily.setOnClickListener {
            val intent: Intent = Intent(view.context, FullScreenImgActivity::class.java)
            intent.putExtra("img", dailyPic.img)
            startActivity(intent)

        }

        try {
            // Caso tenha dados no bundle, ou seja, o fragment foi carregado a partir do Historico,
            // atualiza as imagens e textos de acordo com a informação do post clicado lá no Historico
            if (container != null) {
                // Salva dados do bundle em variaveis
                val img: String = requireArguments().getString("img") as String
                val date = requireArguments().getString("date") as String
                val title = requireArguments().getString("title") as String
                // Acerta o titulo
                view.tvTitle.text = title
                // Atualiza a imagem
                Glide.with(view).asBitmap()
                    .load(img)
                    .into(view.ivDaily)
                // Mostra a data
                view.tvDate.text = date

                view.ivDaily.setOnClickListener {
                    val intent: Intent = Intent(view.context, FullScreenImgActivity::class.java)
                    intent.putExtra("img", img)
                    startActivity(intent)

                }
            }
            // TODO aqui está sempre dando erro por conta de requireArguments (não tem argumentos)
        } catch (e: IllegalStateException) {
            Log.e("RecentMarsFragment", e.toString())
        }



        return view
    }



}