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
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
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
        // Nesta activity estão sendo usados 10 fragments:
        // Fragments de RecyclerView listando os favoritos:
        //      FavRecyclerTodayFragment: RecyclerView das imagens do dia favoritadas
        //      FavRecyclerAsteroidsFragment: RecyclerView dos asteroides favoritados
        //      FavRecyclerGlobeFragment: RecyclerView das animações de globo favoritadas
        //      FavRecyclerTechFragment: RecyclerView das tecnologias favoritadas
        //      FavRecyclerMarsFragment: RecyclerView dos posts de Marte favoritados
        // Fragments para mostrar o item escolhido dentre os favoritos:
        //      FavTodayFragment: fragment mostrando a imagem do dia escolhido na lista de favoritos
        //      FavAsteroidsFragment: fragment mostrando o asteroide escolhido na lista de favoritos
        //      FavGlobeFragment: fragment mostrando o globo do dia escolhido na lista de favoritos
        //      FavTechFragment: fragment mostrando a tecnologia escolhido na lista de favoritos
        //      RecentMarsFragment: fragment mostrando o post do dia escolhido na lista de favoritos
        //          (aproveitado um fragment existente)
        navController = findNavController(R.id.navHostfragFavs) // Container dos fragments
        appBarConfiguration = AppBarConfiguration(navController.graph) // Pega o graph do controller

        // Navegação entre os tabs inferiores
        bottomTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
//                when (tab?.position) {
//                    0 -> {
//                        // Se o fragment atual for o HistoryMarsFragment:
//                        try { findNavController(R.id.navHostfragMars).navigate(R.id.action_historyMarsFragment_to_recentMarsFragment) }
//                        // Se o fragment atual for o RecentMarsFragment mas a tab selecionada for a Historico (tab 1):
//                        catch (e:Exception) { findNavController(R.id.navHostfragMars).navigate(R.id.reload_recentMarsFragment) }
//                    }
//                    1 -> findNavController(R.id.navHostfragMars).navigate(R.id.action_recentMarsFragment_to_historyMarsFragment)
//                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
//                when (tab?.position) {
//                    0 -> findNavController(R.id.navHostfragMars).navigate(R.id.reload_recentMarsFragment)
//                    1 -> {
//                        // Se o fragment atual for o RecentMarsFragment mas a tab selecionada for a Historico (tab 1):
//                        try { findNavController(R.id.navHostfragMars).navigate(R.id.action_recentMarsFragment_to_historyMarsFragment) }
//                        // Se o fragment atual for o HistoryMarsFragment:
//                        catch (e:Exception) { findNavController(R.id.navHostfragMars).navigate(R.id.reload_historyMarsFragment) }
//                    }
//                }
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
        else
            startActivity(Intent(this, InitialActivity::class.java))
    }
}