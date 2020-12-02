package com.example.astrodream.ui.dailyimage

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.example.astrodream.ui.initial.InitialActivity
import com.example.astrodream.R
import com.example.astrodream.ui.ActivityWithTopBar
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_daily_image.*
import kotlinx.android.synthetic.main.activity_mars.bottomTabs

class DailyImageActivity : ActivityWithTopBar(R.string.daily_image, R.id.dlDaily) {

    private lateinit var navController : NavController
    private lateinit var appBarConfiguration : AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_image)
        setUpMenuBehavior()

        navController = findNavController(R.id.navHostfragDaily)
        appBarConfiguration = AppBarConfiguration(navController.graph)

        // Nevegação entre os tabs inferiores
        bottomTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        // Se o fragment atual for o HistoryMarsFragment:
                        try { findNavController(R.id.navHostfragDaily).navigate(R.id.action_dailyHistoryFragment_to_dailyFragment) }
                        // Se o fragment atual for o RecentMarsFragment mas a tab selecionada for a Historico (tab 1):
                        catch (e:Exception) { findNavController(R.id.navHostfragDaily).navigate(R.id.dailyFragment) }
                    }
                    1 -> findNavController(R.id.navHostfragDaily).navigate(R.id.action_dailyFragment_to_dailyHistoryFragment)
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> findNavController(R.id.navHostfragDaily).navigate(R.id.dailyFragment)
                    1 -> {
                        // Se o fragment atual for o RecentMarsFragment mas a tab selecionada for a Historico (tab 1):
                        try { findNavController(R.id.navHostfragDaily).navigate(R.id.action_dailyFragment_to_dailyHistoryFragment) }
                        // Se o fragment atual for o HistoryMarsFragment:
                        catch (e:Exception) { findNavController(R.id.navHostfragDaily).navigate(R.id.dailyHistoryFragment) }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
        })

        // Alteração da cor dos icones dos tabs inferiores (selecionado ou não)
        val colors: ColorStateList = resources.getColorStateList(R.color.tabs_selector, theme)

        for (i in 0 until bottomTabs.tabCount) {
            val tab: TabLayout.Tab = bottomTabs.getTabAt(i)!!
            var icon = tab.icon
            if (icon != null) {
                icon = DrawableCompat.wrap(icon)
                DrawableCompat.setTintList(icon, colors)
            }
        }


    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    // Ao pressionar o botão de voltar:
    //      Fecha o drawer caso ele esteja aberto
    //      Ou vai para a InitialActivity caso estejamos na tab inicial
    //      Ou fecha o fragment "foco" e volta pro recycler
    //      Ou vai para a tab inicial, se ela já não for a ativa
    override fun onBackPressed() {
        if (dlDaily.isDrawerOpen(GravityCompat.END)) {
            dlDaily.closeDrawer(GravityCompat.END)
        }

        else {
            val navHostFrag = supportFragmentManager.findFragmentById(R.id.navHostfragDaily)
            val currFrag = navHostFrag?.findNavController()?.currentDestination?.id

            if (bottomTabs.selectedTabPosition == 0) {
                startActivity(Intent(this, InitialActivity::class.java))
            }

            if (bottomTabs.selectedTabPosition != 0 && currFrag != R.id.dailyHistoryFragment) {
                navController.navigateUp(appBarConfiguration)
            }

            if (bottomTabs.selectedTabPosition != 0 && currFrag == R.id.dailyHistoryFragment) {
                bottomTabs.getTabAt(0)?.select()
            }
        }
    }
}