package com.example.astrodream

import android.os.Bundle

class DailyImageActivity : ActivityWithTopBar(R.string.daily_image, R.id.dlDaily) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_image)
        setUpMenuBehavior()
    }
}