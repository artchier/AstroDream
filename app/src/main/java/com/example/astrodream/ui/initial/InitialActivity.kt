package com.example.astrodream.ui.initial

import android.content.Intent
import android.os.Bundle
import com.example.astrodream.ui.mars.MarsActivity
import com.example.astrodream.R
import com.example.astrodream.ui.tech.TechActivity
import com.example.astrodream.ui.asteroids.AsteroidActivity
import com.example.astrodream.ui.ActivityWithTopBar
import com.example.astrodream.ui.dailyimage.DailyImageActivity
import com.example.astrodream.ui.globe.GlobeActivity
import kotlinx.android.synthetic.main.activity_initial.*

class InitialActivity : ActivityWithTopBar(R.string.app_name, R.id.dlInitial) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial)

        ivDaily.setOnClickListener {
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
}