package com.example.astrodream.ui.dailyimage

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.astrodream.R
import com.example.astrodream.services.service
import com.example.astrodream.ui.ActivityWithTopBar
import com.example.astrodream.ui.initial.InitialActivity
import com.example.astrodream.ui.plaindailymars.PlainActivity
import com.example.astrodream.ui.plaindailymars.PlainViewModel
import com.google.android.material.tabs.TabLayout
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.android.synthetic.main.activity_daily_image.*
import kotlinx.android.synthetic.main.activity_mars.bottomTabs

class DailyImageActivity() : PlainActivity(R.string.daily_image, "Daily") {

    override fun newDetailFrag(): Fragment {
        return DailyImageFragment.newInstance()
    }

    override fun newHistoryFrag(): Fragment {
        return DailyImageHistoryFragment.newInstance()
    }

}