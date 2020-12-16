package com.example.astrodream.ui.asteroids

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.astrodream.R

class ListarAsteroidsPorNomeFragment() : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_listar_asteroids_por_nome, container, false)
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
    }
}