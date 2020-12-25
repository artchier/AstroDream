package com.example.astrodream.ui.asteroids

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.astrodream.R
import com.example.astrodream.domain.Asteroid
import kotlinx.android.synthetic.main.fragment_asteroids.view.*

class AsteroidsFragment() : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_asteroids, container, false)

        setImage("https://i.ibb.co/gzJBqz8/planeta.gif", view.iv_terra)
        setImage("https://i.ibb.co/58CXFCf/20201208-111716.gif", view.iv_asteroids1)
        setImage("https://i.ibb.co/wyvJtFV/20201208-111454.gif", view.iv_asteroids2)
        setImage("https://i.ibb.co/Y72DVrF/20201208-111404.gif", view.iv_asteroids3)
        setImage("https://i.ibb.co/dWcYtQz/20201208-111626.gif", view.iv_asteroids4)

        val asteroidslist = arguments?.get("listFourAsteroids") as ArrayList<Asteroid>
        Log.i("AsteroidsList", asteroidslist.toString())

        view.iv_asteroids1.setOnClickListener {
            val bundle = bundleOf("Asteroid" to asteroidslist[0], "listFourAsteroids" to asteroidslist)
            findNavController().navigate(R.id.action_asteroidsFragment_to_asteroidsDetailsFragment, bundle)
        }

        view.iv_asteroids2.setOnClickListener {
            val bundle = bundleOf("Asteroid" to asteroidslist[1], "listFourAsteroids" to asteroidslist)
            findNavController().navigate(R.id.action_asteroidsFragment_to_asteroidsDetailsFragment, bundle)
        }

        view.iv_asteroids3.setOnClickListener {
            val bundle = bundleOf("Asteroid" to asteroidslist[2], "listFourAsteroids" to asteroidslist)
            findNavController().navigate(R.id.action_asteroidsFragment_to_asteroidsDetailsFragment, bundle)
        }

        view.iv_asteroids4.setOnClickListener {
            val bundle = bundleOf("Asteroid" to asteroidslist[3], "listFourAsteroids" to asteroidslist)
            findNavController().navigate(R.id.action_asteroidsFragment_to_asteroidsDetailsFragment, bundle)

        }
        return view
    }

    fun setImage(url: String, view: ImageView){
        Glide.with(this)
            .asGif()
            .load(url)
            .into(view)
    }
}