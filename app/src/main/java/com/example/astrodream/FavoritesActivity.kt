package com.example.astrodream

import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.example.astrodream.favorites.FavRecyclerFragment
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_favorites.*
import kotlinx.android.synthetic.main.activity_mars.*
import kotlinx.android.synthetic.main.activity_mars.bottomTabs

class FavoritesActivity : AppCompatActivity() {

    private lateinit var navController : NavController
    private lateinit var appBarConfiguration : AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        // Remove o titulo da toolbar e carrega a toolbar personalizada
        // O nome está definido no TextView para permitir a fonte Homespun
        tbFavs.title = ""
        setSupportActionBar(tbFavs)

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
            .setLaunchSingleTop(true)
            .setPopUpTo(navController.getGraph().getStartDestination(), false)
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

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Handle tab unselect
            }
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
        val colors: ColorStateList
        colors = resources.getColorStateList(R.color.tabs_selector, theme)

        for (i in 0 until tabIcons.size) {
            val view: View = layoutInflater.inflate(R.layout.tabs,null)
            Log.i("XXX", tabIcons.toString())
            //view.findViewById(R.id.icon).setBackgroundResource(tabIcons[i])
            bottomTabs.addTab(bottomTabs.newTab())
            val tab: TabLayout.Tab = bottomTabs.getTabAt(i)!!
            //tab.setCustomView(R.layout.tabs)
            tab.setIcon(tabIcons[i])
            var icon = tab.icon
            if (tab != null && icon != null) {
                icon = DrawableCompat.wrap(icon)
                DrawableCompat.setTintList(icon, colors)
            }

        }

    }

    // Infla o menu lateral
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // Abre o drawer
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_lateral -> {
                dlFavs.openDrawer(GravityCompat.END)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    // Vai para a InitialActivity ao pressionar o botão de voltar
    // ou fechar o drawer caso ele esteja aberto
    override fun onBackPressed() {
        if (dlFavs.isDrawerOpen(GravityCompat.END))
            dlFavs.closeDrawer(GravityCompat.END)
        else {
            if (bottomTabs.selectedTabPosition == 0) startActivity(Intent(this, InitialActivity::class.java))
            else bottomTabs.getTabAt(0)?.select()
        }
    }
}