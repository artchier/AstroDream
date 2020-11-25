package com.example.astrodream

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_favorites.*
import kotlinx.android.synthetic.main.activity_mars.bottomTabs

class FavoritesActivity : ActivityWithTopBar(R.string.favoritos, R.id.dlFavs) {

    private lateinit var navController : NavController
    private lateinit var appBarConfiguration : AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        // Configuração do Navigation Component
        // Nesta activity estão sendo usados 6 fragments:
        // 1x Fragment de RecyclerView listando os favoritos
        // 5x Fragments para mostrar o item escolhido dentre os favoritos:
        //      FavTodayFragment: fragment mostrando a imagem do dia escolhido na lista de favoritos
        //      FavAsteroidsFragment: fragment mostrando o asteroide escolhido na lista de favoritos
        //      FavGlobeFragment: fragment mostrando o globo do dia escolhido na lista de favoritos
        //      FavTechFragment: fragment mostrando a tecnologia escolhido na lista de favoritos
        //      RecentMarsFragment: fragment mostrando o post do dia escolhido na lista de favoritos
        //          (aproveitado um fragment existente)
        navController = findNavController(R.id.navHostfragFavs) // Container dos fragments
        appBarConfiguration = AppBarConfiguration(navController.graph) // Pega o graph do controller
        val navOptions = NavOptions.Builder()
            .setPopUpTo(navController.graph.startDestination, false)
            .build()

        // Navegação entre os tabs inferiores
        bottomTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        val bundleRest: Bundle = Bundle().apply {
                            putString("type", "today")
                        }
                        // Reload a FavRecyclerFragment
                        findNavController(R.id.navHostfragFavs).navigate(
                            R.id.favRecyclerFragment,
                            bundleRest,
                            navOptions
                        )
                    }
                    1 -> {
                        val bundleRest: Bundle = Bundle().apply {
                            putString("type", "asteroid")
                        }
                        // Reload a FavRecyclerFragment
                        findNavController(R.id.navHostfragFavs).navigate(
                            R.id.favRecyclerFragment,
                            bundleRest,
                            navOptions
                        )
                    }
                    2 -> {
                        val bundleRest: Bundle = Bundle().apply {
                            putString("type", "globe")
                        }
                        // Reload a FavRecyclerFragment
                        findNavController(R.id.navHostfragFavs).navigate(
                            R.id.favRecyclerFragment,
                            bundleRest,
                            navOptions
                        )
                    }
                    3 -> {
                        val bundleRest: Bundle = Bundle().apply {
                            putString("type", "tech")
                        }
                        // Reload a FavRecyclerFragment
                        findNavController(R.id.navHostfragFavs).navigate(
                            R.id.favRecyclerFragment,
                            bundleRest,
                            navOptions
                        )
                    }
                    4 -> {
                        val bundleRest: Bundle = Bundle().apply {
                            putString("type", "mars")
                        }
                        // Reload a FavRecyclerFragment
                        findNavController(R.id.navHostfragFavs).navigate(
                            R.id.favRecyclerFragment,
                            bundleRest,
                            navOptions
                        )
                    }
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        val bundleRest: Bundle = Bundle().apply {
                            putString("type", "today")
                        }
                        // Reload a FavRecyclerFragment
                        findNavController(R.id.navHostfragFavs).navigate(
                            R.id.favRecyclerFragment,
                            bundleRest,
                            navOptions
                        )
                    }
                    1 -> {
                        val bundleRest: Bundle = Bundle().apply {
                            putString("type", "asteroid")
                        }
                        // Reload a FavRecyclerFragment
                        findNavController(R.id.navHostfragFavs).navigate(
                            R.id.favRecyclerFragment,
                            bundleRest,
                            navOptions
                        )
                    }
                    2 -> {
                        val bundleRest: Bundle = Bundle().apply {
                            putString("type", "globe")
                        }
                        // Reload a FavRecyclerFragment
                        findNavController(R.id.navHostfragFavs).navigate(
                            R.id.favRecyclerFragment,
                            bundleRest,
                            navOptions
                        )
                    }
                    3 -> {
                        val bundleRest: Bundle = Bundle().apply {
                            putString("type", "tech")
                        }
                        // Reload a FavRecyclerFragment
                        findNavController(R.id.navHostfragFavs).navigate(
                            R.id.favRecyclerFragment,
                            bundleRest,
                            navOptions
                        )
                    }
                    4 -> {
                        val bundleRest: Bundle = Bundle().apply {
                            putString("type", "mars")
                        }
                        // Reload a FavRecyclerFragment
                        findNavController(R.id.navHostfragFavs).navigate(
                            R.id.favRecyclerFragment,
                            bundleRest,
                            navOptions
                        )
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
        })

        // Configurações da tab inferior
        // Cria as tabs, inclui os icones
        // Alteração da cor dos icones dos tabs inferiores (selecionado ou não)
        // TODO: aumento do tamanho dos icones
        val tabIcons = arrayListOf(
            R.drawable.ic_background,
            R.drawable.ic_asteroide,
            R.drawable.ic_globo,
            R.drawable.ic_tecnologia,
            R.drawable.ic_marte
        )
        val colors: ColorStateList = resources.getColorStateList(R.color.tabs_selector, theme)

        for (i in 0 until tabIcons.size) {
            Log.i("XXX", tabIcons.toString())
            bottomTabs.addTab(bottomTabs.newTab())
            val tab: TabLayout.Tab = bottomTabs.getTabAt(i)!!
            tab.setIcon(tabIcons[i])
            var icon = tab.icon
            if (icon != null) {
                icon = DrawableCompat.wrap(icon)
                DrawableCompat.setTintList(icon, colors)
            }
        }

        setUpMenuBehavior()
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
        }

        else {
            val navHostFrag = supportFragmentManager.findFragmentById(R.id.navHostfragFavs)
            val currFrag = navHostFrag?.findNavController()?.currentDestination?.id

            if (bottomTabs.selectedTabPosition == 0 && currFrag == R.id.favRecyclerFragment) {
                startActivity(Intent(this, InitialActivity::class.java))
            }

            if (currFrag != R.id.favRecyclerFragment) {
                navController.navigateUp(appBarConfiguration)
            }

            if (bottomTabs.selectedTabPosition != 0 && currFrag == R.id.favRecyclerFragment) {
                bottomTabs.getTabAt(0)?.select()
            }
        }
    }
}