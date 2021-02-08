package com.example.astrodream.ui.initial

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.astrodream.R
import com.example.astrodream.database.AppDatabase
import com.example.astrodream.services.*
import com.example.astrodream.ui.ActivityWithTopBar
import com.example.astrodream.ui.asteroids.AsteroidActivity
import com.example.astrodream.ui.dailyimage.DailyImageActivity
import com.example.astrodream.ui.globe.GlobeActivity
import com.example.astrodream.ui.mars.MarsActivity
import com.example.astrodream.ui.plaindailymars.PlainActivityType
import com.example.astrodream.ui.plaindailymars.PlainViewModel
import com.example.astrodream.ui.tech.TechActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.android.synthetic.main.activity_initial.*
import kotlinx.android.synthetic.main.welcome_dialog.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class InitialActivity : ActivityWithTopBar(R.string.app_name, R.id.dlInitial) {

    private lateinit var db: AppDatabase
    private lateinit var repository: ServiceDBDaily
    private lateinit var animator: ValueAnimator
    private lateinit var welcome: View
    private lateinit var dialog: MaterialAlertDialogBuilder

    companion object {
        private var alreadyUpdated = false
    }

    private val viewModel by viewModels<PlainViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return PlainViewModel(service, PlainActivityType.Initial, repository) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial)
        AndroidThreeTen.init(this)

        db = AppDatabase.invoke(this)
        repository = ServiceDBImplementationDaily(db.dailyDAO())

        val lastLogin =
            getSharedPreferences("com.example.astrodream.first_time", MODE_PRIVATE)
                .getString(
                    "lastLogin",
                    SimpleDateFormat.getDateInstance().format(Calendar.getInstance().time)
                        .toString()
                )!!

        val lastTime =
            getSharedPreferences(
                "com.example.astrodream.first_time", MODE_PRIVATE
            ).getInt("loginTimes", 0)

        welcome = View.inflate(this, R.layout.welcome_dialog, null)

        dialog = MaterialAlertDialogBuilder(this).setView(welcome).setBackground(
            ContextCompat.getColor(
                this,
                android.R.color.transparent
            ).toDrawable()
        )

        dialog.setOnDismissListener {
            when (lastTime) {
                0, 1, 2 -> {
                    realtimeViewModel.updateUserNasaCoins(
                        realtimeViewModel.activeUser.value?.email!!,
                        realtimeViewModel.activeUser.value?.nasaCoins!!.plus(50)
                    )
                }
                3, 4 -> {
                    realtimeViewModel.updateUserNasaCoins(
                        realtimeViewModel.activeUser.value?.email!!,
                        realtimeViewModel.activeUser.value?.nasaCoins!!.plus(100)
                    )
                }
                else -> {
                    realtimeViewModel.updateUserNasaCoins(
                        realtimeViewModel.activeUser.value?.email!!,
                        realtimeViewModel.activeUser.value?.nasaCoins!!.plus(150)
                    )
                }
            }
            dlInitial.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        }

        val showDialog = dialog.create()

        if (isConsecutive(SimpleDateFormat.getDateInstance().parse(lastLogin)!!)) {
            when (lastTime) {
                1, 2 -> {
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(1000)
                        showDialog.show()
                    }
                    animator = ValueAnimator.ofInt(0, 50)
                    animator.addUpdateListener {
                        welcome.tvTotalNasaCoins.text = it.animatedValue.toString()
                    }
                    animator.duration = 500
                    animator.addListener(object : Animator.AnimatorListener {
                        override fun onAnimationStart(p0: Animator?) {
                        }

                        override fun onAnimationEnd(p0: Animator?) {
                        }

                        override fun onAnimationCancel(p0: Animator?) {
                        }

                        override fun onAnimationRepeat(p0: Animator?) {
                        }

                    })
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(1000)
                        animator.start()
                    }
                }
                3, 4 -> {
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(1000)
                        showDialog.show()
                    }
                    animator = ValueAnimator.ofInt(0, 100)
                    animator.addUpdateListener {
                        welcome.tvTotalNasaCoins.text = it.animatedValue.toString()
                    }
                    animator.duration = 500
                    animator.addListener(object : Animator.AnimatorListener {
                        override fun onAnimationStart(p0: Animator?) {
                        }

                        override fun onAnimationEnd(p0: Animator?) {
                        }

                        override fun onAnimationCancel(p0: Animator?) {
                        }

                        override fun onAnimationRepeat(p0: Animator?) {
                        }

                    })
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(1000)
                        animator.start()
                    }
                }
                else -> {
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(1000)
                        showDialog.show()
                    }
                    animator = ValueAnimator.ofInt(0, 150)
                    animator.addUpdateListener {
                        welcome.tvTotalNasaCoins.text = it.animatedValue.toString()
                    }
                    animator.duration = 500
                    animator.addListener(object : Animator.AnimatorListener {
                        override fun onAnimationStart(p0: Animator?) {
                        }

                        override fun onAnimationEnd(p0: Animator?) {
                        }

                        override fun onAnimationCancel(p0: Animator?) {
                        }

                        override fun onAnimationRepeat(p0: Animator?) {
                        }

                    })
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(1000)
                        animator.start()
                    }
                }
            }
            if (!alreadyUpdated) {
                getSharedPreferences(
                    "com.example.astrodream.first_time",
                    MODE_PRIVATE
                ).edit()
                    .putInt("loginTimes", lastTime + 1).apply()

                getSharedPreferences(
                    "com.example.astrodream.first_time",
                    MODE_PRIVATE
                ).edit()
                    .putString(
                        "lastLogin",
                        SimpleDateFormat.getDateInstance().format(Calendar.getInstance().time)
                            .toString()
                    )
                    .apply()
                alreadyUpdated = true
            }
        } else {
            if (!alreadyUpdated) {
                getSharedPreferences(
                    "com.example.astrodream.first_time",
                    MODE_PRIVATE
                ).edit()
                    .putInt("loginTimes", 1).apply()

                getSharedPreferences(
                    "com.example.astrodream.first_time",
                    MODE_PRIVATE
                ).edit()
                    .putString(
                        "lastLogin",
                        SimpleDateFormat.getDateInstance().format(Calendar.getInstance().time)
                            .toString()
                    )
                    .apply()
                alreadyUpdated = true
            }
        }

        dailyImage()
        cvDaily.setOnClickListener {
            startActivity(Intent(this, DailyImageActivity::class.java))
        }

        btAsteriodes.setOnClickListener {
            startActivity(Intent(this, AsteroidActivity::class.java))
        }

        btGlobo.setOnClickListener {
            startActivity(Intent(this, GlobeActivity::class.java))
        }

        btTecnologias.setOnClickListener {
            startActivity(Intent(this, TechActivity::class.java))
        }

        btMarte.setOnClickListener {
            startActivity(Intent(this, MarsActivity::class.java))
        }

        setUpMenuBehavior()
    }

    private fun dailyImage() {
        piInitial.show()
        viewModel.populateList()
        viewModel.focusResult.observe(this) {
            val img = if (it.url != "") {
                it.url
            } else {
                R.drawable.lost_connection
            }
            piInitial.hide()
            Glide.with(this).asBitmap()
                .load(img)
                .into(ivDaily)
            if (it.url == "") {
                tvImagemHoje.text = ""
            }
        }
    }

    private fun isConsecutive(date: Date): Boolean {
        val calendar1 = Calendar.getInstance()
        val calendar2 = Calendar.getInstance()

        calendar1.time = date
        val date1 =
            arrayOf(calendar1.get(Calendar.DAY_OF_MONTH), calendar1.get(Calendar.MONTH + 1))
        val date2 =
            arrayOf(calendar2.get(Calendar.DAY_OF_MONTH), calendar2.get(Calendar.MONTH + 1))

        return (date1[0] == date2[0] && date1[1] == date2[1])
    }
}