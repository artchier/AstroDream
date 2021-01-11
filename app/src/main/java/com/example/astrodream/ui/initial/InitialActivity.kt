package com.example.astrodream.ui.initial

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.bumptech.glide.Glide
import com.example.astrodream.ui.mars.MarsActivity
import com.example.astrodream.R
import com.example.astrodream.services.service
import com.example.astrodream.ui.tech.TechActivity
import com.example.astrodream.ui.ActivityWithTopBar
import com.example.astrodream.ui.dailyimage.DailyImageActivity
import com.example.astrodream.ui.globe.GlobeActivity
import com.example.astrodream.ui.asteroids.AsteroidActivity
import com.example.astrodream.ui.plaindailymars.PlainActivityType
import com.example.astrodream.ui.plaindailymars.PlainViewModel
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.android.synthetic.main.activity_initial.*
import kotlinx.android.synthetic.main.activity_initial.cvDaily

class InitialActivity : ActivityWithTopBar(R.string.app_name, R.id.dlInitial) {

    private val viewModel by viewModels<PlainViewModel> {
        object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return PlainViewModel(service, PlainActivityType.Initial) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial)
        AndroidThreeTen.init(this)

        dailyImage()
        cvDaily.setOnClickListener {
            startActivity(Intent(this, DailyImageActivity::class.java))
        }

        btAsteriodes.setOnClickListener {
            startActivity(Intent(this, AsteroidActivity::class.java))
        }

        btGlobo.setOnClickListener {
            startActivity(Intent(this, GlobeActivity::class.java))
        }

        btTecnologias.setOnClickListener {
            startActivity(Intent(this, TechActivity::class.java))
        }

        btMarte.setOnClickListener {
            startActivity(Intent(this, MarsActivity::class.java))
        }

        setUpMenuBehavior()
    }

    private fun dailyImage() {
        piInitial.show()
        viewModel.populateList()
        viewModel.focusResult.observe(this) {
            val img = if (it.url != "") { it.url } else { R.drawable.no_internet }
            piInitial.hide()
            Glide.with(this).asBitmap()
                .load(img)
                .into(ivDaily)
        }
    }
}