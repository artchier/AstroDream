package com.example.astrodream.ui.asteroids

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.astrodream.ui.FullScreenImgActivity
import com.example.astrodream.R
import kotlinx.android.synthetic.main.fragment_asteroid_orbit.view.*

class AsteroidOrbitFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_asteroid_orbit, container, false)
        try {
            if (requireArguments() != null) {
                if (container != null) {

                    // TODO: buscar todos os dados que serão passados pelo bundle
                    val name = requireArguments().getString("name") as String
                    val date = requireArguments().getString("date") as String
                    val img = "https://s.yimg.com/ny/api/res/1.2/3P2Yc9yGrc99m.i3sSXWwA--/YXBwaWQ9aGlnaGxhbmRlcjt3PTk2MA--/https://media.zenfs.com/pt-br/canal_tech_990/800089564cde284dc55f155406c8e54e" //requireArguments().getString("img") as String
                    val classification = "Apollo [ NEO, PHA ]" //requireArguments().getString("classification") as String
                    val dateSun = "29/01/1980" //requireArguments().getString("dateSun") as String
                    val dist = "9,29" //requireArguments().getString("dist") as String
                    val mag = "18,6" //requireArguments().getString("mag") as String
                    val speed = "23" //requireArguments().getString("speed") as String

                    view.tvAsteroidName.text = "Asteroide ($name)"

                    Glide.with(view).asBitmap()
                        .load(img)
                        .into(view.ivAsteroid)

                    view.tvAsteroidClass.text = "Classificação: $classification"
                    view.tvAsteroidProxSun.text = "Aproximação do sol: $dateSun"
                    view.tvAsteroidProxEarth.text = "Aproximação da terra: $date"
                    view.tvAsteroidDistEarth.text = "Distância mínima da terra: $dist LD"
                    view.tvAsteroidMag.text = "Magnitude Absoluta: $mag"
                    view.tvAsteroidSpeed.text = "Velocidade: $speed km/s"

                    view.ivAsteroid.setOnClickListener {
                        val intent: Intent = Intent(view.context, FullScreenImgActivity::class.java)
                        intent.putExtra("img", img)
                        startActivity(intent)

                    }

                }
            }
        } catch (e: IllegalStateException) {
            Log.e("AsteroidOrbitFrag", "Bundle vazio!")
        }


        return view
    }
}