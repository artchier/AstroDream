package com.example.astrodream.ui

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.astrodream.R
import com.example.astrodream.ui.login.LoginActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        CoroutineScope(Dispatchers.Main).launch{
            delay(1000)
            callInitialActivity()
            finish()
        }
        /*Handler().postDelayed({

        }, 1000)*/
    }

    private fun callInitialActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}