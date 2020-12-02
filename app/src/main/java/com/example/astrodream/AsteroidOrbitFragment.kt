package com.example.astrodream

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_asteroid_orbit.view.*

class AsteroidOrbitFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_asteroid_orbit, container, false)
        val resources = view.resources
        try {
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

                view.tvAsteroidName.text = resources.getString(R.string.nome_template).format(name)

                Glide.with(view).asBitmap()
                    .load(img)
                    .into(view.ivAsteroid)

                view.tvAsteroidClass.text = resources.getString(R.string.classificacao_template).format(classification)
                view.tvAsteroidProxSun.text = resources.getString(R.string.aproximacao_sol_template).format(dateSun)
                view.tvAsteroidProxEarth.text = resources.getString(R.string.aproximacao_terra_template).format(date)
                view.tvAsteroidDistEarth.text = resources.getString(R.string.distancia_template).format(dist)
                view.tvAsteroidMag.text = resources.getString(R.string.magnitute_template).format(mag)
                view.tvAsteroidSpeed.text = resources.getString(R.string.velocidade_template).format(speed)

                view.ivAsteroid.setOnClickListener {
                    val intent: Intent = Intent(view.context, FullScreenImgActivity::class.java)
                    intent.putExtra("img", img)
                    startActivity(intent)

                }

            }
        } catch (e: IllegalStateException) {
            Log.e("AsteroidOrbitFrag", "Bundle vazio!")
        }
        return view
    }

    companion object {
        fun newInstance() = AsteroidOrbitFragment()
    }

}