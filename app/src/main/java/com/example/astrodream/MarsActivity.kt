package com.example.astrodream

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
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
                        // Checar se a tab atual é a Recente ou a Historico
                        // Caso seja a Recente, ela abre a RecentMarsFragment novamente
                        // Caso seja a Historico, ela faz o navigation para a Recente
                        if (tab.position == 0) findNavController(R.id.navHostfragMars).navigate(R.id.reload_recentMarsFragment)
                        else findNavController(R.id.navHostfragMars).navigate(R.id.action_historyMarsFragment_to_recentMarsFragment)
                    }
                    1 -> findNavController(R.id.navHostfragMars).navigate(R.id.action_recentMarsFragment_to_historyMarsFragment)
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Handle tab reselect
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Handle tab unselect
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}