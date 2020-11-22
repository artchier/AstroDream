package com.example.astrodream

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.DrawableCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_mars.*

class MarsActivity : AppCompatActivity() {

    private lateinit var navController : NavController
    private lateinit var appBarConfiguration : AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mars)

        tbMars.title = ""
        setSupportActionBar(tbMars)


        navController = findNavController(R.id.navHostfragMars) // Container dos fragments
        appBarConfiguration = AppBarConfiguration(navController.graph) // Pega o graph do controller

        bottomTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        try {
                            findNavController(R.id.navHostfragMars).navigate(R.id.action_historyMarsFragment_to_recentMarsFragment)
                        } catch (e:Exception) {
                            findNavController(R.id.navHostfragMars).navigate(R.id.reload_recentMarsFragment)
                        }
                    }
                    1 -> findNavController(R.id.navHostfragMars).navigate(R.id.action_recentMarsFragment_to_historyMarsFragment)
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> findNavController(R.id.navHostfragMars).navigate(R.id.reload_recentMarsFragment)
                    1 -> {
                        try {
                            findNavController(R.id.navHostfragMars).navigate(R.id.action_recentMarsFragment_to_historyMarsFragment)
                        } catch (e:Exception) {
                            findNavController(R.id.navHostfragMars).navigate(R.id.reload_historyMarsFragment)
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Handle tab unselect
            }
        })

        val colors: ColorStateList
        colors = if (Build.VERSION.SDK_INT >= 23) {
            resources.getColorStateList(R.color.tabs_selector, theme)
        } else {
            resources.getColorStateList(R.color.tabs_selector)
        }

        for (i in 0 until bottomTabs.getTabCount()) {
            val tab: TabLayout.Tab = bottomTabs.getTabAt(i)!!
            var icon = tab.icon
            if (icon != null) {
                icon = DrawableCompat.wrap(icon)
                DrawableCompat.setTintList(icon, colors)
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, InitialActivity::class.java))
    }
}