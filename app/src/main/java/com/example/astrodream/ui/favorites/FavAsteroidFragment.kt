package com.example.astrodream.ui.favorites

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.astrodream.ui.FullScreenImgActivity
import com.example.astrodream.R
import com.example.astrodream.domain.Asteroid
import kotlinx.android.synthetic.main.fragment_asteroid_orbit.view.*

class FavAsteroidFragment : Fragment() {

    val viewModel: FavViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_asteroid_orbit, container, false)

        val asteroid = viewModel.detail.value as Asteroid
        val name = asteroid.name
        val img = R.drawable.asteroide
        val hazardous = if(asteroid.is_potentially_hazardous_asteroid) { "Asteroide perigoso!" } else { "Tranquilo" }
        val mag = asteroid.absolute_magnitude
        val velocity = asteroid.relative_velocity
        val approachDate = asteroid.close_approach_data
        val missDist = asteroid.miss_distance
        val diameter = asteroid.estimated_diameter

        view.tvAsteroidName.text = "Asteroide ($name)"

        Glide.with(view).asBitmap()
            .load(img)
            .into(view.ivAsteroid)

        view.tvAsteroidHazardous.text = "Classificação: $hazardous"
        view.tvAsteroidMagnitude.text = "Magnitude absoluta: $mag"
        view.tvAsteroidVelocity.text = "Velocidade: $velocity"
        view.tvAsteroidCloseApproachDate.text = "Data de aproximação da terra: $approachDate"
        view.tvAsteroidMissDistance.text = "Distancia: $missDist"
        view.tvAsteroidDiameter.text = "Diametro: $diameter"

        return view
    }
}