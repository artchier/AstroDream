package com.example.astrodream.ui.mars

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.example.astrodream.R
import com.example.astrodream.ui.ActivityWithTopBar
import com.example.astrodream.ui.favorites.FavoritesActivity
import com.example.astrodream.ui.plaindailymars.PlainActivity
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_mars.*
import kotlinx.android.synthetic.main.activity_mars.bottomTabs
import kotlinx.android.synthetic.main.lateral_menu.*

class MarsActivity : PlainActivity(R.string.marte, "Mars") {

    override fun newDetailFrag(): Fragment {
        return RecentMarsFragment.newInstance()
    }

    override fun newHistoryFrag(): Fragment {
        return HistoryMarsFragment.newInstance()
    }
}