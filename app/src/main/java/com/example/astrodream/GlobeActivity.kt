package com.example.astrodream

import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.Menu
import android.view.MenuItem
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.GravityCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_globe.*
import kotlinx.android.synthetic.main.lateral_menu.*
import java.util.*

class GlobeActivity : ActivityWithTopBar(R.id.tbGlobe, R.id.dlGlobe) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_globe)

        //Pega a imagem da API e mostra na ImageView
        // TODO pegar essa imagem deve ser responsabilidade de algum service
        Glide.with(this).asBitmap()
            .load("https://api.nasa.gov/EPIC/archive/natural/2019/05/30/png/epic_1b_20190530011359.png?api_key=DEMO_KEY")
            .transform(RoundedCorners(50))
            .into(ivGlobe)

        //pega a data atual e mostra no TextView
        var day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        var month = Calendar.getInstance().get(Calendar.MONTH)
        var year = Calendar.getInstance().get(Calendar.YEAR)
        var data = "$day/${month + 1}/$year"
        tvData.text = data

        //clique do botão "Escolher Data"
        fabData.setOnClickListener {
            val datePicker = DatePicker((ContextThemeWrapper(this, R.style.DatePicker)), null)
            datePicker.updateDate(year, month, day)
            MaterialAlertDialogBuilder(this)
                .setView(datePicker)
                .setPositiveButton(resources.getString(R.string.ok)) { _, _ ->
                    day = datePicker.dayOfMonth
                    month = datePicker.month
                    year = datePicker.year
                    data = "$day/${month + 1}/$year"
                    tvData.text = data
                }
                .setNegativeButton(resources.getString(R.string.cancelar), null)
                .show()
        }

        setUpMenuBehavior()
    }
}