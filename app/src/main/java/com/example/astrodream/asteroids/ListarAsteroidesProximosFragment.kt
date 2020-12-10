<<<<<<< HEAD:app/src/main/java/com/example/astrodream/ui/asteroids/ListarAsteroidesProximosFragment.kt
package com.example.astrodream.ui.asteroids
=======
package com.example.astrodream.asteroids
>>>>>>> AND-72:app/src/main/java/com/example/astrodream/asteroids/ListarAsteroidesProximosFragment.kt

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.astrodream.R
<<<<<<< HEAD:app/src/main/java/com/example/astrodream/ui/asteroids/ListarAsteroidesProximosFragment.kt
=======
import com.example.astrodream.asteroids.domain.AsteroidsAdapter
>>>>>>> AND-72:app/src/main/java/com/example/astrodream/asteroids/ListarAsteroidesProximosFragment.kt
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
        fun newInstance(adapter: RecyclerView.Adapter<AsteroidsAdapter.AsteroidsViewHolder>) =
            ListarAsteroidesProximosFragment(
                adapter
            )
    }
}