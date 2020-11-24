package com.example.astrodream

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.format.DateFormat
import android.view.ContextThemeWrapper
import android.view.Menu
import android.view.MenuItem
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.GravityCompat
import androidx.core.view.get
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_globe.*
import kotlinx.android.synthetic.main.activity_globe.view.*
import kotlinx.android.synthetic.main.datepicker.*
import kotlinx.android.synthetic.main.lateral_menu.*
import java.time.DayOfWeek
import java.util.*
import kotlin.time.days

class GlobeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_globe)

        //Pega a imagem da API e mostra na ImageView
        Glide.with(this).asBitmap()
            .load("https://api.nasa.gov/EPIC/archive/natural/2019/05/30/png/epic_1b_20190530011359.png?api_key=DEMO_KEY")
            .transform(RoundedCorners(50))
            .into(ivGlobe)

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

        //pega a data atual e mostra no TextView
        var day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        var month = Calendar.getInstance().get(Calendar.MONTH)
        var year = Calendar.getInstance().get(Calendar.YEAR)
        var data = "$day/${month+1}/$year"
        tvData.text = data

        //clique do botão "Escolher Data"
        fabData.setOnClickListener {
            val datePicker = DatePicker((ContextThemeWrapper(this, R.style.DatePicker)), null)
            datePicker.updateDate(year, month, day)
            MaterialAlertDialogBuilder(this)
                .setView(datePicker)
                .setPositiveButton("Ok", DialogInterface.OnClickListener { _, _ ->
                    day = datePicker.dayOfMonth
                    month = datePicker.month
                    year = datePicker.year
                    data = "$day/${month+1}/$year"
                    tvData.text = data
                })
                .setNegativeButton("Cancelar", null)
                .show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_lateral -> {
                dlGlobe.openDrawer(GravityCompat.END)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (dlGlobe.isDrawerOpen(GravityCompat.END))
            dlGlobe.closeDrawer(GravityCompat.END)
        else
            finish()
    }
}