package com.example.astrodream.ui.tech

import android.os.Bundle
import com.example.astrodream.R
import com.example.astrodream.domain.util.AstroDreamUtil
import com.example.astrodream.domain.util.isInternetAvailable
import com.example.astrodream.domain.util.showDialogMessage
import com.example.astrodream.domain.util.showErrorInternetConnection
import com.example.astrodream.ui.ActivityWithTopBar
import kotlinx.android.synthetic.main.activity_globe.*
import kotlinx.android.synthetic.main.activity_tech.*

class TechActivity : ActivityWithTopBar(R.string.tecnologias, R.id.dlTech) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tech)
        setUpMenuBehavior()

        if(!AstroDreamUtil.isInternetAvailable(this))
            AstroDreamUtil.showErrorInternetConnection(this)

        realtimeViewModel.activeUser.observe(this) {
            tvTotalTech.text = it.nasaCoins.toString()
        }
    }

    override fun onStop() {
        super.onStop()

        realtimeViewModel.updateUserNasaCoins(
            realtimeViewModel.activeUser.value?.email!!,
            tvTotalTech.text.toString().toLong()
        )
    }
}