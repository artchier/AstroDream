package com.example.astrodream.ui.globe

import android.animation.Animator
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.View.*
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.DatePicker
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_UNLOCKED
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.astrodream.R
import com.example.astrodream.services.service
import com.example.astrodream.ui.ActivityWithTopBar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_avatar.*
import kotlinx.android.synthetic.main.activity_globe.*
import kotlinx.android.synthetic.main.activity_globe.view.*
import kotlinx.android.synthetic.main.astrodialog.view.*
import kotlinx.android.synthetic.main.card_globe.*
import kotlinx.android.synthetic.main.fragment_recent_mars.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.relex.circleindicator.CircleIndicator
import java.text.SimpleDateFormat
import java.util.*

class GlobeActivity : ActivityWithTopBar(R.string.globo, R.id.dlGlobe) {

    private lateinit var date: Date
    private var lastDate: Long = 0
    private var hasSettedMax = false
    private var hasClickedOnNewDate = false
    private var day = 0
    private var month = 0
    private var year = 0
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

        viewModel.getAllAvailableEPIC()

        //pega todas as datas disponíveis e faz a requisição da última data
        viewModel.epicAvailableDates.observe(this) {
            val chosenDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(it.last())!!

            viewModel.getAllEPIC(it.last())

            val textViewLabel = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(chosenDate)
            tvData.text = "${textViewLabel[0].toUpperCase()}${textViewLabel.substring(1)}"
        }

        val indicator = ciGlobe as CircleIndicator

        //clique do botão "Escolher Data"
        fabData.setOnClickListener {

            //pega a data mais recente com imagens disponíveis
            if (!hasSettedMax) {
                lastDate = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).parse(tvData.text.toString())!!.time
                hasSettedMax = true
            }

            //inicializa o DatePicker
            val datePicker = DatePicker((ContextThemeWrapper(this, R.style.DatePicker)), null)

            //seta a posição do "marcador de data" do date picker
            if (year != 0 && day != 0)
                datePicker.updateDate(year, month, day)

            //seta a data mínima disponível do date picker
            datePicker.minDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse("2015-06-13")!!.time

            //seta a data máxima disponível do date picker
            datePicker.maxDate = lastDate

            MaterialAlertDialogBuilder(this)
                .setView(datePicker)
                .setPositiveButton(resources.getString(R.string.ok)) { _, _ ->

                    day = datePicker.dayOfMonth
                    month = datePicker.month
                    year = datePicker.year

                    date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse("$year-${month + 1}-$day")!!

                    if (date != SimpleDateFormat(
                            "MMM dd, yyyy",
                            Locale.getDefault()
                        ).parse(
                            tvData.text.toString()
                        )
                    ) {
                        val textViewLabel2 = SimpleDateFormat(
                            "MMM dd, yyyy",
                            Locale.getDefault()
                        ).format(date).toString()
                        tvData.text =
                            "${textViewLabel2[0].toUpperCase()}${textViewLabel2.substring(1)}"
                        viewModel.getAllEPIC(
                            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                                date
                            ).toString()
                        )
                        hasClickedOnNewDate = true
                    }
                }
                .setNegativeButton(resources.getString(R.string.cancelar), null)
                .show()
        }

        realtimeViewModel.activeUser.observe(this) {
            tvTotalGlobe.text = it.nasaCoins.toString()
        }

        viewModel.imageArray.observe(this) {
            //se a lista não estiver vazia, faz as requisições
            if (it.isNotEmpty()) {
                val chosenDate = it.first().substring(8, 16)
                globeAdapter = GlobeAdapter(it, this, chosenDate)
                vpGlobe.adapter = globeAdapter
                indicator.setViewPager(vpGlobe)

                //se clicar em uma nova data, inicia as animações dos NasaCoins
                if (hasClickedOnNewDate) {
                    realtimeViewModel.animateNasaCoins(
                        llNasaCoinsGlobe,
                        tvTotalGlobe,
                        R.string.globo
                    )
                    hasClickedOnNewDate = false
                }
            } else {
                //senão, pede ao usuário que escolha uma data diferente
                vpGlobe.adapter = null
                indicator.setViewPager(null)
                Snackbar.make(clSnackbar, "Imagens indisponíveis. Escolha uma data diferente por favor.", Snackbar.LENGTH_LONG)
                    .setBackgroundTint(resources.getColor(R.color.gigas, null))
                    .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
                    .setTextColor(resources.getColor(R.color.white, null))
                    .setAction("Ok") {
                        fabData.performClick()
                    }
                    .setActionTextColor(resources.getColor(R.color.lucky_point, null))
                    .show()
            }
        }
        setUpMenuBehavior()
    }

    override fun onResume() {
        val firstTimePreference = getSharedPreferences("com.example.astrodream.first_time", MODE_PRIVATE)
        if (!firstTimePreference.getBoolean("globe", true)) {
            super.onResume()
            return
        }

        fabData.isClickable = false
        dlGlobe.setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED)

        CoroutineScope(Dispatchers.Main).launch {
            delay(1000)
            clTutorialGlobe.animate()
                .alpha(1.0f)
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(p0: Animator?) {}

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
                                dlGlobe.setDrawerLockMode(LOCK_MODE_UNLOCKED)
                                firstTimePreference.edit().putBoolean("globe", false).apply()

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
        super.onResume()
    }

    override fun onStop() {
        super.onStop()

        realtimeViewModel.updateUserNasaCoins(
            realtimeViewModel.activeUser.value?.email!!,
            tvTotalGlobe.text.toString().toLong()
        )
    }
}
