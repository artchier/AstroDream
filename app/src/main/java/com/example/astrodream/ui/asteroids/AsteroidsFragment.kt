package com.example.astrodream.ui.asteroids

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.astrodream.R
import kotlinx.android.synthetic.main.fragment_asteroids.view.*

class AsteroidsFragment : Fragment() {
    private val viewModel = AsteroidsFragmentViewModel(this)
    private val listAsteroidViews = ArrayList<ImageView>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_asteroids, container, false)
        viewModel.execute(view)

        listAsteroidViews.addAll(arrayListOf(view.iv_asteroids1, view.iv_asteroids2,
            view.iv_asteroids3, view.iv_asteroids4))

        val asteroidslist = arguments?.get("listFourAsteroids") as ArrayList<*>

        listAsteroidViews.forEach { it ->
            it.setOnClickListener {
            val bundle = bundleOf("Asteroid" to asteroidslist[listAsteroidViews.indexOf(it)],
                "listFourAsteroids" to asteroidslist)
            findNavController().navigate(R.id.action_asteroidsFragment_to_asteroidsDetailsFragment,
                bundle) } }

        return view
    }
}