package com.example.astrodream.ui.tech

import android.os.Bundle
import com.example.astrodream.R
import com.example.astrodream.domain.util.AstroDreamUtil
import com.example.astrodream.domain.util.isInternetAvailable
import com.example.astrodream.domain.util.showDialogMessage
import com.example.astrodream.domain.util.showErrorInternetConnection
import com.example.astrodream.ui.ActivityWithTopBar

class TechActivity : ActivityWithTopBar(R.string.tecnologias, R.id.dlTech) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tech)
        setUpMenuBehavior()

        if(!AstroDreamUtil.isInternetAvailable(this))
            AstroDreamUtil.showErrorInternetConnection(this)
    }
}