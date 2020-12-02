package com.example.astrodream.ui

import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.astrodream.*
import com.example.astrodream.ui.asteroids.AsteroidActivity
import com.example.astrodream.ui.avatar.AvatarActivity
import com.example.astrodream.ui.dailyimage.DailyImageActivity
import com.example.astrodream.ui.favorites.FavoritesActivity
import com.example.astrodream.ui.globe.GlobeActivity
import com.example.astrodream.ui.initial.InitialActivity
import com.example.astrodream.ui.mars.MarsActivity
import com.example.astrodream.ui.tech.TechActivity
import com.example.astrodream.ui.userconfig.UserConfigActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.app_tool_bar.*

abstract class ActivityWithTopBar(
    private val toolbarTiteTitleId: Int,
    private val drawerLayoutId: Int
) : AppCompatActivity() {

    private var toolBar: MaterialToolbar? = null
    private lateinit var drawerLayout: DrawerLayout

    private fun <T> goToActivityIfNotAlreadyThere(destination: Class<T>) {
        if (this::class.java == destination) {
            drawerLayout.closeDrawer(GravityCompat.END)
            return
        }
        startActivity(Intent(this, destination))
        if (this !is InitialActivity) {
            finish()
        }
    }

    fun setUpMenuBehavior() {
        toolBar = if (toolBar == null) appToolBar else toolBar
        drawerLayout = findViewById(drawerLayoutId)

        toolBar?.apply {
            title = ""
            tvToolBarTitle.text = resources.getString(toolbarTiteTitleId)
        }
        setSupportActionBar(toolBar)

        val lateralMenuHost = findViewById<NavigationView>(R.id.nvLateralMenu)
        val lateralMenu = findViewById<ConstraintLayout>(R.id.clLateralMenu)

        val btnAvatar = lateralMenuHost.findViewById<AppCompatButton>(R.id.btnAvatar)

        val llBackground = lateralMenu.findViewById<LinearLayout>(R.id.llBackground)
        val llAsteroides = lateralMenu.findViewById<LinearLayout>(R.id.llAsteroides)
        val llGlobo = lateralMenu.findViewById<LinearLayout>(R.id.llGlobo)
        val llTecnologias = lateralMenu.findViewById<LinearLayout>(R.id.llTecnologias)
        val llMarte = lateralMenu.findViewById<LinearLayout>(R.id.llMarte)

        val btnFavoritos = lateralMenuHost.findViewById<AppCompatButton>(R.id.btnFavoritos)
        val btnConfig = lateralMenuHost.findViewById<AppCompatButton>(R.id.btnConfig)
        val btnSobre = lateralMenuHost.findViewById<AppCompatButton>(R.id.btnSobre)

        btnAvatar.setOnClickListener {
            goToActivityIfNotAlreadyThere(AvatarActivity::class.java)
        }

        llBackground.setOnClickListener {
            goToActivityIfNotAlreadyThere(DailyImageActivity::class.java)
        }

        llAsteroides.setOnClickListener {
            goToActivityIfNotAlreadyThere(AsteroidActivity::class.java)
        }

        llGlobo.setOnClickListener {
            goToActivityIfNotAlreadyThere(GlobeActivity::class.java)
        }

        llTecnologias.setOnClickListener {
            goToActivityIfNotAlreadyThere(TechActivity::class.java)
        }

        llMarte.setOnClickListener {
            goToActivityIfNotAlreadyThere(MarsActivity::class.java)
        }

        btnFavoritos.setOnClickListener {
            goToActivityIfNotAlreadyThere(FavoritesActivity::class.java)
        }

        btnConfig.setOnClickListener {
            goToActivityIfNotAlreadyThere(UserConfigActivity::class.java)
        }

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
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_lateral) {
            drawerLayout.openDrawer(GravityCompat.END)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END)
            return
        }
        super.onBackPressed()
    }
}