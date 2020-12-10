package com.example.astrodream.asteroids

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.astrodream.R
import kotlinx.android.synthetic.main.fragment_asteroids_details.view.*

class AsteroidsDetailsFragment() : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_asteroids_details, container, false)

        view.arrowup.setOnClickListener {
            findNavController().navigate(R.id.action_asteroidsDetailsFragment_to_asteroidsFragment)
        }
        Glide.with(this)
            .asGif()
            .load("https://i.ibb.co/gzJBqz8/planeta.gif")
            .into(view.iv_background_terra)
        return view
    }
}