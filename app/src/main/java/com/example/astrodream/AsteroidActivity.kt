package com.example.astrodream

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.findFragment
import com.example.astrodream.domain.Asteroids
import com.example.astrodream.domain.AsteroidsAdapter
import kotlinx.android.synthetic.main.activity_asteroid.*
import kotlinx.android.synthetic.main.activity_login.view.*
import kotlinx.android.synthetic.main.btn_asteroids.view.*
import kotlinx.android.synthetic.main.fragment_listar_asteroides_proximos.view.*

class AsteroidActivity : AppCompatActivity(), AsteroidsAdapter.OnClickAsteroidsListener {

    val adapter = AsteroidsAdapter(this, getAsteroids())
    var click = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asteroid)

        btn_asteroids_proximos.apply {
            findViewById<TextView>(R.id.tv_btn_name).text = "Listar asteroides proximos"
                openCloseButton(this, R.id.fl_btn_rv, ListarAsteroidesProximosFragment.newInstance(adapter), findViewById<LinearLayout>(R.id.btn_superior_asteroids))
        }

        btn_asteroids_name.apply {
            findViewById<TextView>(R.id.tv_btn_name).text = "Listar asteroides por nome"
                openCloseButton(this, R.id.fl_btn_rv, ListarAsteroidsPorNomeFragment.newInstance(adapter),findViewById<LinearLayout>(R.id.btn_superior_asteroids))
        }

        btn_asteroids_date.apply {
            findViewById<TextView>(R.id.tv_btn_name).text = "Listar asteroides por data"
            openCloseButton(this, R.id.fl_btn_rv, ListarAsteroidsPorNomeFragment.newInstance(adapter), findViewById<LinearLayout>(R.id.btn_superior_asteroids))
        }

        btn_asteroids_dangerous.apply {
            findViewById<TextView>(R.id.tv_btn_name).text = "Listar asteroides perigosos"
            openCloseButton(this, R.id.fl_btn_rv, ListarAsteroidsPorNomeFragment.newInstance(adapter), findViewById<LinearLayout>(R.id.btn_superior_asteroids))
        }

    }

    fun openCloseButton(view: View, id_oldFragment: Int, newFragment: Fragment, btn: LinearLayout) {
        btn.setOnClickListener {
            if (!click) openButton(view, id_oldFragment, newFragment)
            else closeButton(view)
        }
    }

    fun openButton(view: View, id_oldfragment: Int, newFragment: Fragment){
        alterColorBtnAsteroids(view, R.drawable.button_style_click)
        alterArrowButton(R.drawable.ic_arrow_up)
        supportFragmentManager.beginTransaction().apply {
            replace(id_oldfragment, newFragment)
            addToBackStack(null)
            commit()
        }
        click = true
    }

    fun closeButton(view: View) {
        onBackPressed()
        alterColorBtnAsteroids(view, R.drawable.button_style)
        alterArrowButton(R.drawable.ic_arrow_down)
        click = false
    }

    fun alterArrowButton(drawableId: Int){
        findViewById<ImageView>(R.id.ic_btn).setImageDrawable(getDrawable(drawableId))
    }

    fun alterColorBtnAsteroids(view: View, id: Int){ view.background = getDrawable(id) }

    fun getAsteroids(): ArrayList<Asteroids> {
        return arrayListOf(
            Asteroids("Ananda", "24/11/2020"),
            Asteroids("Arthur", "24/11/2020"),
            Asteroids("Marina", "24/11/2020"),
            Asteroids("Rafael", "24/11/2020"),
            Asteroids("Raul", "24/11/2020")
        )
    }

    override fun onClickAsteroids(position: Int) {
        TODO("Not yet implemented")
    }
}