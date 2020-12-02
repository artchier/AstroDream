package com.example.astrodream.ui.asteroids

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.Fragment
import com.example.astrodream.R
import com.example.astrodream.domain.Asteroids
import com.example.astrodream.ui.ActivityWithTopBar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_asteroid.*

class AsteroidActivity : ActivityWithTopBar(R.string.asteroides, R.id.dlAsteroids),
    AsteroidsAdapter.OnClickAsteroidsListener {

    val adapter = AsteroidsAdapter(this, getAsteroids())
    var click = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asteroid)

        btn_asteroids_proximos.apply {
                openCloseButton(this,
                    R.id.fl_btn_proximos,
                    ListarAsteroidesProximosFragment.newInstance(adapter), findViewById(R.id.btn_superior_asteroids_1),
                    R.id.ic_btn_1
                )
        }

        btn_asteroids_name.apply {
                openCloseButton(this,
                    R.id.fl_btn_nome,
                    ListarAsteroidsPorNomeFragment.newInstance(adapter),findViewById(R.id.btn_superior_asteroids_2),
                    R.id.ic_btn_2
                )
        }

        btn_asteroids_date.apply {
            openCloseButton(this,
                R.id.fl_btn_data,
                ListarAsteroidsPorDataFragment.newInstance(adapter), findViewById(R.id.btn_superior_asteroids_3),
                R.id.ic_btn_3
            )
        }

        btn_asteroids_dangerous.apply {
            openCloseButton(this,
                R.id.fl_btn_perigosos,
                ListarAsteroidsPorNomeFragment.newInstance(adapter), findViewById(R.id.btn_superior_asteroids_4),
                R.id.ic_btn
            )
        }

//        btn_ver_orbita.setOnClickListener {
//            supportFragmentManager.beginTransaction().apply {
//                replace(R.id.fl_asteroids, AsteroidOrbitFragment.newInstance())
//                commit()
//            }
//        }

        setUpMenuBehavior()
    }

    fun openCloseButton(view: View, id_oldFragment: Int, newFragment: Fragment, btn: LinearLayout, ic_btn: Int) {
        btn.setOnClickListener {
            if (!click) openButton(view, id_oldFragment, newFragment, ic_btn)
            else closeButton(view, ic_btn)
        }
    }

    fun openButton(view: View, id_oldfragment: Int, newFragment: Fragment, ic_btn: Int){
        alterColorBtnAsteroids(view, R.drawable.button_style_click)
        alterArrowButton(ic_btn, R.drawable.ic_arrow_up)
        supportFragmentManager.beginTransaction().apply {
            replace(id_oldfragment, newFragment)
            addToBackStack(null)
            commit()
        }
        click = true
    }

    fun closeButton(view: View, ic_btn: Int) {
        onBackPressed()
        alterColorBtnAsteroids(view, R.drawable.button_style)
        alterArrowButton(ic_btn, R.drawable.ic_arrow_down)
        click = false
    }

    fun alterArrowButton(drawableAtual: Int, drawablePosterior: Int){ findViewById<ImageView>(drawableAtual).setImageDrawable(getDrawable(drawablePosterior)) }

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
        run {
            MaterialAlertDialogBuilder(this)
                .setBackgroundInsetStart(70)
                .setBackgroundInsetEnd(70)
                .setBackgroundInsetTop(10)
                .setBackgroundInsetBottom(100)
                .setBackground(
                    ContextCompat.getColor(this, android.R.color.transparent).toDrawable()
                )
                .setView(R.layout.asteroid_dialog)
                .show()
        }
    }
}
