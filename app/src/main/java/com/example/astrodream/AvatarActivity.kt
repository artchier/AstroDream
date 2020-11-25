package com.example.astrodream

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.GravityCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_avatar.*
import kotlinx.android.synthetic.main.activity_globe.*
import kotlinx.android.synthetic.main.buy_avatar_dialog.*
import kotlinx.android.synthetic.main.lateral_menu.*

class AvatarActivity : AppCompatActivity() {
    lateinit var listAvatar: List<Int>
    lateinit var adapter: AvatarAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_avatar)

        //seta a Toolbar
        tbAvatar.title = ""
        setSupportActionBar(tbAvatar)

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

        listAvatar = listOf(
            R.drawable.ic_avatar_alien,
            R.drawable.ic_avatar_astronaut,
            R.drawable.ic_avatar_naked,
            R.drawable.ic_avatar_normal1,
            R.drawable.ic_avatar_normal2,
            R.drawable.ic_avatar_normal3,
            R.drawable.ic_avatar_normal4,
            R.drawable.ic_avatar_normal5,
            R.drawable.ic_avatar_nuts,
            R.drawable.ic_avatar_suit
        )

        adapter = AvatarAdapter(this, listAvatar)

        rvAvatar.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_lateral -> {
                dlAvatar.openDrawer(GravityCompat.END)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (dlAvatar.isDrawerOpen(GravityCompat.END))
            dlAvatar.closeDrawer(GravityCompat.END)
        else
            startActivity(Intent(this, InitialActivity::class.java))
            finish()
    }
}