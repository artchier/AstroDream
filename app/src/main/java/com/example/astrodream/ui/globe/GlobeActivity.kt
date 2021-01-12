package com.example.astrodream.ui.globe

import android.animation.Animator
import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.ContextThemeWrapper
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
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.astrodream.R
import com.example.astrodream.services.buildGlobeImageUrl
import com.example.astrodream.services.service
import com.example.astrodream.ui.ActivityWithTopBar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_globe.*
import kotlinx.android.synthetic.main.astrodialog.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.util.*

class GlobeActivity : ActivityWithTopBar(R.string.globo, R.id.dlGlobe) {

    private var indexGlobe = 0
    private lateinit var date: Date
    private lateinit var animation: AlphaAnimation
    private lateinit var animation2: AlphaAnimation

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

        btPreviousGlobe.alpha = 0f
        btNextGlobe.alpha = 0f

        //animação do pisque do botão de escolher data
        animation = AlphaAnimation(0.5f, 1f)
        animation.repeatMode = Animation.REVERSE
        animation.repeatCount = Animation.INFINITE
        animation.duration = 300

        animation2 = AlphaAnimation(0f, 1f)
        animation2.duration = 400
        animation2.repeatCount = 4
        animation2.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
                btPreviousGlobe.alpha = 1f
                btNextGlobe.alpha = 1f
                btPreviousGlobe.isClickable = false
                btNextGlobe.isClickable = false
            }

            override fun onAnimationEnd(p0: Animation?) {
                btPreviousGlobe.alpha = 0f
                btNextGlobe.alpha = 0f
                btPreviousGlobe.isClickable = true
                btNextGlobe.isClickable = true
            }

            override fun onAnimationRepeat(p0: Animation?) {}
        })

        //pega a data atual e mostra no TextView
        var day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        var month = Calendar.getInstance().get(Calendar.MONTH)
        var year = Calendar.getInstance().get(Calendar.YEAR)

        //clique do botão "Escolher Data"
        fabData.setOnClickListener {
            val datePicker = DatePicker((ContextThemeWrapper(this, R.style.DatePicker)), null)

            when (datePicker.dayOfMonth) {
                day -> datePicker.updateDate(year, month, day - 1)
                else -> datePicker.updateDate(year, month, day)

            }
            val now = Calendar.getInstance()
            now.add(Calendar.DAY_OF_MONTH, -1)
            datePicker.maxDate = now.timeInMillis

            MaterialAlertDialogBuilder(this)
                .setView(datePicker)
                .setPositiveButton(resources.getString(R.string.ok)) { _, _ ->
                    ivGlobe.setImageBitmap(null)

                    day = datePicker.dayOfMonth
                    month = datePicker.month
                    year = datePicker.year

                    date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse("$day/${month + 1}/$year")!!
                    tvData.text = SimpleDateFormat.getDateInstance().format(date).toString()
                    indexGlobe = 0
                    viewModel.getAllEPIC(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date).toString())
                    pbGlobe.visibility = VISIBLE
                }
                .setNegativeButton(resources.getString(R.string.cancelar), null)
                .show()
        }

        viewModel.imageArray.observe(this) {
            //Pega a imagem da API
            // TODO pegar essa imagem deve ser responsabilidade de algum service

            //Tratamento primeira imagem
            Glide.with(this).asBitmap()
                .load(
                    "https://api.nasa.gov/EPIC/archive/natural/${
                        SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(
                            date
                        )
                    }/png/${it[0]}.png?api_key=$apikeyApp"
                )
                .transform(RoundedCorners(50))
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        viewModel.saveEPIC(resource)
                        pbGlobe.visibility = GONE
                        ivGlobe.setImageBitmap(resource)
                        if (getSharedPreferences("first_time", MODE_PRIVATE).getBoolean(
                                "globe",
                                true
                            )
                        ) {
                            CoroutineScope(Dispatchers.Main).launch {
                                delay(1000)
                                btPreviousGlobe.startAnimation(animation2)
                                btNextGlobe.startAnimation(animation2)
                            }
                        }
                        Toast.makeText(
                            this@GlobeActivity,
                            "indexGlobe1: $indexGlobe",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {

                    }

                })

            //tratamento das outras imagens
            it.subList(1, it.size).forEach { it1 ->
                Glide.with(this).asBitmap()
                    .load(buildGlobeImageUrl(date, it1))
                    .transform(RoundedCorners(50))
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap>?
                        ) {
                            viewModel.saveEPIC(resource)
                            Toast.makeText(
                                this@GlobeActivity,
                                "indexGlobe1: $indexGlobe",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {}

                        override fun onLoadCleared(placeholder: Drawable?) {}
                    })
            }
        }

        btNextGlobe.setOnClickListener {
            try {
                if (viewModel.epicImage.value!!.size < viewModel.imageArray.value!!.size) {
                    ivGlobe.setImageBitmap(null)
                    pbGlobe.visibility = VISIBLE
                }
                if (indexGlobe + 1 < viewModel.imageArray.value!!.size - 1) {
                    indexGlobe++
                    if (pbGlobe.visibility == VISIBLE) {
                        CoroutineScope(Dispatchers.Main).launch {
                            delay(1000)
                            pbGlobe.visibility = GONE
                            Glide.with(this@GlobeActivity).asBitmap()
                                .load(viewModel.epicImage.value?.get(indexGlobe))
                                .transform(RoundedCorners(50))
                                .into(ivGlobe)
                        }
                    } else {
                        Glide.with(this@GlobeActivity).asBitmap()
                            .load(viewModel.epicImage.value?.get(indexGlobe))
                            .transform(RoundedCorners(50))
                            .into(ivGlobe)
                    }
                }
            } catch (ignored: Exception) {
            }
        }

        btPreviousGlobe.setOnClickListener {
            try {
                if (indexGlobe - 1 >= 0) {
                    indexGlobe--
                    Glide.with(this).asBitmap()
                        .load(viewModel.epicImage.value?.get(indexGlobe))
                        .transform(RoundedCorners(50))
                        .into(ivGlobe)

                    Toast.makeText(
                        this@GlobeActivity,
                        "indexGlobe: $indexGlobe",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (ignored: Exception) {
            }
        }
        setUpMenuBehavior()
    }

    override fun onResume() {
        super.onResume()
        if (!getSharedPreferences("first_time", MODE_PRIVATE).getBoolean("globe", true)) {
            return
        }
        CoroutineScope(Dispatchers.Main).launch {
            delay(1000)
            clTutorialGlobe.animate()
                .alpha(1.0f)
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(p0: Animator?) {}

                    override fun onAnimationEnd(p0: Animator?) {
                        val view =
                            View.inflate(this@GlobeActivity, R.layout.astrodialog, null)
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
                            }.create()

                        dialog.window?.setDimAmount(0f)
                        dialog.show()

                        fabData.startAnimation(animation)
                    }

                    override fun onAnimationCancel(p0: Animator?) {}

                    override fun onAnimationRepeat(p0: Animator?) {}
                }).duration = 500

            delay(500)
            fabData.startAnimation(animation)
        }

    }

    override fun onBackPressed() {
        val sharedPreferences = getSharedPreferences("first_time", MODE_PRIVATE).edit()
        sharedPreferences.putBoolean("globe", false)
        sharedPreferences.apply()
        super.onBackPressed()
    }
}