package com.example.astrodream.ui.asteroids

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
import kotlinx.android.synthetic.main.fragment_asteroids_details.*
import kotlinx.android.synthetic.main.fragment_asteroids_details.view.*

class AsteroidsDetailsFragment() : Fragment() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_asteroids_details, container, false)

        val asteroidList = arguments?.get("listFourAsteroids") as ArrayList<Asteroid>
        val bundle = bundleOf("listFourAsteroids" to asteroidList)
        val asteroid = arguments?.get("Asteroid") as Asteroid
        view.name_asteroid_fltransparente.text = asteroid.name
        view.tv_data_asteroid_fltransparent.text = "Data de aproximação: ${asteroid.getDataFormatada()}"
//        view.tv_tamanho_asteroid_fltransparent.text = asteroid.absolute_magnitude.toString()
//        view.tv_velocidade_asteroid_fltransparent.text = asteroid.relative_velocity.toString()

        view.arrowup.setOnClickListener {
            findNavController().navigate(R.id.action_asteroidsFragment_to_asteroidsDetailsFragment, bundle)
        }

        Glide.with(this)
            .asGif()
            .load("https://i.ibb.co/gzJBqz8/planeta.gif")
            .into(view.iv_background_terra)
        return view
    }
}