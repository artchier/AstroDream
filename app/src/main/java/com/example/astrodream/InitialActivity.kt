package com.example.astrodream

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class InitialActivity : ActivityWithTopBar(R.string.app_name, R.id.dlInitial) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial)
        setUpMenuBehavior()

        tbInitial.title = ""
        setSupportActionBar(tbInitial)

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

        btAsteriodes.setOnClickListener {
            startActivity(Intent(this, AsteroidActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            menu_lateral -> {
                dlInitial.openDrawer(GravityCompat.END)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (dlInitial.isDrawerOpen(GravityCompat.END))
            dlInitial.closeDrawer(GravityCompat.END)
        else
            finish()
    }
}