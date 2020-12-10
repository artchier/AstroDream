package com.example.astrodream.asteroids

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.astrodream.R
import com.example.astrodream.asteroids.domain.AsteroidsAdapter
import kotlinx.android.synthetic.main.fragment_listar_asteroids_por_nome.view.*

class ListarAsteroidsPorNomeFragment(var adapter: RecyclerView.Adapter<AsteroidsAdapter.AsteroidsViewHolder>) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_listar_asteroids_por_nome, container, false)
        view.rv_asteroidnome_btn.adapter = adapter
        return view
    }

    companion object {
        fun newInstance(adapter: RecyclerView.Adapter<AsteroidsAdapter.AsteroidsViewHolder>) =
            ListarAsteroidsPorNomeFragment(
                adapter
            )
    }
}