package com.example.astrodream.ui.globe

import android.animation.Animator
import android.graphics.Bitmap
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.Request
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.example.astrodream.R
import com.example.astrodream.services.buildGlobeImageUrl
import com.example.astrodream.services.service
import com.example.astrodream.ui.ActivityWithTopBar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.android.synthetic.main.activity_globe.*
import kotlinx.android.synthetic.main.activity_globe.view.*
import kotlinx.android.synthetic.main.astrodialog.view.*
import kotlinx.android.synthetic.main.fragment_recent_mars.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.relex.circleindicator.CircleIndicator
import org.threeten.bp.LocalDate
import org.threeten.bp.temporal.ChronoField
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class GlobeActivity : ActivityWithTopBar(R.string.globo, R.id.dlGlobe) {

    private var maxDay = 0
    private lateinit var date: Date
    private lateinit var animation: AlphaAnimation
    private lateinit var globeAdapter: GlobeAdapter

    private val viewModel by viewModels<GlobeViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return GlobeViewModel(service) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_globe)

        //animação do pisque do botão de escolher data
        animation = AlphaAnimation(0.5f, 1f)
        animation.repeatMode = Animation.REVERSE
        animation.repeatCount = Animation.INFINITE
        animation.duration = 300

        //pega a data mais atual com imagens
        var day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH) - 2
        var month = Calendar.getInstance().get(Calendar.MONTH)
        var year = Calendar.getInstance().get(Calendar.YEAR)

        //pega a data atual e seta como limite
        maxDay = day + 2

        try {
            date = SimpleDateFormat(
                "dd/MM/yyyy",
                Locale.getDefault()
            ).parse("$day/${month + 1}/$year")!!
            tvData.text = SimpleDateFormat.getDateInstance().format(date).toString()
            viewModel.getAllEPIC(
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                    date
                ).toString()
            )
        } catch (ignored: Exception) {
        }

        val indicator = ciGlobe as CircleIndicator

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

                    date = SimpleDateFormat(
                        "dd/MM/yyyy",
                        Locale.getDefault()
                    ).parse("$day/${month + 1}/$year")!!

                    if (day >= maxDay || day == maxDay - 1) {
                        Toast.makeText(this, "Escolha uma data anterior", Toast.LENGTH_LONG)
                            .show()
                    } else if (date != SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).parse(
                            tvData.text.toString()
                        )
                    ) {
                        tvData.text = SimpleDateFormat.getDateInstance().format(date).toString()
                        viewModel.getAllEPIC(
                            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                                date
                            ).toString()
                        )
                    }
                }
                .setNegativeButton(resources.getString(R.string.cancelar), null)
                .show()
        }

        viewModel.imageArray.observe(this)
        {
            globeAdapter = GlobeAdapter(it, this, date)
            vpGlobe.adapter = globeAdapter
            indicator.setViewPager(vpGlobe)
        }
        setUpMenuBehavior()
    }

    override fun onResume() {
        if (getSharedPreferences("first_time", MODE_PRIVATE).getBoolean("globe", true)) {
            fabData.isClickable = false
            CoroutineScope(Dispatchers.Main).launch {
                delay(1000)
                clTutorialGlobe.animate()
                    .alpha(1.0f)
                    .setListener(object : Animator.AnimatorListener {
                        override fun onAnimationStart(p0: Animator?) {
                        }

                        override fun onAnimationEnd(p0: Animator?) {
                            val view = inflate(this@GlobeActivity, R.layout.astrodialog, null)
                            view.ivDialog.visibility = GONE
                            view.tvAppName.visibility = GONE
                            view.tvDialog1.text = getString(R.string.date_picker_instruction)
                            view.tvDialog2.visibility = GONE
                            view.tvDialog3.visibility = GONE
                            val dialog = MaterialAlertDialogBuilder(this@GlobeActivity)
                                .setBackground(
                                    ContextCompat.getColor(
                                        this@GlobeActivity,
                                        android.R.color.transparent
                                    ).toDrawable()
                                )
                                .setView(view)
                                .setOnDismissListener {
                                    this@GlobeActivity.clTutorialGlobe.animate()
                                        .alpha(0f)
                                        .setListener(null)
                                        .duration = 500
                                    it.cancel()
                                    fabData.clearAnimation()
                                    fabData.isClickable = true
                                    getSharedPreferences("first_time", MODE_PRIVATE).edit()
                                        .putBoolean("globe", false)
                                        .apply()
                                }.create()

                            dialog.window?.setDimAmount(0f)
                            dialog.show()
                        }

                        override fun onAnimationCancel(p0: Animator?) {}

                        override fun onAnimationRepeat(p0: Animator?) {}
                    }).duration = 500

                delay(500)
                fabData.startAnimation(animation)
            }
        }
        super.onResume()
    }
}
