package com.example.astrodream.ui.asteroids

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.astrodream.R
import com.example.astrodream.domain.Asteroid
import kotlinx.android.synthetic.main.fragment_asteroids_details.view.*

class AsteroidsDetailsFragment : Fragment() {

    @SuppressLint("LongLogTag")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_asteroids_details, container, false)

        val asteroidList = arguments?.get("listFourAsteroids") as ArrayList<*>
        val bundle = bundleOf("listFourAsteroids" to asteroidList)
        val asteroid = arguments?.get("Asteroid") as Asteroid

        view.name_asteroid_fltransparente.text = asteroid.name

        view.tv_data_asteroid_fltransparent.text = context?.getString(R.string.data_asteroide, asteroid.close_approach_data)
        view.tv_tamanho_asteroid_fltransparent.text = context?.getString(R.string.tamanho_estimado_asteroide, asteroid.estimated_diameter)
        view.tv_velocidade_asteroid_fltransparent.text = context?.getString(R.string.velocidade_estimada_asteroide, asteroid.relative_velocity)

        view.arrowup.setOnClickListener { findNavController().navigate(R.id.action_asteroidsFragment_to_asteroidsDetailsFragment, bundle) }

            Glide.with(this)
                .asGif()
                .load(context?.resources?.getString(R.string.url_imagem_globo))
                .into(view.iv_background_terra)
            return view
    }

    fun loadProgressBar(){

    }
}