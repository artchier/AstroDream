package com.example.astrodream.ui.favorites

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.astrodream.R
import com.example.astrodream.database.AppDatabase
import com.example.astrodream.entitiesDatabase.AsteroidRoom
import com.example.astrodream.services.ServiceDBAsteroidsImpl
import com.example.astrodream.services.databaseReference
import com.example.astrodream.services.service
import com.example.astrodream.services.shareText
import com.example.astrodream.ui.asteroids.AsteroidViewModel
import kotlinx.android.synthetic.main.fragment_asteroid_favs.view.*

class FavAsteroidFragment : Fragment() {

    val viewModel: FavViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_asteroid_favs, container, false)

        val asteroid = viewModel.detail.value as AsteroidRoom
        val name = asteroid.codeAsteroid
        val url = asteroid.urlOrbit
        val descricao = asteroid.description

        view.descricao_asteroids_favs.text = descricao
        view.nome_asteroid_favs.text = getString(R.string.asteroid_template).format(name)

        view.findViewById<View>(R.id.asteroid_favs_fragment).setOnClickListener {
            val db = AppDatabase.invoke(requireActivity())
            val serviceDB = ServiceDBAsteroidsImpl(db.asteroidDAO())
            val viewModel by viewModels<AsteroidViewModel> {
                object : ViewModelProvider.Factory {
                    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                        return AsteroidViewModel(service, serviceDB, databaseReference, requireActivity()) as T
                    }
                }
            }
            viewModel.deleteAsteroidInDB(asteroid)
            requireActivity().onBackPressed()
        }

        view.btn_ver_orbita_favs.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        view.findViewById<ImageButton>(R.id.btnShareAsteroid).setOnClickListener {
            shareText("Veja informações do asteróide $name", url, requireContext())
        }

        return view
    }
}