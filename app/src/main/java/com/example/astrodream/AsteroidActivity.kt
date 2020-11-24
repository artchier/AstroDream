package com.example.astrodream

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.astrodream.domain.Asteroids
import com.example.astrodream.domain.AsteroidsAdapter
import kotlinx.android.synthetic.main.activity_asteroid.*

class AsteroidActivity : AppCompatActivity(), AsteroidsAdapter.OnClickAsteroidsListener {
    val adapter = AsteroidsAdapter(this, getAsteroids())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asteroid)

        val asteroidesProximos = ListarAsteroidesProximosFragment.newInstance(adapter)

        btn_asteroids_proximos.setOnClickListener {
            alterColorBtnAsteroids(btn_asteroids_proximos)

            supportFragmentManager.beginTransaction().apply {
                add(R.id.fl_btn_rv, asteroidesProximos)
                commit()
            }
        }
    }

    fun alterColorBtnAsteroids(view: View){
        view.background = getDrawable(R.drawable.button_style_click)
    }

    fun getAsteroids(): ArrayList<Asteroids>{
        return arrayListOf(Asteroids("Ananda", "24/11/2020"),
            Asteroids("Arthur", "24/11/2020"),
            Asteroids("Marina", "24/11/2020"),
            Asteroids("Rafael", "24/11/2020"),
            Asteroids("Raul", "24/11/2020"))
    }

    override fun onClickAsteroids(position: Int) {
        TODO("Not yet implemented")
    }
}