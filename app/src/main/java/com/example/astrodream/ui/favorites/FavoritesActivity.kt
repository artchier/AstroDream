package com.example.astrodream.ui.favorites

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.example.astrodream.R
import com.example.astrodream.database.AppDatabase
import com.example.astrodream.services.*
import com.example.astrodream.ui.ActivityWithTopBar
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_favorites.*
import kotlinx.android.synthetic.main.activity_mars.bottomTabs

class FavoritesActivity : ActivityWithTopBar(R.string.favoritos, R.id.dlFavs) {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var db: AppDatabase
    private lateinit var repositoryDaily: ServiceDBDaily
    private lateinit var repositoryTech: ServiceDatabaseTech
    private lateinit var repositoryMars: ServiceDBMars
    private lateinit var repositoryAsteroid: ServiceDBAsteroids

    val viewModel by viewModels<FavViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return FavViewModel(repositoryDaily, repositoryTech, repositoryMars, repositoryAsteroid) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        setUpMenuBehavior()

        db = AppDatabase.invoke(this)
        repositoryDaily = ServiceDBImplementationDaily(db.dailyDAO())
        repositoryTech = ServiceDatabaseImplementationTech(db.techDAO())
        repositoryMars = ServiceDBImplementationMars(db.marsDAO())
        repositoryAsteroid = ServiceDBAsteroidsImpl(db.asteroidDAO())

        // Configuração do Navigation Component
        navController = findNavController(R.id.navHostfragFavs) // Container dos fragments
        appBarConfiguration = AppBarConfiguration(navController.graph) // Pega o graph do controller
        val navOptions = NavOptions.Builder()
            .setPopUpTo(navController.graph.startDestination, false)
            .build()

        // Navegação entre os tabs inferiores
        bottomTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> { viewModel.setFavType("daily") }
                    1 -> { viewModel.setFavType("asteroid") }
                    2 -> { viewModel.setFavType("tech") }
                    3 -> { viewModel.setFavType("mars") }
                }

                findNavController(R.id.navHostfragFavs).navigate(
                    R.id.favRecyclerFragment,
                    null,
                    navOptions
                )
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                reselectTab()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
        })

        // Configurações da tab inferior
        configBottomTabs()
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
        if (dlFavs.isDrawerOpen(GravityCompat.END)) {
            dlFavs.closeDrawer(GravityCompat.END)
            return
        }
        val navHostFrag = supportFragmentManager.findFragmentById(R.id.navHostfragFavs)
        val currFrag = navHostFrag?.findNavController()?.currentDestination?.id

        if (currFrag != R.id.favRecyclerFragment) {
            navController.navigateUp(appBarConfiguration)
            return
        }
        if (bottomTabs.selectedTabPosition != 0) {
            bottomTabs.getTabAt(0)?.select()
            return
        }
        finish()
    }

    fun reselectTab() {
        val navHostFrag = supportFragmentManager.findFragmentById(R.id.navHostfragFavs)
        val currFrag = navHostFrag?.findNavController()?.currentDestination?.id

        if (currFrag != R.id.favRecyclerFragment) {
            navController.navigateUp(appBarConfiguration)
        }
    }

    private fun configBottomTabs() {
        // TODO: aumentar o tamanho dos icones
        val tabIcons = arrayListOf(
            R.drawable.ic_background,
            R.drawable.ic_asteroide,
            R.drawable.ic_tecnologia,
            R.drawable.ic_marte
        )
        val colors: ColorStateList = resources.getColorStateList(R.color.tabs_selector, theme)

        for (i in 0 until tabIcons.size) {
            bottomTabs.addTab(bottomTabs.newTab())
            val tab: TabLayout.Tab = bottomTabs.getTabAt(i)!!
            tab.setIcon(tabIcons[i])
            var icon = tab.icon
            if (icon != null) {
                icon = DrawableCompat.wrap(icon)
                DrawableCompat.setTintList(icon, colors)
            }
        }
    }

}