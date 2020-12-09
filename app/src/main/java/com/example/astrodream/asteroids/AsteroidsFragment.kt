package com.example.astrodream.asteroids

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.astrodream.R
import kotlinx.android.synthetic.main.fragment_asteroids.*
import kotlinx.android.synthetic.main.fragment_asteroids.view.*

class AsteroidsFragment() : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_asteroids, container, false)

        Glide.with(this)
            .asGif()
            .load("https://i.ibb.co/gzJBqz8/planeta.gif")
            .into(view.iv_terra)

        Glide.with(this)
            .asGif()
            .load("https://i.ibb.co/58CXFCf/20201208-111716.gif")
            .into(view.iv_asteroids1)

        Glide.with(this)
            .asGif()
            .load("https://i.ibb.co/wyvJtFV/20201208-111454.gif")
            .into(view.iv_asteroids2)

        Glide.with(this)
            .asGif()
            .load("https://i.ibb.co/Y72DVrF/20201208-111404.gif")
            .into(view.iv_asteroids3)

        Glide.with(this)
            .asGif()
            .load("https://i.ibb.co/dWcYtQz/20201208-111626.gif")
            .into(view.iv_asteroids4)

        view.iv_asteroids1.setOnClickListener {
            findNavController().navigate(R.id.action_asteroidsFragment_to_asteroidsDetailsFragment)
        }

        view.iv_asteroids2.setOnClickListener {
            findNavController().navigate(R.id.action_asteroidsFragment_to_asteroidsDetailsFragment)
        }

        view.iv_asteroids3.setOnClickListener {
            findNavController().navigate(R.id.action_asteroidsFragment_to_asteroidsDetailsFragment)
        }

        view.iv_asteroids4.setOnClickListener {
            findNavController().navigate(R.id.action_asteroidsFragment_to_asteroidsDetailsFragment)
        }
        return view
    }
}