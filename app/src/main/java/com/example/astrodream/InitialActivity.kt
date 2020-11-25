package com.example.astrodream

import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_initial.*

class InitialActivity : ActivityWithTopBar(R.string.app_name, R.id.dlInitial) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial)

        // TODO navegar parar as Activities apropriadas quando prontas
        vpImagemHoje.setOnClickListener {
//            startActivity(Intent(this, ))
        }

        btAsteriodes.setOnClickListener {
//            startActivity(Intent(this, ))
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