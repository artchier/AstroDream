package com.example.astrodream

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_asteroid_orbit.view.*
import kotlinx.android.synthetic.main.fragment_fav_asteroids.view.*
import kotlinx.android.synthetic.main.fragment_fav_asteroids.view.ivAsteroid
import kotlinx.android.synthetic.main.fragment_fav_asteroids.view.tvAsteroidData
import kotlinx.android.synthetic.main.fragment_fav_asteroids.view.tvAsteroidName

class AsteroidOrbitFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_asteroid_orbit, container, false)
        try {
            // Pega dados do bundle e atualiza a imagem e textos de acordo com o asteroide clicado
            if (requireArguments() != null) {
                if (container != null) {
                    // Salva dados do bundle em variaveis
                    val name = requireArguments().getString("name") as String
                    val data = requireArguments().getString("data") as String
                    val img = "https://s.yimg.com/ny/api/res/1.2/3P2Yc9yGrc99m.i3sSXWwA--/YXBwaWQ9aGlnaGxhbmRlcjt3PTk2MA--/https://media.zenfs.com/pt-br/canal_tech_990/800089564cde284dc55f155406c8e54e"
                    // Acerta o nome
                    view.tvAsteroidName.text = "Asteroide ($name)"
                    // Acerta a imagem
                    Glide.with(view).asBitmap()
                        .load(img)
                        .into(view.ivAsteroid)
                    // Acerta os dados
                    view.tvAsteroidClass.text = "Classificação: Apollo [ NEO, PHA ]"
                    view.tvAsteroidProxSun.text = "Aproximação do sol: 29/01/1980"
                    view.tvAsteroidProxEarth.text = "Aproximação da terra: $data"
                    view.tvAsteroidDistEarth.text = "Distância mínima da terra: 9.29 LD"
                    view.tvAsteroidMag.text = "Magnitude Absoluta: 18,6"
                    view.tvAsteroidSpeed.text = "Velocidade: 23 km/s"
                }
            }
        } catch (e: Exception) {
            Log.e("AsteroidOrbitFrag", e.toString())
        }
        return view
    }
}