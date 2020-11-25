package com.example.astrodream

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_globe.*
import kotlinx.android.synthetic.main.lateral_menu.*

class AvatarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_avatar)

        //seta a Toolbar
        tbGlobe.title = ""
        setSupportActionBar(tbGlobe)

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
    }
}