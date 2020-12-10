<<<<<<< HEAD:app/src/main/java/com/example/astrodream/ui/asteroids/ListarAsteroidsPorDataFragment.kt
package com.example.astrodream.ui.asteroids
=======
package com.example.astrodream.asteroids
>>>>>>> AND-72:app/src/main/java/com/example/astrodream/asteroids/ListarAsteroidsPorDataFragment.kt

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.astrodream.R
<<<<<<< HEAD:app/src/main/java/com/example/astrodream/ui/asteroids/ListarAsteroidsPorDataFragment.kt
import kotlinx.android.synthetic.main.fragment_listar_asteroids_por_data.view.*
=======
import com.example.astrodream.asteroids.domain.AsteroidsAdapter
>>>>>>> AND-72:app/src/main/java/com/example/astrodream/asteroids/ListarAsteroidsPorDataFragment.kt

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