package com.example.astrodream

import android.os.Bundle

class InitialActivity : ActivityWithTopBar(R.id.tbInitial, R.id.dlInitial) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial)
        setUpMenuBehavior()
    }
}