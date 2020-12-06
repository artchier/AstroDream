//package com.example.astrodream.ui.dailymarsfav
//
//import android.content.res.ColorStateList
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import androidx.core.graphics.drawable.DrawableCompat
//import androidx.navigation.NavController
//import androidx.navigation.findNavController
//import androidx.navigation.ui.AppBarConfiguration
//import com.example.astrodream.R
//import com.example.astrodream.ui.ActivityWithTopBar
//import com.google.android.material.tabs.TabLayout
//import kotlinx.android.synthetic.main.activity_mars.*
//
//class PlainActivity(toolbarTitleString: Int, drawerLayoutId: Int, private val navHostId: Int) : ActivityWithTopBar(toolbarTitleString, drawerLayoutId) {
//
//    private lateinit var navController : NavController
//    private lateinit var appBarConfiguration : AppBarConfiguration
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_plain)
//
//        setUpMenuBehavior()
//
//        navController = findNavController(navHostId)
//        appBarConfiguration = AppBarConfiguration(navController.graph)
//
//        // Nevegação entre os tabs inferiores
//        bottomTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//            override fun onTabSelected(tab: TabLayout.Tab?) {
//                when (tab?.position) {
//                    0 -> {
//                        // Se o fragment atual for o HistoryMarsFragment:
//                        try { navController.navigate() findNavController(R.id.navHostfragDaily).navigate(R.id.action_dailyHistoryFragment_to_dailyFragment) }
//                        // Se o fragment atual for o RecentMarsFragment mas a tab selecionada for a Historico (tab 1):
//                        catch (e:Exception) { findNavController(R.id.navHostfragDaily).navigate(R.id.dailyFragment) }
//                    }
//                    1 -> findNavController(R.id.navHostfragDaily).navigate(R.id.action_dailyFragment_to_dailyHistoryFragment)
//                }
//            }
//
//            override fun onTabReselected(tab: TabLayout.Tab?) {
//                when (tab?.position) {
//                    0 -> findNavController(R.id.navHostfragDaily).navigate(R.id.dailyFragment)
//                    1 -> {
//                        // Se o fragment atual for o RecentMarsFragment mas a tab selecionada for a Historico (tab 1):
//                        try { findNavController(R.id.navHostfragDaily).navigate(R.id.action_dailyFragment_to_dailyHistoryFragment) }
//                        // Se o fragment atual for o HistoryMarsFragment:
//                        catch (e:Exception) { findNavController(R.id.navHostfragDaily).navigate(R.id.dailyHistoryFragment) }
//                    }
//                }
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab?) {}
//        })
//
//        // Alteração da cor dos icones dos tabs inferiores (selecionado ou não)
//        val colors: ColorStateList = resources.getColorStateList(R.color.tabs_selector, theme)
//
//        for (i in 0 until bottomTabs.tabCount) {
//            val tab: TabLayout.Tab = bottomTabs.getTabAt(i)!!
//            var icon = tab.icon
//            if (icon != null) {
//                icon = DrawableCompat.wrap(icon)
//                DrawableCompat.setTintList(icon, colors)
//            }
//        }
//    }
//}