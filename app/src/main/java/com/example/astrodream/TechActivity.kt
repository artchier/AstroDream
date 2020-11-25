package com.example.astrodream

import android.os.Bundle

class TechActivity : ActivityWithTopBar(R.id.tbTech, R.id.dlTech) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tech)
        setUpMenuBehavior()
    }
}