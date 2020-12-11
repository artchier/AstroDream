package com.example.astrodream.ui.dailyimage

import androidx.fragment.app.*
import com.example.astrodream.R
import com.example.astrodream.ui.plaindailymars.PlainActivity
import com.example.astrodream.ui.plaindailymars.PlainActivityType

class DailyImageActivity : PlainActivity(R.string.daily_image, PlainActivityType.DailyImage) {

    override fun newDetailFrag(): Fragment {
        return DailyImageFragment.newInstance()
    }

    override fun newHistoryFrag(): Fragment {
        return DailyImageHistoryFragment.newInstance()
    }

}