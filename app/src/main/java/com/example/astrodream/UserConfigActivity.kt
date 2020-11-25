package com.example.astrodream

import android.os.Bundle

class UserConfigActivity : ActivityWithTopBar(R.id.tbUserConfig, R.id.dlUserConfig) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_config)
        setUpMenuBehavior()
    }
}