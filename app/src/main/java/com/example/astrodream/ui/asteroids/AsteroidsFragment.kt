package com.example.astrodream.ui.asteroids

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.astrodream.R
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

    fun setImage(url: String, view: ImageView){
        Glide.with(this)
            .asGif()
            .load(url)
            .into(view)
    }
}