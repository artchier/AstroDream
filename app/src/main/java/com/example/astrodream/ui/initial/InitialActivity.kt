package com.example.astrodream.ui.initial

import android.animation.ValueAnimator
import android.annotation.SuppressLint
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
import com.example.astrodream.services.ServiceDBDaily
import com.example.astrodream.services.ServiceDBImplementationDaily
import com.example.astrodream.services.service
import com.example.astrodream.ui.ActivityWithTopBar
import com.example.astrodream.ui.SplashScreenActivity
import com.example.astrodream.ui.asteroids.AsteroidActivity
import com.example.astrodream.ui.dailyimage.DailyImageActivity
import com.example.astrodream.ui.globe.GlobeActivity
import com.example.astrodream.ui.login.LoginActivity
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

    private val viewModel by viewModels<PlainViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return PlainViewModel(service, PlainActivityType.Initial, repository) as T
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial)
        AndroidThreeTen.init(this)

        db = AppDatabase.invoke(this)
        repository = ServiceDBImplementationDaily(db.dailyDAO())

        val today = Calendar.getInstance()
        val todayString = dateFormat.format(today.time)
        val dailyLoginPreferences = getSharedPreferences("daily_login", MODE_PRIVATE)

        val lastLogin = dailyLoginPreferences.getString("lastLogin", "1980-01-01")!!
        var consecutiveDays = dailyLoginPreferences.getInt("consecutiveDays", 0)

        // If it's the first time we're login on today, execute this code
        if (lastLogin != todayString) {
            if (isConsecutive(lastLogin, today)) {
                consecutiveDays += 1
            } else {
                consecutiveDays = 0
            }

            val coinsEarned = when (consecutiveDays) {
                0, 1, 2 -> 50
                3, 4 -> 100
                else -> 150
            }

            val welcome = View.inflate(this, R.layout.welcome_dialog, null)
            val welcomeDialog = MaterialAlertDialogBuilder(this).setView(welcome).setBackground(
                ContextCompat.getColor(this, android.R.color.transparent).toDrawable()
            )

            welcomeDialog.setOnDismissListener {
                realtimeViewModel.updateUserNasaCoins(
                    realtimeViewModel.activeUser.value?.email!!,
                    realtimeViewModel.activeUser.value?.nasaCoins!!.plus(coinsEarned)
                )
                dlInitial.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }

            val showDialog = welcomeDialog.create()

            CoroutineScope(Dispatchers.Main).launch {
                delay(1000)
                showDialog.show()
            }
            animator = ValueAnimator.ofInt(0, coinsEarned)
            animator.addUpdateListener {
                welcome.tvTotalNasaCoins.text = it.animatedValue.toString()
            }
            animator.duration = 500
            CoroutineScope(Dispatchers.Main).launch {
                delay(1000)
                animator.start()
            }

            dailyLoginPreferences.edit().putInt("consecutiveDays", consecutiveDays).apply()
            dailyLoginPreferences.edit().putString("lastLogin", todayString).apply()
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
                tvImagemHoje.text = ""
                R.drawable.lost_connection
            }
            piInitial.hide()
            Glide.with(this).asBitmap()
                .load(img)
                .into(ivDaily)
        }
    }

    private fun isConsecutive(lastLogin: String, today: Calendar): Boolean {
        val yesterday = (today.clone() as Calendar).apply {
            add(Calendar.DATE, -1)
        }

        return dateFormat.format(yesterday.time) == lastLogin
    }

    override fun onBackPressed() {
        super.onBackPressed()

        //essa linha garante que a tela de login seja exibida novamente caso o usuário saia do app
        // por meio do botão de voltar e não o encerre
        SplashScreenActivity.alreadyLogged = true
    }
}