package com.example.astrodream

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.astrodream.domain.AsteroidsAdapter
import kotlinx.android.synthetic.main.fragment_listar_asteroides_proximos.*
import kotlinx.android.synthetic.main.fragment_listar_asteroides_proximos.view.*

class ListarAsteroidesProximosFragment(var adapter: RecyclerView.Adapter<AsteroidsAdapter.AsteroidsViewHolder>) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_listar_asteroides_proximos, container, false)
        view.rv_asteroid_btn.adapter = adapter
        return view
    }

    companion object {
        fun newInstance(adapter: RecyclerView.Adapter<AsteroidsAdapter.AsteroidsViewHolder>) = ListarAsteroidesProximosFragment(adapter)
    }
}