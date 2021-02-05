package com.example.astrodream.ui.asteroids

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.astrodream.R
import com.example.astrodream.domain.Asteroid
import com.example.astrodream.domain.util.AstroDreamUtil
import com.example.astrodream.domain.util.isInternetAvailable
import kotlinx.android.synthetic.main.fragment_asteroids.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AsteroidsFragment : Fragment() {
    private val viewModel = AsteroidsFragmentViewModel(this)
    private val listAsteroidViews = ArrayList<ImageView>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_asteroids, container, false)
        viewModel.execute(view)

        listAsteroidViews.addAll(
            arrayListOf(
                view.iv_asteroids1, view.iv_asteroids2,
                view.iv_asteroids3, view.iv_asteroids4
            )
        )

        val asteroidslist = arguments?.get("listFourAsteroids") as ArrayList<*>

        listAsteroidViews.forEach { it ->
            it.setOnClickListener {
                //progressbar.visibility = LinearLayout.GONE
                val bundle: Bundle? = bundleOf(
                    "Asteroid" to (asteroidslist?.get(listAsteroidViews.indexOf(it))),
                    "listFourAsteroids" to asteroidslist
                )
                findNavController().navigate(
                    R.id.action_asteroidsFragment_to_asteroidsDetailsFragment,
                    bundle
                )
            }
        }
        return view
    }
}