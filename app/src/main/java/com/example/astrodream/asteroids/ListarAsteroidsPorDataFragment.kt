package com.example.astrodream.asteroids

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.astrodream.R
import com.example.astrodream.asteroids.domain.AsteroidsAdapter

class ListarAsteroidsPorDataFragment(var adapter: RecyclerView.Adapter<AsteroidsAdapter.AsteroidsViewHolder>) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_listar_asteroids_por_data, container, false)
        return view
    }

    companion object {
        fun newInstance(adapter: RecyclerView.Adapter<AsteroidsAdapter.AsteroidsViewHolder>) =
            ListarAsteroidsPorDataFragment(
                adapter
            )
    }
}