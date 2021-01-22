package com.example.astrodream.ui.favorites

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.astrodream.R
import com.example.astrodream.domain.Asteroid
import com.example.astrodream.entitiesDatabase.AsteroidRoom
import kotlinx.android.synthetic.main.fragment_asteroid_favs.view.*

class FavAsteroidFragment : Fragment() {

    val viewModel: FavViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_asteroid_favs, container, false)

        val asteroid = viewModel.detail.value as AsteroidRoom
        val name = asteroid.codeAsteroid
        val url = asteroid.urlOrbit
        val descricao = asteroid.description

        view.nome_asteroid_favs.text = "Asteroide $name"
        view.findViewById<View>(R.id.asteroid_favs_fragment).setOnClickListener {

        }

        view.btn_ver_orbita_favs.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }

        view.descricao_asteroids_favs.text = descricao

        return view
    }
}