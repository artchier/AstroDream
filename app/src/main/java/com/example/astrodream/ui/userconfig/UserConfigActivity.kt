package com.example.astrodream.ui.userconfig

import android.os.Bundle
import com.example.astrodream.R
import com.example.astrodream.ui.ActivityWithTopBar

class UserConfigActivity : ActivityWithTopBar(R.string.user_config, R.id.dlUserConfig) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_config)
        setUpMenuBehavior()
    }
}