package com.example.astrodream

import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
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

    fun setUpMenuBehavior() {
        // cada botão do menu lateral (lateral_menu.xml) deve ser obtido dessa forma
        val lateralMenu = findViewById<NavigationView>(R.id.nvLateralMenu)
        val btnAvatar = lateralMenu.findViewById<AppCompatButton>(R.id.btnAvatar)
        val btnSobre = lateralMenu.findViewById<AppCompatButton>(R.id.btnSobre)

        toolBar = if (toolBar == null) appToolBar else toolBar
        drawerLayout = findViewById(drawerLayoutId)

        toolBar?.apply {
            title = ""
            tvToolBarTitle.text = resources.getString(toolbarTiteTitleId)
        }
        setSupportActionBar(toolBar)

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

        btnAvatar.setOnClickListener {
            if (this is AvatarActivity) {
                drawerLayout.closeDrawer(GravityCompat.END)
            }
            else {
                startActivity(Intent(this, AvatarActivity::class.java))
            }
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
        }
        else {
            super.onBackPressed()
        }
    }
}