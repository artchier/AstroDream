package com.example.astrodream

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_mars.*
import kotlinx.android.synthetic.main.activity_mars.bottomTabs
import kotlinx.android.synthetic.main.lateral_menu.*

class MarsActivity : AppCompatActivity() {

    private lateinit var navController : NavController
    private lateinit var appBarConfiguration : AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mars)

        // Remove o titulo da toolbar e carrega a toolbar personalizada
        // O nome está definido no TextView para permitir a fonte Homespun
        tbMars.title = ""
        setSupportActionBar(tbMars)

        //clique do botão "Sobre"
        btnSobre.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setBackgroundInsetStart(70)
                .setBackgroundInsetEnd(70)
                .setBackgroundInsetTop(10)
                .setBackgroundInsetBottom(100)
                .setBackground(
                    ContextCompat.getColor(this, android.R.color.transparent).toDrawable()
                )
                .setView(R.layout.astrodialog)
                .show()
        }

        // Clique do botão "Favoritos"
        btnFavoritos.setOnClickListener {
            startActivity(Intent(this, FavoritesActivity::class.java))
        }

        // Configuração do Navigation Component
        // Nesta activity estão sendo usados 2 fragments apenas:
        //      RecentMarsFragment: mostra as imagens da data mais recente disponivel.
        //          Esse fragment está sendo aproveitado para carregar as imagens de uma data qualquer selecionada do Historico
        //      HistoryMarsFragment: mostra todas as datas com imagens disponiveis
        navController = findNavController(R.id.navHostfragMars) // Container dos fragments
        appBarConfiguration = AppBarConfiguration(navController.graph) // Pega o graph do controller

        // Nevegação entre os tabs inferiores
        bottomTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        // Se o fragment atual for o HistoryMarsFragment:
                        try { findNavController(R.id.navHostfragMars).navigate(R.id.action_historyMarsFragment_to_recentMarsFragment) }
                        // Se o fragment atual for o RecentMarsFragment mas a tab selecionada for a Historico (tab 1):
                        catch (e:Exception) { findNavController(R.id.navHostfragMars).navigate(R.id.recentMarsFragment) }
                    }
                    1 -> findNavController(R.id.navHostfragMars).navigate(R.id.action_recentMarsFragment_to_historyMarsFragment)
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> findNavController(R.id.navHostfragMars).navigate(R.id.recentMarsFragment)
                    1 -> {
                        // Se o fragment atual for o RecentMarsFragment mas a tab selecionada for a Historico (tab 1):
                        try { findNavController(R.id.navHostfragMars).navigate(R.id.action_recentMarsFragment_to_historyMarsFragment) }
                        // Se o fragment atual for o HistoryMarsFragment:
                        catch (e:Exception) { findNavController(R.id.navHostfragMars).navigate(R.id.historyMarsFragment) }
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

    // Infla o menu lateral
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // Abre o drawer
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_lateral) {
            dlMars.openDrawer(GravityCompat.END)
        }
        return super.onOptionsItemSelected(item)
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
        if (dlMars.isDrawerOpen(GravityCompat.END)) {
            dlMars.closeDrawer(GravityCompat.END)
        }

        val navHostFrag = supportFragmentManager.findFragmentById(R.id.navHostfragMars)
        val currFrag = navHostFrag?.findNavController()?.currentDestination?.id

        if (bottomTabs.selectedTabPosition == 0) {
            startActivity(Intent(this, InitialActivity::class.java))
        }

        if (bottomTabs.selectedTabPosition != 0 && currFrag != R.id.historyMarsFragment) {
            navController.navigateUp(appBarConfiguration)
        }

        if (bottomTabs.selectedTabPosition != 0 && currFrag == R.id.historyMarsFragment) {
            bottomTabs.getTabAt(0)?.select()
        }
    }
}